package com.vaadin.recipes.recipe.griddatabaseerror;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.recipes.recipe.griddatabaseerror.PersonService.DatabaseException;

@Route("grid-database-error")
@Metadata(howdoI = "Handle database error in Grid lazy call back", description = "Various errors can happen with database or backend system while doing lazy call back. This recipe shows how to handle the case gracefully. ", sourceFiles = {
        "Person.java", "PersonService.java" }, tags = { Tag.GRID })
public class HandleDataBaseErrorInGrid extends Recipe {

    final PersonService service = new PersonService();

    public HandleDataBaseErrorInGrid() {
        setWidth("100%");

        final Grid<Person> grid = new Grid<>();
        grid.setWidth("100%");
        add(grid);

        // Create lazy DataProvider using the PersonService
        GridLazyDataView<Person> dataView = grid.setItems(q -> {
            // Use try - catch to get potential database error
            try {
                return service.fetchPersons(q.getOffset(), q.getLimit());
            } catch (DatabaseException e) {
                List<Person> persons = new ArrayList<>();
                // Feed Grid with dummy data to avoid client side exception
                for (int i = 0; i < q.getLimit(); i++) {
                    Person person = new Person();
                    persons.add(person);
                }
                Notification
                        .show("Error loading data, scroll to retry!", 2000,
                                Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return persons.stream();
            }
        });
        dataView.setItemCountEstimate(10000);

        grid.addColumn(Person::getName).setHeader("Name").setKey("name");
        grid.addColumn(Person::getEmail).setHeader("Email").setKey("email");
        grid.addColumn(Person::getAge).setHeader("Age").setKey("age");
        grid.addColumn(new LocalDateRenderer<>(Person::getBirthday))
                .setHeader("Birthday").setKey("birthday");

    }
}
