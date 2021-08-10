package com.vaadin.recipes.recipe.gridarrowselection;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.time.LocalDate;
import java.util.Collection;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@Route("grid-arrow-selection")
@Metadata(
    howdoI = "Select Grid rows automatically using up/down arrow keys",
    description = "Allow Grid rows to be selected using up/down arrow keys",
    tags = { Tag.GRID }
)
@CssImport(themeFor = "vaadin-grid", value = "./recipe/grid-arrow-selection/vaadin-grid-styles.css")
public class GridArrowSelection extends Recipe {

    public GridArrowSelection() {
        Grid<Person> grid = new Grid<>(Person.class);
        grid.setItems(createExamplePersons(100));

        // A 'keyup' event is used (1) to do the selection on the client-side
        // when arrow keys are used, and (2) to inform the server of the
        // selection.
        grid.addAttachListener(
            a -> {
                grid
                    .getElement()
                    .executeJs(
                        "this.addEventListener('keyup', function(e) {" +
                        // ignore Space as it can still be used to
                        // (de)select items
                        "if (e.keyCode == 32) return;" +
                        "let grid = $0;" +
                        "if (grid.selectedItems){" +
                        "grid.activeItem=this.getEventContext(e).item;" +
                        "grid.selectedItems=[this.getEventContext(e).item];" +
                        "grid.$server.select(this.getEventContext(e).item.key);}} )",
                        grid.getElement()
                    );
            }
        );

        Span selectedItem = new Span("Selected item:");
        grid.addSelectionListener(
            e -> {
                String txt = "Selected item:";
                Person item = e.getFirstSelectedItem().orElse(null);
                if (item != null) {
                    txt += " " + item.getFirstName() + " " + item.getLastName();
                }
                selectedItem.setText(txt);
            }
        );
        add(grid, selectedItem);
    }

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, 123);
        generator.setData(Person::setFirstName, DataType.FIRST_NAME);
        generator.setData(Person::setLastName, DataType.LAST_NAME);
        generator.setData(Person::setBirthDate, DataType.DATE_OF_BIRTH);
        return generator.create(count);
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
