package com.vaadin.recipes.recipe.gridcsvexport;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;
import org.vaadin.firitin.components.DynamicFileDownloader;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Stream;

@Route("grid-csv-export")
@Metadata(
    howdoI = "Export grid data as CSV",
    description = "Export the contents of a Vaadin Grid as CSV so your users can download and save it. Using the Java component API. ",
    tags = { Tag.GRID, Tag.CSV, Tag.DOWNLOAD }
)
public class GridCsvExport extends Recipe {

    public GridCsvExport() {
        // Setup a grid with random data
        Grid<Person> grid = new Grid<>(Person.class);
        grid.setItems(createExamplePersons(100));
        grid.setSelectionMode(SelectionMode.MULTI);

        add(grid);

        add(new DynamicFileDownloader("Download as CSV...", "persons.csv", out -> {
            Stream<Person> persons = null;
            if (grid.asMultiSelect().getValue().isEmpty()) {
                // No selected rows, write all
                persons = grid.getGenericDataView().getItems();
            } else {
                // write only selected rows
                persons = grid.asMultiSelect().getValue().stream();
            }

            PrintWriter writer = new PrintWriter(out);
            writer.println("birthDate,firstName,lastName");
            persons.forEach(p -> {
                writer.println("%s,%s,%s".formatted(
                        p.getBirthDate(),
                        p.getFirstName(),
                        p.getLastName()
                    ));
            });
            writer.close();

        }));
    }

    public static class Person {

        private String firstName, lastName;
        private LocalDate birthDate;

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
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

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, 123);
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count);
    }

}
