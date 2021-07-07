package com.vaadin.recipes.recipe.gridscrolllistener;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

// Fixme: This recipe depends on internal outerscroller API that has changed and does not work
// @Route("grid-scroll-listener")
// @Metadata(
//         howdoI = "Listen grid scroll events",
//         description = "Code example for showing how to add a scroll listener to a Grid. Server is notified with grid index and new scrollTop value.",
//         tags = {Tag.GRID}
// )
public class GridScrollListener extends Recipe {

    private final Grid<Patient> grid;
    private final Span scrollTopValueSpan;

    public GridScrollListener() {
        grid = new Grid<>(Patient.class);
        grid.setItems(createPatientData());
        grid.setColumns("name", "bodyTemp", "pulse");

        scrollTopValueSpan = new Span("Grid 0 scrollTop=0");
        add(scrollTopValueSpan, withClientsideScrollListener(grid, 0));
    }

    /**
     * Add client-side scroll listener to grid
     */
    private Grid<Patient> withClientsideScrollListener(Grid<Patient> grid, int gridIdentifier) {
        grid.getElement().executeJs(
                "this.$.outerscroller.addEventListener('scroll', (scrollEvent) => " +
                        "{requestAnimationFrame(() => $0.$server.onGridScroll({gi: " + gridIdentifier + ", st: this.$.table.scrollTop}))},true)",
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
        scrollTopValueSpan.setText("Grid "+gridIdentifier+" scrollTop="+scrollTop);
    }

    /**
     * Items for the first grid
     */
    private List<Patient> createPatientData() {
        List<Patient> peopleBefore = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            Patient person1 = new Patient(i, "Person " + i, 36 + Math.random(), 70 + (int) (Math.random() * 10));
            peopleBefore.add(person1);
        }
        return peopleBefore;
    }

    /**
     * Example DTO
     */
    public static class Patient {
        private int id;
        private String name;
        private double bodyTemp;
        private int pulse;

        public Patient(int id, String name, double bodyTemp, int pulse) {
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
