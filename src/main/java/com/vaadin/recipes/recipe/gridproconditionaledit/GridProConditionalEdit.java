package com.vaadin.recipes.recipe.gridproconditionaledit;

import java.util.Collection;

import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-pro-conditional-edit")
@Metadata(
    howdoI = "Enable conditional editing of the cells in GridPro and style compact editor field.",
    description = "It is a common requirement to have conditional editing of cells based of various conditions. You may for example have a property in the bean depending on which it makes sense to not to allow input of the other. Say I have example below of date being needed only if subscribed is true. Thus I want to disable or hide the editor component. Alternative cases could be access right related conditions.",
    tags = { Tag.GRID }
)
// Import empty css, the actual styles are coming from GridPro via include
@CssImport(value="./recipe/gridproconditionaledit/gridproconditionaledit.css", themeFor="vaadin-email-field", include="lumo-grid-pro-editor")
public class GridProConditionalEdit extends Recipe {
    private GridPro<Person> grid = new GridPro<>();

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(
                Person.class, 123);
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        return generator.create(count);
    }

    public GridProConditionalEdit() {
        // Setup a grid with random data
        grid.setItems(createExamplePersons(100));
        grid.setSelectionMode(SelectionMode.NONE);

        grid.addEditColumn(Person::getFirstName)
                .text((item, newValue) -> item.setFirstName(newValue))
                .setHeader("First name");

        grid.addEditColumn(Person::getLastName)
                .text((item, newValue) -> item.setFirstName(newValue))
                .setHeader("Last name");

        grid.addEditColumn(Person::isSubscriber)
                .checkbox((item, newValue) -> item.setSubscriber(newValue))
                .setResizable(true).setHeader("Subscriber");

        // Use custom editor
        EmailField emailField = new EmailField();
        emailField.setWidth("100%");
        emailField.addThemeName("grid-pro-editor");
        grid.addEditColumn(Person::getEmail)
                .custom(emailField, (item, newValue) -> {
                    item.setEmail(newValue);
                }).setHeader("E-mail ");
        // Use edit started listener to set the field conditionally enabled
        grid.addCellEditStartedListener(event -> {
            if (!event.getItem().isSubscriber()) {
                emailField.setReadOnly(true);
            } else {
                emailField.setReadOnly(false);
            }
        });

        add(grid);
    }

    public static class Person {
        private String firstName, lastName;
        private String email = "";
        private boolean subscriber = false;

        public boolean isSubscriber() {
            return subscriber;
        }

        public void setSubscriber(boolean subscriber) {
            this.subscriber = subscriber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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
