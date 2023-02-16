package com.vaadin.recipes.recipe.sortwithrenderer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("sort-with-renderer")
@Metadata(
    howdoI = "Enable sorting for a Grid column with a custom renderer",
    description = "When using a custom renderer for a Grid with in-memory items , it might be necessary to customize how sorting is applied for that column.",
    tags = { Tag.GRID }
)
public class SortWithRenderer extends Recipe {

    public SortWithRenderer() {
        Grid<Integer> gridWithoutRenderer = createExampleGrid();
        gridWithoutRenderer.addColumn(number -> number).setHeader("Without comparator").setSortable(true);
        add("Rendered as a number. No custom comparator needed.", gridWithoutRenderer);

        Grid<Integer> stringGrid = createExampleGrid();
        stringGrid.addColumn(number -> "Value: " + number).setSortable(true).setHeader("Without comparator");
        stringGrid
            .addColumn(number -> "Value: " + number)
            .setSortable(true)
            .setHeader("With comparator")
            .setComparator(number -> number);
        add("Rendered as a string. Without custom comparator, sorting will use alphabetic order.", stringGrid);

        Grid<Integer> rendererGrid = createExampleGrid();
        NumberRenderer<Integer> numberRenderer = new NumberRenderer<>(number -> number, "Value: %d");
        rendererGrid.addColumn(numberRenderer).setSortable(true).setHeader("Without comparator");
        rendererGrid
            .addColumn(numberRenderer)
            .setSortable(true)
            .setHeader("With comparator")
            .setComparator(number -> number);
        add("Rendered using NumberRenderer. Without custom comparator, nothing is sorted.", rendererGrid);
    }

    private Grid<Integer> createExampleGrid() {
        Grid<Integer> grid = new Grid<>();

        grid.setAllRowsVisible(true);
        grid.setItems(2, 100, 30);

        return grid;
    }

    private void add(String caption, Component component) {
        Div captionDiv = new Div();
        captionDiv.setText(caption);

        add(captionDiv, component);
    }
}
