package com.vaadin.recipes.recipe.gridhyperlinks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-hyperlink")
@Metadata(
        howdoI = "Put hyperlinks into Vaadin Grid cells",
        description = "Learn how to display links in Grid cells. Uses LitRenderer to create the hyperlink for improved performance.",
        sourceFiles = {"recipe/gridhyperlink/button-hyperlink.css"},
        tags = {Tag.GRID, Tag.PERFORMANCE})
@CssImport(themeFor = "vaadin-button", value = "recipe/gridhyperlink/button-hyperlink.css")
public class GridHyperlink extends Recipe {

    // Lit template html which creates hyperlink on 'Id' column.
    // 'tertiary-inline' and 'small' are Vaadin build-in variants, while 'link' is
    // our own custom variant for buttons.
    private static final String LIT_TEMPLATE_HTML = """
            <vaadin-button title="Go to ..."
                           @click="${clickHandler}"
                           theme="tertiary-inline small link">
                ${item.id}
            </vaadin-button>""";

    public GridHyperlink() {
        Grid<Person> grid = new Grid<>(Person.class, false);
        grid.setItems(createExamplePersons(100));
        grid.addColumn(
                LitRenderer.<Person>of(LIT_TEMPLATE_HTML)
                        .withProperty("id", Person::getId)
                        .withFunction("clickHandler", person -> {
                            // Do whatever. For example navigate to other view.
                            Notification.show("Link was clicked for Person #" + person.getId());
                        }))
                .setHeader("Id").setSortable(true);
        grid.addColumn(Person::getFirstName).setHeader("First name").setSortable(true);
        grid.addColumn(Person::getLastName).setHeader("Last name").setSortable(true);

        add(grid);
    }

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, LocalDateTime.now());
        generator.setData(Person::setId, DataType.ID);
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count, 1);
    }

    public static class Person {

        private Integer id;
        private String firstName, lastName;
        private LocalDate birthDate;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

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
