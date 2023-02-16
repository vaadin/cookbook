package com.vaadin.recipes.recipe.gridcsvexport;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-csv-export")
@Metadata(
    howdoI = "Export grid data as CSV",
    description = "Export the contents of a Vaadin Grid as CSV so your users can download and save it. Using the Java component API. ",
    tags = { Tag.GRID, Tag.CSV }
)
public class GridCsvExport extends Recipe {

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

    private GridListDataView<Person> dataView;

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, 123);
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count);
    }

    public GridCsvExport() {
        // Setup a grid with random data
        Grid<Person> grid = new Grid<>(Person.class);
        dataView = grid.setItems(createExamplePersons(100));
        grid.setSelectionMode(SelectionMode.MULTI);

        add(grid);

        TextArea resultField = new TextArea();
        resultField.setWidth("100%");
        Button exportButton = new Button(
            "Export as CSV",
            e -> {
                this.export(grid, resultField);
            }
        );
        add(exportButton);
        add(resultField);
    }

    private void export(Grid<Person> grid, TextArea result) {
        // Fetch all data from the grid in the current sorted order
        Stream<Person> persons = null;
        Set<Person> selection = grid.asMultiSelect().getValue();
        if (selection != null && selection.size() > 0) {
            persons = selection.stream();
        } else {
            persons = dataView.getItems();
        }

        StringWriter output = new StringWriter();
        StatefulBeanToCsv<Person> writer = new StatefulBeanToCsvBuilder<Person>(output).build();
        try {
            writer.write(persons);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            output.write("An error occured during writing: " + e.getMessage());
        }

        result.setValue(output.toString());
    }

}
