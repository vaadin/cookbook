package com.vaadin.recipes.recipe.gridcsvexport;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@Route("grid-csv-export")
@Metadata(howdoI = "Export grid data as CSV", tags = { Tag.GRID })
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
        grid.setItems(createExamplePersons(100));

        add(grid);

        TextArea resultField = new TextArea();
        resultField.setWidth("100%");
        Button exportButton = new Button("Export as CSV", e -> {
            this.export(grid, resultField);
        });
        add(exportButton);
        add(resultField);

    }

    private void export(Grid<Person> grid, TextArea result) {
        // Fetch all data from the grid in the current sorted order
        Stream<Person> persons = ((DataProvider<Person, String>) grid.getDataProvider()).fetch(createQuery(grid));

        StringWriter output = new StringWriter();
        StatefulBeanToCsv<Person> writer = new StatefulBeanToCsvBuilder<Person>(output).build();
        try {
            writer.write(persons);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            output.write("An error occured during writing: " + e.getMessage());
        }

        result.setValue(output.toString());
    }

    private Query<Person, String> createQuery(Grid<Person> grid) {
        List<GridSortOrder<Person>> gridSort = grid.getSortOrder();
        List<QuerySortOrder> sortOrder = gridSort.stream()
                .map(order -> order.getSorted().getSortOrder(order.getDirection())).flatMap(orders -> orders)
                .collect(Collectors.toList());

        BinaryOperator<SerializableComparator<Person>> operator = (comparator1, comparator2) -> {
            return comparator1.thenComparing(comparator2)::compare;
        };
        SerializableComparator<Person> inMemorySorter = gridSort.stream()
                .map(order -> order.getSorted().getComparator(order.getDirection())).reduce(operator).orElse(null);

        return new Query<Person, String>(0, Integer.MAX_VALUE, sortOrder, inMemorySorter, null);

    }
}