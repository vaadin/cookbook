package com.vaadin.recipes.recipe.gridrecord;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Route("grid-record")
@Metadata(
    howdoI = "Display a Java record in a Vaadin Grid",
    description = "Record types need separate handling when adding columns to the Vaadin Grid",
    tags = { Tag.GRID, Tag.JAVA }
)
public class GridRecord extends Recipe {

    private final List<Person> personList = List.of(
        new Person("Donald Duck", LocalDate.of(1952, 06, 25)),
        new Person("Micky Mouse", LocalDate.of(1654, 12, 5)),
        new Person("Goofy", LocalDate.of(2020, 1, 14))
    );

    public GridRecord() {
        // would not work alone
        // Grid<Person> personGrid = new Grid<>(Person.class);

        // would throw an exception java.lang.IllegalArgumentException: Can't resolve
        // property name 'name' from
        // 'Property set for bean com.vaadin.recipes.recipe.gridrecord.GridRecord$Person'
        // personGrid.addColumn("name");

        // here are possible options
        Grid<Person> personGrid = new Grid<>();
        personGrid.addColumn(Person::name).setHeader("Name");
        personGrid.addColumn(person -> person.birthday).setHeader("Birthday");
        personGrid
            .addColumn(
                LitRenderer
                    .<Person>of("<span>${item.value}</span>")
                    .withProperty("value", person -> Period.between(person.birthday, LocalDate.now()).getYears())
            )
            .setHeader("Age");

        personGrid.setItems(personList);
        add(personGrid);
    }

    public record Person(String name, LocalDate birthday) {}
}
