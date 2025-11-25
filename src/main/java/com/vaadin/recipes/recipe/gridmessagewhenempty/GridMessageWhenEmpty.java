package com.vaadin.recipes.recipe.gridmessagewhenempty;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Route("grid-message-when-empty")
@Metadata(
    howdoI = "Show a meaningful message instead of an empty Grid",
    description = "Provide a meaningful message when a Grid would show an empty data set potentially confusing a user with its appearance."
    , tags = { Tag.GRID, Tag.USABILITY }
)
public class GridMessageWhenEmpty extends Recipe {

    // This example uses Vaadin 24.5+ API
    // For earlier versions, see the old example:
    // https://github.com/vaadin/cookbook/blob/8a04538930b4649cbb9be92af92e1cf73188b308/src/main/java/com/vaadin/recipes/recipe/gridmessagewhenempty/GridMessageWhenEmpty.java

    public GridMessageWhenEmpty() {
        addClassName("grid-message-example");

        var control = new ItemsControl();

        var grid = new Grid<>(ExampleBean.class);
        grid.setItems(control.getDataProvider());

        grid.setEmptyStateText("No items found"); // <--

        add(grid, control);
    }

    public static class ItemsControl extends HorizontalLayout {
        private final List<ExampleBean> items = /* Collections.synchronizedList */(new ArrayList<>());
        private final ListDataProvider<ExampleBean> dataProvider = new ListDataProvider<>(items);

        public ItemsControl() {
            Button removeButton = new Button("-");
            removeButton.addClickListener(evt -> removeItem());

            Button addButton = new Button("+");
            addButton.addClickListener(evt -> addItem());

            var text = new Div();
            text.getStyle().setFontSize("25px");

            DataProviderListener<ExampleBean> eventListener = (e -> text.setText("Items: " + items.size()));
            dataProvider.addDataProviderListener(eventListener);
            setAlignItems(Alignment.END);
            setWidthFull();

            eventListener.onDataChange(null); // initial run

            addToStart(removeButton, addButton);
            addToEnd(text);
        }

        private void addItem() {
            ExampleBean item = new ExampleBean(
                RandomStringUtils.randomAscii(15),
                RandomStringUtils.randomAscii(15),
                RandomStringUtils.randomAscii(15),
                RandomStringUtils.randomAscii(15)
            );

            final int index;
            if (items.isEmpty()) {
                index = 0;
            } else {
                index = new Random().nextInt(items.size());
            }

            items.add(index, item);
            dataProvider.refreshAll();
        }

        private void removeItem() {
            if (!items.isEmpty()) {
                items.remove(new Random().nextInt(items.size()));
            }
            dataProvider.refreshAll();
        }

        public ListDataProvider<ExampleBean> getDataProvider() {
            return dataProvider;
        }
    }

    public static class ExampleBean {
        public final String lastName;
        public final String firstName;
        public final String street;
        public final String city;

        public ExampleBean(String lastName, String firstName, String street, String city) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.street = street;
            this.city = city;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }
    }
}
