package com.vaadin.recipes.recipe.griddrilldown;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Route("grid-drill-down")
@Metadata(
    howdoI = "Do drill down pattern with Grid of huge amount of columns",
    description = "Sometimes Grid can grow super large, think about a year report. This Grid shows simplified example of year by week report with monthly drill down. This makes Grid more performant as not all the columns are rendered all the time.",
    sourceFiles = "recipe/griddrilldown/griddrilldown.css"
)
@CssImport(value = "./recipe/griddrilldown/griddrilldown.css", themeFor = "vaadin-grid")
public class GridDrillDown extends Recipe {

    public GridDrillDown() {
        Grid<List<Integer>> grid = createGrid();
        add(grid);
    }

    private Grid<List<Integer>> createGrid() {
        // Instead of usig bean, you can use List or HashMap for column values
        // here we use integer value for column id, so List is the simplest case.
        final Grid<List<Integer>> grid = new Grid<>();

        Binder<List<Integer>> binder = new Binder<>();
        grid.getEditor().setBinder(binder);
        grid.getEditor().setBuffered(false);

        int week = 1;
        for (int i = 0; i < 65; i++) {
            final int index = i;
            // add column with value provider and renderer
            TextField textField = new TextField();
            binder
                .forField(textField)
                .withConverter(new StringToIntegerConverter("Not a number"))
                .bind(list -> list.get(index), (list, value) -> list.set(index, value));
            if ((i % 5) == 0) {
                grid
                    .addColumn(list -> list.get(index))
                    .setKey("" + i)
                    .setHeader("M" + (i / 5))
                    .setPartNameGenerator(item -> "month");
            } else {
                grid
                    .addColumn(list -> list.get(index))
                    .setKey("" + i)
                    .setHeader("W" + (week))
                    .setEditorComponent(textField);
                week++;
            }
        }

        // One approach to improve performnce of the large Grid view is to
        // add drill down pattern. Here is simplified example of the idea, uncomment
        // the code to see the effect
        HeaderRow header = grid.prependHeaderRow();
        for (int i = 0; i < 65; i++) {
            if (i % 5 == 0) {
                final int index = i;
                Button button = new Button("M" + i / 5);
                button.addThemeVariants(ButtonVariant.LUMO_SMALL);
                button.addClickListener(
                    event -> {
                        if (grid.getEditor().isOpen()) {
                            grid.getEditor().save();
                            grid.getEditor().closeEditor();
                        }
                        if (grid.getColumnByKey("" + (index + 1)).isVisible()) {
                            grid.getColumnByKey("" + (index + 1)).setVisible(false);
                            grid.getColumnByKey("" + (index + 2)).setVisible(false);
                            grid.getColumnByKey("" + (index + 3)).setVisible(false);
                            grid.getColumnByKey("" + (index + 4)).setVisible(false);
                        } else {
                            grid.getColumnByKey("" + (index + 1)).setVisible(true);
                            grid.getColumnByKey("" + (index + 2)).setVisible(true);
                            grid.getColumnByKey("" + (index + 3)).setVisible(true);
                            grid.getColumnByKey("" + (index + 4)).setVisible(true);
                        }
                    }
                );
                header.getCell(grid.getColumnByKey("" + i)).setComponent(button);
            } else {
                grid.getColumnByKey("" + i).setVisible(false);
            }
        }

        // Generate some data
        Random random = new Random();
        List<List<Integer>> items = new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            final List<Integer> values = new ArrayList<>();
            int sum = 0;
            for (int i = 0; i < 66; i++) {
                if (i % 5 == 0 && i > 0) {
                    values.add(i - 5, sum);
                    sum = 0;
                } else {
                    int number = random.nextInt(10000);
                    values.add(i, number);
                    sum = sum + number;
                }
            }
            values.add(j);
            items.add(values);
        }

        MyDataProvider<List<Integer>> dp = new MyDataProvider<>(items);
        grid.setItems(dp);

        grid.setWidth("100%");
        grid.setSelectionMode(SelectionMode.NONE);

        grid.addItemClickListener(
            event -> {
                if (!grid.getEditor().isOpen()) {
                    grid.getEditor().editItem(event.getItem());
                } else {}
            }
        );
        // Update sum
        grid
            .getEditor()
            .addCloseListener(
                event -> {
                    List<Integer> values = event.getItem();
                    Integer sum = 0;
                    for (int i = 0; i < 66; i++) {
                        if (i % 5 == 0 && i > 0) {
                            values.set(i - 5, sum);
                            sum = 0;
                        } else if (i > 0) {
                            Integer number = values.get(i);
                            sum = sum + number;
                        }
                    }
                    dp.refreshItem(values);
                }
            );

        return grid;
    }

    private class MyDataProvider<T> extends ListDataProvider<T> {

        public MyDataProvider(Collection<T> items) {
            super(items);
        }

        @Override
        @SuppressWarnings("rawtypes")
        public String getId(T item) {
            Objects.requireNonNull(item, "Cannot provide an id for a null item.");
            if (item instanceof List) {
                if (((List) item).size() == 67) return ((List) item).get(66).toString(); else return item.toString();
            } else {
                return item.toString();
            }
        }
    }
}
