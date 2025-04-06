package com.vaadin.recipes.recipe.gridmultiselectnoselectcolumn;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-multiselect-no-selectcolumn")
@Metadata(
    howdoI = "Create multiselect Grid without selection column",
    description = "Create multiselect Grid without visible selection column by hiding it with JavaScript call and using explicit item click listener to do the selection.",
    tags = { Tag.GRID }
)
public class GridMultiselectNoSelectColumn extends Recipe {
    private Grid<Person> grid = new Grid<>(Person.class);

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, LocalDateTime.now());
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count, 123);
    }

    public GridMultiselectNoSelectColumn() {
        // Setup a grid with random data
        grid.setItems(createExamplePersons(100));
        grid.setSelectionMode(SelectionMode.MULTI);

        grid.addItemClickListener(
            event -> {
                Person item = event.getItem();
                if (grid.getSelectedItems().contains(item)) {
                    grid.deselect(item);
                } else {
                    grid.select(event.getItem());
                }
            }
        );

        add(grid);
    }

    public void onAttach(AttachEvent event) {
        // Hide the selection column with JS
        grid
            .getElement()
            .executeJs("this.getElementsByTagName(\"vaadin-grid-flow-selection-column\")[0].hidden = true;");
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
