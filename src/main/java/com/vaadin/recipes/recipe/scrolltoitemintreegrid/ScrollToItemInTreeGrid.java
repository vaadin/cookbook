package com.vaadin.recipes.recipe.scrolltoitemintreegrid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Route("scroll-to-item-in-tree-grid")
@Metadata(
    howdoI = "Scroll a TreeGrid to an item and expand nodes",
    description = "Code example for scrolling to a specific item in a TreeGrid, expanding nodes as needed.",
    tags = { Tag.GRID }
)
public class ScrollToItemInTreeGrid extends Recipe {

    private static class ScrollTreeGrid<T> extends TreeGrid<T> {

        /**
         * @param item the item where to scroll to.
         */
        public void scrollToItemAndExpand(T item) {

            select(item);

            ArrayList<T> items = new ArrayList<>();
            ArrayList<Integer> indices = new ArrayList<>();

            items.add(item);

            T parent;
            do {
                //T current = items.getLast(); // later Java versions
                T current = items.get(items.size()-1);

                parent = getTreeData().getParent(current); // this can be considered back-end access

                int index = getTreeData().getChildren(parent).indexOf(current);
                indices.add(index);

                Notification.show("Item '" + current + "' has parent '" + parent + "'.");
                if (parent != null) {
                    items.add(parent);
                }
            }
            while (parent != null);
            // items now has all the items from 'item' up to a root item (which it could be itself).

            //expand(items.reversed()); // later Java versions
            List<T> shallowItemsCopy = items.subList(0, items.size());
            Collections.reverse(shallowItemsCopy);
            expand(shallowItemsCopy);

            // int[] in = indices.reversed().stream().mapToInt(Integer::intValue).toArray(); // later Java versions
            List<Integer> shallowIndicesCopy = indices.subList(0, indices.size());
            Collections.reverse(shallowIndicesCopy);
            scrollToIndex(shallowIndicesCopy.stream().mapToInt(Integer::intValue).toArray());
        }
    }

    /**
     * Data entiry to use in the treegrid
     *
     */
    public static class Person {
        private String name;
        private Person parent;

        public Person() {}

        public Person(String name, Person parent) {
            this.name = name;
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public Person getParent() {
            return parent;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setParent(Person parent) {
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
			return Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Demo
     *
     */
    public ScrollToItemInTreeGrid() {
        ScrollTreeGrid<Person> grid = new ScrollTreeGrid<>();
        grid.addHierarchyColumn(p -> p.getName()).setHeader("Name");
        Person grandDad = new Person("Granddad", null);
        Person dad = new Person("Dad", grandDad);
        List<Person> people = new ArrayList<>();
        people.add(grandDad);
        people.add(dad);
        Person firstChild = new Person("First Child", dad);
        people.add(firstChild);
        for (int i = 2; i < 100; i++) {
            Person child = new Person("Child " + i, dad);
            people.add(child);
        }
        Person lastChild = new Person("Last Child", dad);
        people.add(lastChild);
        people.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));
        add(grid);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(
            new Button(
                "Expand and scroll to first",
                e -> {
                    grid.scrollToItemAndExpand(firstChild);
                }
            )
        );
        buttons.add(
            new Button(
                "Expand and scroll to last",
                e -> {
                    grid.scrollToItemAndExpand(lastChild);
                }
            )
        );
        buttons.add(
            new Button(
                "Expand and scroll to Child 42",
                e -> {
                    // note that we have engineered Person equality to solely depend on the name
                    grid.scrollToItemAndExpand(new Person("Child 42", null));
                }
            )
        );
        add(buttons);
    }
}
