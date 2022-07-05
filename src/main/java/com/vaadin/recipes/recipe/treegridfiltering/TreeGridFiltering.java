package com.vaadin.recipes.recipe.treegridfiltering;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("treegrid-filtering")
@Metadata(
        howdoI = "filter a Vaadin TreeGrid",
        description = "Learn how to filter the contents of a Vaadin TreeGrid.",
        tags = Tag.GRID
)
public class TreeGridFiltering extends Recipe {

    public TreeGridFiltering() {
        TreeGrid<String> grid = new TreeGrid<>();
        grid.addHierarchyColumn(s -> s);

        TreeDataProvider<String> dataProvider = new TreeDataProvider<>(
                generateData());
        grid.setDataProvider(dataProvider);

        TextField filterField = new TextField();
        filterField.setValueChangeMode(ValueChangeMode.EAGER);

        filterField.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                dataProvider.setFilter(null);
            } else {
                dataProvider.setFilter(item -> item.toLowerCase()
                        .contains(event.getValue().toLowerCase()));
            }
            grid.expandRecursively(dataProvider.getTreeData().getRootItems(),
                    99);
        });
        add(filterField, grid);
    }

    private TreeData<String> generateData() {
        TreeData<String> data = new TreeData<>();

        data.addRootItems("planning", "transportation", "personnel logistics",
                "vessel log", "configuration", "system", "user data");

        data.addItems("planning", //
                "operational plan", "transportation plan", "group schedule",
                "bulk schedule", "personnel schedule", "generate schedule log",
                "line overview", "resource plan", "shift plan");

        data.addItems("transportation", //
                "transportation summary", "visual transportation summary",
                "visual status", "visual analysis", "map", "ata/atd",
                "dfr control", "dummy transportation", "traffic log");

        data.addItems("personnel logistics", //
                "reservation", "check-in", "request & approval",
                "group schedule: request & approval", "pob plan",
                "approve purpose of visit", "approve replacement", "cargo",
                "standby", "waiting list", "upload reservation",
                "information to traveller", "reservations with deviations",
                "move to new company / department / job",
                "charter reservations", "reservation overview",
                "terminal process");
        data.addItems("terminal process", //
                "id control", "luggage drop", "security control",
                "survival suit delivery", "safety briefing",
                "terminal overview");

        data.addItems("vessel log", //
                "vessel activities", "vessel current totals");

        return data;
    }
}
