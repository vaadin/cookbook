package com.vaadin.recipes.recipe.reducecomboboxitemsseparation;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("reduce-combobox-items-separation")
@Metadata(
        howdoI = "reduce spacing between ComboBox items",
        description = "Create a CombBox component with little separation between items.",
        tags = { Tag.THEME })
@CssImport(
        themeFor = "vaadin-combo-box-item",
        value = "./recipe/reduce-combobox-items-separation/vaadin-combo-box-item-styles.css"
    )
public class ReduceComboBoxItemsSeparation extends Recipe {

    public ReduceComboBoxItemsSeparation() {
        ComboBox<String> customSpacing = new ComboBox<>();

        customSpacing.setItems("Item #1", "Item #2", "Item #3", "Item #4");
        customSpacing.getElement().setAttribute("theme", "custom-spacing");

        customSpacing.setLabel("Reduced space between items");

        add(customSpacing);
    }
}