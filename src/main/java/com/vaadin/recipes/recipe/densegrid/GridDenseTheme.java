package com.vaadin.recipes.recipe.densegrid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-dense-theme")
@Metadata(
    howdoI = "Show a Vaadin Grid with compact row styling",
    description = "Use CSS to create a Grid with compact rows to show dense data. ",
    sourceFiles = {
        "recipe/densegrid/densegrid.css" },
    tags = { Tag.GRID, Tag.THEME }
)
@CssImport(themeFor = "vaadin-grid", value = "recipe/densegrid/densegrid.css")
public class GridDenseTheme extends Recipe {
    private Grid<Person> grid = new Grid<>(Person.class);

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, LocalDateTime.now());
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count, 123);
    }

    public GridDenseTheme() {
        // Setup a grid with random data
        grid.setItems(createExamplePersons(100));
        grid.setThemeName("dense");

        add(grid);
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
