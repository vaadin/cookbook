package com.vaadin.recipes.nonselectablecomboboxitems;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("non-selectable-combobox-items")
@Metadata(
    howdoI = "Create non-selectable items in ComboBox",
    description = "Create a ComboBox such that some of its items cannot be selected by user.",
    sourceFiles = { "recipe/nonselectablecomboboxitems/vaadin-combo-box-item-styles.css" },
    tags = { Tag.JAVA, Tag.THEME }
)
@CssImport(
    themeFor = "vaadin-combo-box-item",
    value = "./recipe/nonselectablecomboboxitems/vaadin-combo-box-item-styles.css"
)
public class NonSelectableComboBoxItems extends Recipe {


    public NonSelectableComboBoxItems() {
        ComboBox<String> combo = new ComboBox<>();
        combo.setItems("Heading one", "Option one", "Option two",
                "Option three", "Heading two", "Option four", "Option five");

        combo.setRenderer(new ComponentRenderer<>(item -> {
            Span span = new Span(item);
            if (isHeading(item)) {
                span.getStyle().set("opacity", "0.5");
            } else {
                span.getStyle().set("padding-left", "10px");
                span.getStyle().set("pointer-events", "auto");
            }
            return span;
        }));

        combo.addValueChangeListener(e -> {
            if ((e.getValue() != null) && isHeading(e.getValue())) {
                if ((e.getOldValue() != null)) {
                    combo.setValue(e.getOldValue());
                } else {
                    combo.clear();
                }
            }
        });

        combo.getElement().setAttribute("theme", "non-selectable-items");
        combo.setLabel("Has non-selectable items:");
        add(combo);
    }

    private boolean isHeading(String item) {
        return item.startsWith("Heading");
    }
}
