package com.vaadin.recipes.recipe.scrollgridssidebyside;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.ArrayList;
import java.util.List;

@Route("scroll-grids-side-by-side")
@Metadata(
        howdoI = "Scroll two Grids synchronously",
        description = "Code example for scrolling two grids synchronously. The use case could be side by side comparison of grids.",
        sourceFiles = {"recipe/scrollgridssidebyside/grid-sync.js"},
        tags = {Tag.GRID}
)
@JsModule("./recipe/scrollgridssidebyside/grid-sync.js")
public class ScrollGridsSideBySide extends Recipe {

    private final Grid<Person> beforeGrid;
    private final Grid<Person> afterGrid;

    public ScrollGridsSideBySide() {
        beforeGrid = withClientsideScrollListener(new Grid<>(Person.class));
        afterGrid = withClientsideScrollListener(new Grid<>(Person.class));

        beforeGrid.setItems(createPeopleBeforeData());
        afterGrid.setItems(createPeopleAfterData());
        beforeGrid.setColumns("id", "name", "bodyTemp", "pulse");
        afterGrid.setColumns("name", "bodyTemp", "pulse");

        HorizontalLayout comparisonLayout = new HorizontalLayout();
        comparisonLayout.setSizeFull();
        comparisonLayout.add(beforeGrid, afterGrid);
        add(comparisonLayout);
    }

    /**
     * Add client-side scroll listener to grid
     */
    private Grid<Person> withClientsideScrollListener(Grid<Person> grid) {
        grid.getElement().executeJs("syncGrid($0)", grid.getElement());
        return grid;
    }

    /**
     * Items for the first grid
     */
    private List<Person> createPeopleBeforeData() {
        List<Person> peopleBefore = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            Person person1 = new Person(i, "Person " + i + " (before)", 37 + Math.random(), 70 + (int) (Math.random() * 10));
            peopleBefore.add(person1);
        }
        return peopleBefore;
    }

    /**
     * Items for the second grid
     */
    private List<Person> createPeopleAfterData() {
        List<Person> peopleAfter = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            Person person2 = new Person(i, "Person " + i + " (after)", 37 - Math.random(), 70 - (int) (Math.random() * 10));
            peopleAfter.add(person2);
        }
        return peopleAfter;
    }

    /**
     * Example DTO
     */
    public static class Person {
        private int id;
        private String name;
        private double bodyTemp;
        private int pulse;

        public Person(int id, String name, double bodyTemp, int pulse) {
            this.id = id;
            this.name = name;
            this.bodyTemp = bodyTemp;
            this.pulse = pulse;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public double getBodyTemp() {
            return bodyTemp;
        }

        public int getPulse() {
            return pulse;
        }
    }
}
