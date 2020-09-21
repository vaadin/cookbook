package com.vaadin.recipes.recipe.dynamiccomboboxitembackgroundcolor;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("dynamic-combobox-item-background-color")
@Metadata(
        howdoI = "set ComboBox items' background color dynamically",
        description = "Dynamically set the background color of items in the dropdown list of a ComboBox component.",
        sourceFiles = { "recipe/dynamic-combobox-item-background-color/vaadin-combo-box-item-styles.css" },
        tags = { Tag.THEME })
@CssImport(
        themeFor = "vaadin-combo-box-item",
        value = "./recipe/dynamic-combobox-item-background-color/vaadin-combo-box-item-styles.css"
    )
public class DynamicComboboxItemBackgroundColor extends Recipe {

    public DynamicComboboxItemBackgroundColor() {
        ComboBox<String> customItemBackground = new ComboBox<>();
        customItemBackground.setItems("Good #1", "Bad #1", "Good #2", "Good #3",
                "Bad #2");

        customItemBackground.setRenderer(new ComponentRenderer<>(item -> {
            Span text = new Span(item);
            if (item.startsWith("Bad")) {
                text.setClassName("bad");
            } else if (item.startsWith("Good")) {
                text.setClassName("good");
            }
            return text;
        }));
        customItemBackground.getElement().setAttribute("theme",
                "custom-item-background");

        customItemBackground.setLabel("Custom item background");
        customItemBackground.setValue("Good #2");
        add(customItemBackground);
    }
}