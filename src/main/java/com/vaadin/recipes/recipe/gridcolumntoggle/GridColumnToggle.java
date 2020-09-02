package com.vaadin.recipes.recipe.gridcolumntoggle;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.util.HashMap;
import java.util.Map;

@Route("grid-column-toggle")
@Metadata(howdoI = "Show a popup to toggle grid columns")
public class GridColumnToggle extends Recipe {

    private final Map<Column<?>, String> toggleableColumns = new HashMap<>();

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

        Column<Rectangle> settingColumn = grid.addColumn(box -> "").setWidth("auto").setFlexGrow(0);
        grid.getHeaderRows().get(0).getCell(settingColumn).setComponent(createMenuToggle());

        add(grid);
    }

    private MenuBar createMenuToggle() {
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        MenuItem menuItem = menuBar.addItem(VaadinIcon.ELLIPSIS_DOTS_V.create());
        SubMenu subMenu = menuItem.getSubMenu();

        toggleableColumns.forEach((column, header) -> {
            Checkbox checkbox = new Checkbox(header);
            checkbox.setValue(column.isVisible());
            checkbox.addValueChangeListener(e -> column.setVisible(e.getValue()));
            subMenu.addItem(checkbox);
        });

        return menuBar;
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
