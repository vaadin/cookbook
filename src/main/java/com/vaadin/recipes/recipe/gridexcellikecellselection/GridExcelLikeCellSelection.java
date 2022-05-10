package com.vaadin.recipes.recipe.gridexcellikecellselection;

import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = "grid-excel-like-cell-selection")
@JavaScript("./recipe/gridexcellikecellselection/gridexcellikecellselection.js")
@Metadata(
    howdoI = "Excel like cell selection in a grid",
    description = "Code example for selecting cells in a grid similar to Excel. Server side gets informed about " +
    "selected items and columns.",
    sourceFiles = { "recipe/gridexcellikecellselection/gridexcellikecellselection.js" },
    tags = { Tag.GRID }
)
public class GridExcelLikeCellSelection extends Recipe {

    private final Grid<SamplePerson> grid;
    private final VerticalLayout results;

    public GridExcelLikeCellSelection() {
        grid = new Grid<>(SamplePerson.class, false);

        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);

        grid.setItems(SamplePerson.ITEMS);

        initExcelLikeCellSelection();

        results = new VerticalLayout();

        HorizontalLayout layout = new HorizontalLayout(grid, results);
        layout.setSizeFull();
        grid.setWidthFull();

        add(layout);
    }

    /**
     * Initiates the excel like cell selection
     */
    private void initExcelLikeCellSelection() {
        // Applies the necessary client side event logic to the given grid
        grid.getElement().executeJs("window.Vaadin.Flow.custom.initGridExcelCellSelection(this)");

        // Adds a event listener, that receives the client side fired event to read the selected items and columns.
        // A subclass of Grid could also use a custom @DomEvent annotated event class, e.g. ExcelLikeSelectedCellsEvent
        grid
            .getElement()
            .addEventListener(
                "excel-like-selected-cells",
                event -> {
                    JsonObject eventData = event.getEventData();
                    results.removeAll();
                    results.add(new H3("Selection results"));

                    VerticalLayout items = new VerticalLayout();

                    items.add(new H5("Selected Items"));

                    JsonValue value = eventData.get("event.detail.selectedKeys");
                    if (value instanceof JsonArray) {
                        JsonArray array = (JsonArray) value;
                        for (int i = 0; i < array.length(); i++) {
                            String key = array.getString(i);
                            SamplePerson item = grid.getDataCommunicator().getKeyMapper().get(key);
                            if (item != null) {
                                items.add(new Span(item.toString()));
                            }
                        }
                    }

                    VerticalLayout columns = new VerticalLayout();
                    columns.add(new H5("Selected Columns"));
                    value = eventData.get("event.detail.selectedColumns");
                    if (value instanceof JsonArray) {
                        JsonArray array = (JsonArray) value;
                        for (int i = 0; i < array.length(); i++) {
                            String key = array.getString(i);
                            columns.add(new Span(key));
                        }
                    }

                    results.add(items, columns);
                }
            )
            .addEventData("event.detail.selectedKeys")
            .addEventData("event.detail.selectedColumns");
    }

    public static class SamplePerson {

        public static final List<SamplePerson> ITEMS;

        static {
            ITEMS = new ArrayList<>(10);
            for (int i = 0; i < 200; i++) {
                ITEMS.add(new SamplePerson(i, "First Name " + i, "Last Name " + i, "person" + i + "@example.com"));
            }
        }

        private final int id;
        private final String firstName;
        private final String lastName;
        private final String email;

        public SamplePerson(int id, String firstName, String lastName, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "Person " + id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SamplePerson that = (SamplePerson) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
