package com.vaadin.recipes.recipe.gridprobinder;

import java.time.LocalDateTime;
import java.util.Collection;

import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("grid-pro-binder")
@Metadata(howdoI = "Use Binder and validators in GridPro editing.", description = "This recipe shows how to use Binder and Validators with GridPro. The solution requires to use .custom for custom editors so that we can hook Binder. We use Notifications for errors, as compact editors in GridPro cells do not have space to show integrated error labels.", tags = {
        Tag.GRID })
// Import empty css, the actual styles are coming from GridPro via include
@CssImport(value = "./recipe/gridproconditionaledit/gridproconditionaledit.css", themeFor = "vaadin-text-field", include = "lumo-grid-pro-editor")
public class GridProBinder extends Recipe {
    private GridPro<Person> grid = new GridPro<>();

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(
                Person.class, LocalDateTime.now());
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setEmail, DataType.EMAIL);
        return generator.create(count, 1);
    }

    public GridProBinder() {
        // Add Binder
        Binder<Person> binder = new Binder<>();

        // Setup a grid with random data
        grid.setItems(createExamplePersons(100));
        grid.setSelectionMode(SelectionMode.NONE);
        // Double click selects field text which is annoying
        // grid.setEditOnClick(true);

        // Use custom editor
        TextField firstName = createTextField();

        // Bind with Validators
        binder.forField(firstName).asRequired("First name is required")
                .withValidator(new StringLengthValidator(
                        "First name(s) can be 1 - 80 characters long", 1, 80))
                // Use notification for showing validation error
                .withValidationStatusHandler(
                        handler -> showValidationError(handler,binder))
                .bind(Person::getFirstName, Person::setFirstName);

        addEditColumnWithBinder(binder, Person::getFirstName, firstName,
                "First name(s)");

        TextField lastName = createTextField();

        binder.forField(lastName)
                .withValidator(new StringLengthValidator(
                        "Last name can be 1 - 20 characters long", 1, 20))
                .withValidator(Validator.from(value -> !value.contains(" "),
                        "Last name cannot contain spaces"))
                // Use notification for showing validation error
                .withValidationStatusHandler(
                        handler -> showValidationError(handler,binder))
                .bind(Person::getLastName, Person::setLastName);

        addEditColumnWithBinder(binder, Person::getLastName, lastName,
                "Last name");

        TextField emailField = createTextField();

        binder.forField(emailField).asRequired("E-mail is required")
                .withValidator(new EmailValidator("Input valid e-mail"))
                .withValidationStatusHandler(
                        handler -> showValidationError(handler,binder))
                .bind(Person::getEmail, Person::setEmail);

        addEditColumnWithBinder(binder, Person::getEmail, emailField, "E-mail");
        grid.addCellEditStartedListener(event -> {
            // We prefer using setBean as GridPro is by nature un-buffered
            binder.setBean(event.getItem());
        });
        grid.addItemPropertyChangedListener(event -> {
            // This is required to avoid accidental copy pasting of value
            binder.setBean(null);
        });
        add(grid);
    }

    // Convenience method for adding column with given text field, etc.
    private void addEditColumnWithBinder(Binder<Person> binder,
            ValueProvider<Person, ?> valueProvider, TextField textField,
            String header) {
        grid.addEditColumn(valueProvider)
                .custom(textField, (item, newValue) -> {
                    // Intentionally NOP as value is commited by Binder as we
                    // use setBean
                }).setHeader(header);
    }

    // Convenience method for creating a new textfield
    private TextField createTextField() {
        TextField textField = new MyTextField();
        textField.setWidth("100%");
        textField.setAutoselect(false);
        textField.addThemeName("grid-pro-editor");
        return textField;
    }

    // Convenience method for showing the validation error as Notification
    private void showValidationError(BindingValidationStatus<?> handler, Binder<Person> binder) {
        if (binder.getBean() != null &&  handler.isError()) {
            Notification
                    .show("Validation: " + handler.getMessage().get(), 5000,
                            Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    // Convenience solution for missing required indicator
    public class  MyTextField extends TextField {
    
        @Override
        public void setRequiredIndicatorVisible(boolean visible) {
            super.setRequiredIndicatorVisible(visible);
            if (visible) {
                setPlaceholder("required");
            } else {
                setPlaceholder(null);
            }
        }
    }

    public static class Person {
        private String firstName, lastName;
        private String email = "";

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
