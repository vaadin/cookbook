package com.vaadin.recipes.recipe.userlocalegridnumbers;

import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("user-locale-grid-numbers")
@Metadata(howdoI = "Show numbers in a grid according to the user's locale", sourceFiles = "ApplicationI18NProvider.java")
public class UserLocaleGridNumbers extends Recipe {
    public UserLocaleGridNumbers() {
        Grid<Double> grid = new Grid<>();
        grid.setItems(-1000000000d, -12.234, -0.000003, 0d, 0.000003, 12.234, 1000000000d);

        Locale userLocale = UI.getCurrent().getLocale();

        add(new Text("User locale: " + userLocale));

        grid.addColumn(value -> value).setHeader("Unformatted number");
        grid.addColumn(new NumberRenderer<>(value -> value, NumberFormat.getInstance(userLocale)))
                .setHeader("Formatted number");

        add(grid);
    }
}
