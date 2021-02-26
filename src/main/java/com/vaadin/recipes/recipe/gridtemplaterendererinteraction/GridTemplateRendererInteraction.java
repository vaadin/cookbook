package com.vaadin.recipes.recipe.gridtemplaterendererinteraction;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.time.LocalDate;
import java.util.List;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@Route("grid-template-renderer-interaction")
@Metadata(
    howdoI = "Add the template renderer in grid.",
    description = "This recipe shows how to use TemplateRenderer in Grid, populate data in the element and handling events."
)
public class GridTemplateRendererInteraction extends Recipe {

    public GridTemplateRendererInteraction() {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, 423524l);

        generator.setData(Person::setId, DataType.ID);
        generator.setData(Person::setName, DataType.FULL_NAME);
        generator.setData(Person::setEmail, DataType.EMAIL);
        generator.setData(Person::setBirthday, DataType.DATE_OF_BIRTH);
        List<Person> personList = generator.create(100);

        Grid<Person> personGrid = new Grid<>();

        personGrid.addColumn(Person::getName).setHeader("Name");
        personGrid.addColumn(Person::getEmail).setHeader("E-Mail");
        personGrid.addColumn(Person::getBirthday).setHeader("Birthday");

        personGrid
            .addColumn(
                TemplateRenderer
                    .<Person>of(
                        "<vaadin-button id='button-example-[[item.id]]' title='you can click me once!' on-click='onClick'>click me</vaadin-button>"
                    )
                    .withProperty("id", Person::getId)
                    .withEventHandler(
                        "onClick",
                        person -> {
                            Notification.show(String.format("Clicked on %s", person.getName()));
                            getElement()
                                .executeJs(
                                    "this.getRootNode().getElementById($0).setAttribute('disabled','disabled')",
                                    String.format("button-example-%d", person.getId())
                                );
                        }
                    )
            )
            .setHeader("Show Notification");

        personGrid.setItems(personList);

        this.add(personGrid);
    }

    public static class Person {
        private Long id;
        private String name;
        private String email;
        private LocalDate birthday;

        public Person() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setId(Integer id) {
            this.id = id.longValue();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }
    }
}
