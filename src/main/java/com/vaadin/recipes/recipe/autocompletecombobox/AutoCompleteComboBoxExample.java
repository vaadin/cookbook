package com.vaadin.recipes.recipe.autocompletecombobox;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("autocomplete-combo-box")
@Metadata(
  howdoI = "Create an auto-complete ComboBox",
  description = "Learn how to create a combo box for which value is auto-selected if the filter text matches exactly one value.",
  sourceFiles = {"AutoCompleteComboBox.java"}
)
public class AutoCompleteComboBoxExample extends Recipe {

    public AutoCompleteComboBoxExample() {
        add(new Div("Enter text and press Tab, once filter only matches a single item."));

        var comboBox = new AutoCompleteComboBox<ItemWithText>();
        comboBox.setLabel("Auto-complete ComboBox");
        comboBox.setItems(new ItemWithText(1, "Item Root 1"), new ItemWithText(2, "Item Root 2"), new ItemWithText(3, "Item Branch 3"));
        comboBox.setItemLabelGenerator(ItemWithText::text);
        add(comboBox);

        add(new Div("Note: This auto-complete combo-box implementation supports only list data (items set with setItems(Collection<T>) or setItems(T...))."));
    }

    public record ItemWithText(int id, String text) {

    }
}