package com.vaadin.recipes.recipe.gridmessagewhenempty;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

@Route("grid-message-when-empty")
@Metadata(
    howdoI = "Show a meaningful message instead of an empty Grid",
    description = "Provide a meaningful message when a Grid would show an empty data set potentially confusing a user with its appearance.",
    sourceFiles = "recipe/gridmessagewhenempty/grid-message-when-empty.css"
    tags = { Tag.GRID }
)
@CssImport("./recipe/gridmessagewhenempty/grid-message-when-empty.css")
public class GridMessageWhenEmpty extends Recipe {

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

    public static class ItemsControl extends HorizontalLayout {
        private final List<ExampleBean> items = /* Collections.synchronizedList */(new ArrayList<>());
        private final ListDataProvider<ExampleBean> dataProvider = new ListDataProvider<>(items);

        public ItemsControl() {
            Button removeButton = new Button("-");
            removeButton.addClickListener(evt -> removeItem());

            Button addButton = new Button("+");
            addButton.addClickListener(evt -> addItem());

            Label text = new Label();
            DataProviderListener<ExampleBean> eventListener = (e -> text.setText("Items: " + items.size()));
            dataProvider.addDataProviderListener(eventListener);
            text.getStyle().set("font-size", "25px");
            text.getStyle().set("margin-left", "auto"); // https://vaadin.com/learn/training/v14-layouting 18:45

            setAlignItems(Alignment.END);
            setWidthFull();

            eventListener.onDataChange(null); // initial run

            add(removeButton, addButton, text);
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

    public GridMessageWhenEmpty() {
        addClassName("grid-message-example");

        ItemsControl control = new ItemsControl();

        Grid<ExampleBean> grid = new Grid<>(ExampleBean.class);
        grid.addClassName("grid");
        ListDataProvider<ExampleBean> dataProvider = control.getDataProvider();
        grid.setItems(dataProvider);

        /*
         * This is the meat of this recipe: a Div with the same size of the Grid, both
         * with 'absolute' position within a 'relative' position Div as a root to staple
         * them together.
         *
         * See the CSS file for implementation details.
         */
        Div gridRoot = new Div();
        gridRoot.addClassName("grid-root");

        Div warning = new Div(new Text("*** There is no data to display ***"));
        warning.addClassName("warning");

        gridRoot.add(grid, warning);

        add(new VerticalLayout(gridRoot), control);

        DataProviderListener<ExampleBean> listener =
            (
                e -> {
                    if (dataProvider.size(new Query<>()) == 0) {
                        warning.removeClassName("hidden");
                    } else {
                        warning.addClassName("hidden");
                    }
                }
            );

        dataProvider.addDataProviderListener(listener);

        // Initial run of the listener, as there is no event fired for the initial state
        // of the data set that might be empty or not.
        listener.onDataChange(null);
    }
}
