package com.vaadin.recipes.recipe.griddndreorderrows;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.util.ArrayList;
import java.util.List;

@Route("grid-dnd-reorder-rows")
@Metadata(
    howdoI = "Drag and drop rows to reorder a Grid",
    description = "Learn how to enable drag and drop reordering of rows in a Vaadin Java grid. ",
    tags = { Tag.GRID }
)
public class GridDndReorderRows extends Recipe {
    private Person draggedItem;
    private List<Person> gridItems;

    public GridDndReorderRows() {
        gridItems = getTestData();
        Grid<Person> grid = new Grid<>(Person.class);
        grid.setColumns("firstName", "lastName");
        grid.setItems(gridItems);
        grid.setSortableColumns();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setRowsDraggable(true);

        grid.addDragStartListener(
            event -> {
                // store current dragged item so we know what to drop
                draggedItem = event.getDraggedItems().get(0);
                grid.setDropMode(GridDropMode.BETWEEN);
            }
        );

        grid.addDragEndListener(
            event -> {
                draggedItem = null;
                // Once dragging has ended, disable drop mode so that
                // it won't look like other dragged items can be dropped
                grid.setDropMode(null);
            }
        );

        grid.addDropListener(
            event -> {
                Person dropOverItem = event.getDropTargetItem().get();
                if (!dropOverItem.equals(draggedItem)) {
                    // reorder dragged item the backing gridItems container
                    gridItems.remove(draggedItem);
                    // calculate drop index based on the dropOverItem
                    int dropIndex =
                        gridItems.indexOf(dropOverItem) + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0);
                    gridItems.add(dropIndex, draggedItem);
                    grid.getDataProvider().refreshAll();
                }
            }
        );
        add(grid);
    }

    private List<Person> getTestData() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Jake", "Peralta"));
        persons.add(new Person("Rosa", "Diaz"));
        persons.add(new Person("Terry", "Jeffords"));
        persons.add(new Person("Amy", "Santiago"));
        persons.add(new Person("Charles", "Boyle"));
        persons.add(new Person("Raymond", "Holt"));
        return persons;
    }

    public static class Person {
        private String firstName;
        private String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
