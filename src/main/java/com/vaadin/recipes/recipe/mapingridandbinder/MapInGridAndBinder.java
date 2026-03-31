package com.vaadin.recipes.recipe.mapingridandbinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-with-map")
@Metadata(howdoI = "Use HashMap in Grid and Binder instead of POJO", description = "SerializablePredicate in Vaadin accepts both function references and Lambda experessions. Thus it is possible to setup Grid and Binder to use un-conventional data, i.e. something else than POJOs. In this recipe we show how to use HashMap as type of Grid and Binder. Input number of columns between 5 and 25 to the field to generate Grids with different amount of columns.", sourceFiles = {
        "./recipe/mapingridandbinder/mapingridandbinder.css" }, tags = {
                Tag.GRID, Tag.BINDER })
@CssImport(value = "./recipe/mapingridandbinder/mapingridandbinder.css", themeFor = "vaadin-grid")
public class MapInGridAndBinder extends Recipe {

    public MapInGridAndBinder() {
        setHeight("500px");
        IntegerField field = new IntegerField("Columns ");
        field.setMin(5);
        field.setMax(25);
        field.setStepButtonsVisible(true);
        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();
        add(field, container);

        field.addValueChangeListener(event -> {
            Integer value = event.getValue();
            container.removeAll();
            Grid<Map<String, Integer>> grid = createGrid(value);
            grid.setSizeFull();
            container.add(grid);
        });

        field.setValue(10);
    }

    private Grid<Map<String, Integer>> createGrid(int columns) {
        // Instantiate Grid with Map type
        Grid<Map<String, Integer>> grid = new Grid<>();

        List<Map<String, Integer>> items = createData(columns);
        MyDataProvider<Map<String, Integer>> dataProvider = new MyDataProvider<>(
                items);
        grid.setItems(dataProvider);

        // Instantiate Binder with Map type
        Binder<Map<String, Integer>> binder = new Binder<>();

        grid.addColumn(map -> map.get("id")).setHeader("Id");

        // Generate requested amount of columns and setup Binder for edit fields
        for (int i = 0; i < columns; i++) {
            final int index = i;
            IntegerField integerField = new IntegerField();
            binder.forField(integerField).bind(map -> map.get("col" + index),
                    (map, value) -> map.put("col" + index, value));
            grid.addColumn(map -> map.get("col" + index))
                    .setEditorComponent(integerField).setHeader("Col " + index);
            // Use small variant for the field in order to edit row look nicer
            integerField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
            integerField.addKeyDownListener(Key.ENTER, event -> {
                grid.getEditor().closeEditor();
            });
            integerField.addKeyDownListener(Key.ESCAPE, event -> {
                grid.getEditor().closeEditor();
            });
        }
        grid.getEditor().setBinder(binder);

        // Add column for the sum
        grid.addColumn(map -> map.get("sum")).setHeader("Sum")
                .setPartNameGenerator(item -> "sum");

        // Tip: Lets use item click listener as toggle for editing
        grid.addItemClickListener(event -> {
            Map<String, Integer> item = event.getItem();
            // If editor is open already, lets close it
            if (grid.getEditor().isOpen()
                    && grid.getEditor().getItem().equals(item)) {
                grid.getEditor().closeEditor();
            } else {
                grid.getEditor().editItem(item);
                // Tip: Find the field from the colum and focus it for better UX
                IntegerField field = (IntegerField) event.getColumn()
                        .getEditorComponent();
                field.focus();
            }
        });

        // Update sum when editor is closed
        grid.getEditor().addCloseListener(event -> {
            Map<String, Integer> item = event.getItem();
            int sum = 0;
            for (int i = 0; i < columns; i++) {
                sum += item.get("col" + i);
            }
            item.put("sum", sum);
            // Item refrsh is needed in order to new sum be visible
            grid.getDataProvider().refreshItem(item);
        });

        // Tip: Grid uses these values for padding, lets set them smaller value
        // so that editor will look nicer.
        grid.getStyle().set("--lumo-space-xs", "1px");
        grid.getStyle().set("--lumo-space-m", "1px");
        return grid;
    }

    // Generate some mock data
    private List<Map<String, Integer>> createData(int columns) {
        Random random = new Random();
        List<Map<String, Integer>> items = new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            final Map<String, Integer> values = new HashMap<>();
            values.put("id", j);
            int sum = 0;
            for (int i = 0; i < columns; i++) {
                int number = random.nextInt(10000);
                values.put("col" + i, number);
                sum = sum + number;
            }
            values.put("sum", sum);
            items.add(values);
        }
        return items;
    }

    // HashMap identity does not play nice with Grid by default, thus
    // we use option to override getId method of DataProvider to return value
    // of "id" key as identity.
    public class MyDataProvider<T> extends ListDataProvider<T> {

        public MyDataProvider(Collection<T> items) {
            super(items);
        }

        @Override
        public String getId(T item) {
            Objects.requireNonNull(item,
                    "Cannot provide an id for a null item.");
            if (item instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) item;
                if (map.get("id") != null)
                    return map.get("id").toString();
                else
                    return item.toString();
            } else {
                return item.toString();
            }
        }

    }
}
