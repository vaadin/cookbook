package com.vaadin.recipes.recipe.gridexternaltemplaterenderer;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.time.LocalDate;
import java.util.*;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@Route("grid-external-template-renderer")
@JsModule("./recipe/grid-external-template/renderer.js")
@Metadata(
    howdoI = "Add an external template renderer in Grid",
    description = "This recipe shows how to use TemplateRenderer from an external source in Grid and populate data in the element.",
    sourceFiles = "recipe/grid-external-template/renderer.js",
    tags = { Tag.GRID, Tag.PERFORMANCE, Tag.TYPE_SCRIPT, Tag.FLOW }
)
public class GridExternalTemplateRenderer extends Recipe {

    private final Map<Long, Boolean> disabledMap;

    public GridExternalTemplateRenderer() {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(Person.class, 423524l);

        generator.setData(Person::setId, DataType.ID);
        generator.setData(Person::setName, DataType.FULL_NAME);
        generator.setData(Person::setEmail, DataType.EMAIL);
        generator.setData(Person::setBirthday, DataType.DATE_OF_BIRTH);
        List<Person> personList = generator.create(100);
        personList.stream().forEach(this::generatePhoneNumberList);
        disabledMap = new HashMap<>();

        Grid<Person> personGrid = new Grid<>();

        personGrid.addColumn(Person::getName).setHeader("Name");
        personGrid.addColumn(Person::getEmail).setHeader("E-Mail");
        personGrid.addColumn(Person::getBirthday).setHeader("Birthday");

        personGrid
            .addColumn(
                LitRenderer
                    .<Person>of("${myListRenderer(item.phones)}")
                    .withProperty("phones", person -> person.getPhoneNumbers())
            )
            .setHeader("Phonenumbers");

        personGrid.setItems(personList);

        this.add(personGrid);
    }

    private void generatePhoneNumberList(Person person) {
        var phoneNoSet = new HashSet<String>();
        var generator = new Random();

        for (int i = 0; i < generator.nextInt(1, 5); i++) {
            var phoneNumber = String.format(
                "%d-%d-%d",
                generator.nextInt(100, 999),
                generator.nextInt(100, 999),
                generator.nextInt(1000, 9999)
            );
            phoneNoSet.add(phoneNumber);
        }

        person.setPhoneNumbers(phoneNoSet);
    }

    public static class Person {

        private Long id;
        private String name;
        private String email;
        private LocalDate birthday;
        private Set<String> phoneNumbers;

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

        public Set<String> getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(Set<String> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }
    }
}
