package com.vaadin.recipes.recipe.userlocalegridnumbers;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("user-locale-grid-numbers")
@Metadata(howdoI = "Show numbers in a grid according to the user's locale", description = "Learn how to format numbers in a Vaadin Grid column based on user locales.", sourceFiles = "ApplicationI18NProvider.java", tags = {
    Tag.GRID })
public class UserLocaleGridNumbers extends Recipe {
  private final Grid<Double> grid = new Grid<>();

  public UserLocaleGridNumbers() {
    grid.setItems(-1000000000d, -12.234, -0.000003, 0d, 0.000003, 12.234, 1000000000d);

    Locale userLocale = UI.getCurrent().getLocale();

    add(new Text("User locale: " + userLocale));

    ComboBox<Locale> overrideSelector = new ComboBox<>("Override user locale", getAvailableLocales());
    overrideSelector.setItemLabelGenerator(Locale::getDisplayName);
    overrideSelector.addValueChangeListener(event -> useLocale(event.getValue()));
    add(overrideSelector);

    useLocale(userLocale);

    add(grid);
  }

  private static Locale[] getAvailableLocales() {
    Locale[] availableLocales = Locale.getAvailableLocales();
    Arrays.sort(availableLocales, Comparator.comparing(Locale::getDisplayName));
    return availableLocales;
  }

  private void useLocale(Locale locale) {
    grid.removeAllColumns();
    grid.addColumn(value -> value).setHeader("Unformatted number");
    grid.addColumn(new NumberRenderer<>(value -> value, NumberFormat.getInstance(locale)))
        .setHeader("Formatted number");
  }
}
