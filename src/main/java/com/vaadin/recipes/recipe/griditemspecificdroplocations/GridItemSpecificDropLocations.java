package com.vaadin.recipes.recipe.griditemspecificdroplocations;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-item-specific-drop-locations")
@Metadata(howdoI = "Define item specific drop locations for Grid",
    description = "This recipe shows how drop locations can be enabled individually for each Grid row.",
    sourceFiles = {"recipe/grid-item-specific-drop-locations/grid-drop-locations.css" },
    tags = { Tag.GRID })
@CssImport(value = "./recipe/grid-item-specific-drop-locations/grid-drop-locations.css", themeFor = "vaadin-grid")
public class GridItemSpecificDropLocations extends Recipe {

    private Integer draggedItem;

    public GridItemSpecificDropLocations() {
        Grid<Integer> grid = new Grid<>();
        grid.addColumn(item -> item).setHeader("Id").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(item -> {
            switch (item) {
                case 0:
                    return "can be dropped between 0 and 1 OR below 2";
                case 1:
                    return "can be dropped on top of 0 OR on top of 2";
                case 2:
                    return "can be dropped between 0 and 1 OR on top of 0";
                default:
                    return "";
            }
        }).setHeader("Rules");
        grid.setItems(0, 1, 2);
        grid.setSelectionMode(SelectionMode.NONE);
        grid.setRowsDraggable(true);

        grid.setPartNameGenerator(item -> {
            if (draggedItem == null) {
                return "";
            }

            List<String> classNames = new ArrayList<>();
            if (!isLegalDrop(draggedItem, item, GridDropLocation.ABOVE)) {
                classNames.add("drop-above-disabled");
            }
            if (!isLegalDrop(draggedItem, item, GridDropLocation.ON_TOP)) {
                classNames.add("drop-on-top-disabled");
            }
            if (!isLegalDrop(draggedItem, item, GridDropLocation.BELOW)) {
                classNames.add("drop-below-disabled");
            }
            return String.join(" ", classNames);
        });

        grid.addDragStartListener(event -> {
            draggedItem = event.getDraggedItems().get(0);
            grid.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
            // This makes the class name genrator re-run. Use the more lightweight
            // grid.getDataCommunicator().refresh(item) to only refresh specific items
            grid.getDataCommunicator().reset();
        });

        grid.addDropListener(event -> {
            Integer dropOverItem = event.getDropTargetItem().get();
            GridDropLocation dropLocation = event.getDropLocation();
            if (isLegalDrop(draggedItem, dropOverItem, dropLocation)) {
                Notification.show(draggedItem + " was dropped " + dropLocation + " " + dropOverItem);
            }
        });

        grid.addDragEndListener(event -> {
            draggedItem = null;
            grid.setDropMode(null);
        });

        add(grid);
    }

    /**
     * Application logic based, item specific rules for legal drops. In the case of
     * this application, the following rules apply:
     *
     * - 0 can be dropped between 0 and 1 OR below 2
     *
     * - 1 can be dropped on top of 0 OR on top of 2
     *
     * - 2 can be dropped between 0 and 1 OR on top of 0
     */
    private boolean isLegalDrop(Integer draggedItem, Integer targetItem, GridDropLocation dropLocation) {
        switch (draggedItem) {
            case 0:
                return targetItem.equals(0) && GridDropLocation.BELOW.equals(dropLocation)
                        || targetItem.equals(1) && GridDropLocation.ABOVE.equals(dropLocation)
                        || targetItem.equals(2) && GridDropLocation.BELOW.equals(dropLocation);
            case 1:
                return targetItem.equals(0) && GridDropLocation.ON_TOP.equals(dropLocation)
                        || targetItem.equals(2) && GridDropLocation.ON_TOP.equals(dropLocation);
            case 2:
                return targetItem.equals(0) && GridDropLocation.ON_TOP.equals(dropLocation)
                        || targetItem.equals(0) && GridDropLocation.BELOW.equals(dropLocation)
                        || targetItem.equals(1) && GridDropLocation.ABOVE.equals(dropLocation);
            default:
                return false;
        }

    }

}
