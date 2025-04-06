package com.vaadin.recipes.recipe.gridfrozenselectioncolumn;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Route(value = "grid-frozen-selection-column")
@Metadata(
        howdoI = "Freeze a grid's selection (checkbox) column",
        description = "The Java API has a new easy-to-use way of freezing a grid's selection (checkbox)",
        tags = { Tag.GRID }
)
public class GridFrozenSelectionColumn extends Recipe {

    private Grid<Person> grid = new Grid<>(Person.class);

    public GridFrozenSelectionColumn() {
        grid.setColumns("birthDate", "firstName", "lastName");
        grid.setItems(createExamplePersons(100));
        ((GridMultiSelectionModel<?>) grid
          .setSelectionMode(Grid.SelectionMode.MULTI))
          .setSelectionColumnFrozen(true);
        // Overflow for demo purposes
        for (Grid.Column<Person> column : grid.getColumns()) {
            column.setAutoWidth(true);
        }
        grid.setWidth(320, Unit.PIXELS);

        add(grid);
    }

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, LocalDateTime.now());
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count, 123);
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
}
