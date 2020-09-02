package com.vaadin.recipes.recipe.gridcolumntoggle;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.util.HashMap;
import java.util.Map;

@Route("grid-column-toggle")
@Metadata(howdoI = "Show a popup to toggle grid columns")
public class GridColumnToggle extends Recipe {

    private final Map<Column<?>, String> toggleableColumns = new HashMap<>();
    private Dialog dialog;

    public GridColumnToggle() {
        Grid<Rectangle> grid = new Grid<>(Rectangle.class);
        Column<Rectangle> volumeColumn = grid
                .addColumn(rect -> rect.getWidth() * rect.getHeight()).setHeader("Area");
        grid.setItems(
                new Rectangle(1, 2),
                new Rectangle(2, 2),
                new Rectangle(12, 3)
        );

        toggleableColumns.put(grid.getColumnByKey("width"), "Width");
        toggleableColumns.put(volumeColumn, "Area");

        Button toggleButton = new Button("Toggle columns", e -> showToggleDialog());
        add(grid, toggleButton);
    }

    private void showToggleDialog() {
        if (dialog == null) {
            VerticalLayout layout = new VerticalLayout(new Text("Toggle column visibility"));
            dialog = new Dialog(layout);

            toggleableColumns.forEach((column, header) -> {
                Checkbox checkbox = new Checkbox(header);
                checkbox.setValue(column.isVisible());
                checkbox.addValueChangeListener(e -> column.setVisible(e.getValue()));
                layout.add(checkbox);
            });
        }
        dialog.open();
    }

    public static class Rectangle {
        private final int width;
        private final int height;

        private Rectangle(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
