package com.vaadin.recipes.recipe.scrolltwogridssidebyside;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

@Route("scroll-two-grids-side-by-side")
@Metadata(
        howdoI = "Scroll two Grids synchronously",
        description = "Code example for scrolling two grids synchronously. The use case could be side by side comparison of grids.",
        tags = {Tag.GRID}
)
public class ScrollTwoGridsSideBySide extends Recipe {

    private final Grid<Person> beforeGrid;
    private final Grid<Person> afterGrid;
    private int skipScrollEventForGridIdentifier = -1;

    public ScrollTwoGridsSideBySide() {
        beforeGrid = withClientsideScrollListener(new Grid<>(Person.class), 0);
        afterGrid = withClientsideScrollListener(new Grid<>(Person.class), 1);

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
    private Grid<Person> withClientsideScrollListener(Grid<Person> grid, int gridIdentifier) {
        grid.getElement().executeJs(
                "this.$.table.addEventListener('scroll', (scrollEvent) => " +
                        "{requestAnimationFrame(() => $0.$server.onGridScroll({gi: " + gridIdentifier + ", st: this.$.table.scrollTop}))})",
                getElement());
        return grid;
    }

    /**
     * Server-side listener of grid scroll event fired from the client-side
     */
    @ClientCallable
    public void onGridScroll(JsonObject scrollEvent) {
        int gridIdentifier = (int) scrollEvent.getNumber("gi");
        int scrollTop = (int) scrollEvent.getNumber("st");

        // prevents scroll loop
        if (gridIdentifier != skipScrollEventForGridIdentifier) {
            if (gridIdentifier == 0) {
                afterGrid.getElement().executeJs("this.$.table.scrollTop=" + scrollTop);
                skipScrollEventForGridIdentifier = 1;
            } else if (gridIdentifier == 1) {
                beforeGrid.getElement().executeJs("this.$.table.scrollTop=" + scrollTop);
                skipScrollEventForGridIdentifier = 0;
            }
        } else {
            skipScrollEventForGridIdentifier = -1;
        }
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
