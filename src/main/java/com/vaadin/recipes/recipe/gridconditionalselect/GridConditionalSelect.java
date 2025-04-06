package com.vaadin.recipes.recipe.gridconditionalselect;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-conditional-select")
@Metadata(howdoI = "Create multiselect Grid with conditional selection", description = "Create multiselect Grid with conditional selection. We use two techniques. Class name generator is used to style checkboxes disabled and disable pointer events. User can still toggle selection with space key, thus we add server side check as well to bounce selections we do not want.", tags = {
        Tag.GRID }, sourceFiles = {
                "recipe/gridconditionalselect/gridconditionalselect.css" })
@CssImport(themeFor = "vaadin-grid", value = "./recipe/gridconditionalselect/gridconditionalselect.css")
public class GridConditionalSelect extends Recipe {
    private Grid<Person> grid = new Grid<>(Person.class);

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(
                Person.class, LocalDateTime.now());
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count, 123);
    }

    public GridConditionalSelect() {
        // Setup a grid with random data
        grid.setItems(createExamplePersons(100));
        grid.setSelectionMode(SelectionMode.MULTI);

        grid.addSelectionListener(event -> {
            if (event.isFromClient()) {
                event.getAllSelectedItems().forEach(item -> {
                    // Revert selection if item cannot be selected
                    if (canSelect(item)) {
                        grid.deselect(item);
                    }
                });
            }
        });
        // Generate "no-select" class name for rows that cannot be selected
        grid.setClassNameGenerator(
                item -> canSelect(item) ? "no-select"
                        : null);

        Checkbox checkbox = new Checkbox("Enable selection by clicking row");

        // For convenience you could set also click listener to do the selection
        grid.addItemClickListener(event -> {
            Person item = event.getItem();
            if (!checkbox.getValue() || canSelect(item)) {
                return;
            }
            if (grid.getSelectedItems().contains(item)) {
                grid.deselect(item);
            } else {
                grid.select(event.getItem());
            }
        });

        add(checkbox, grid);
    }

    private boolean canSelect(Person item) {
        return item.getBirthDate().getYear() >= 2000;
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
