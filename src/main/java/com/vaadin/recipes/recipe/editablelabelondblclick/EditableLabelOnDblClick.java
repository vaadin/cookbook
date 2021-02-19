package com.vaadin.recipes.recipe.editablelabelondblclick;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("editable-label-on-dblclick")
@Metadata(
    howdoI = "create an editable label when double-clicked",
    description = "Create a text label that can turn into a text field for editing on double-click.",
    tags = {}
)
public class EditableLabelOnDblClick extends Recipe {

    public EditableLabelOnDblClick() {
        String initialContent = "Double-click me to edit ...";
        Span label = new Span(initialContent);
        TextField textField = new TextField();
        textField.setValue(initialContent);

        textField.setVisible(false);

        label
            .getElement()
            .addEventListener(
                "dblclick",
                e -> {
                    label.setVisible(false);
                    textField.setVisible(true);
                    textField.focus();
                }
            );

        textField.addValueChangeListener(
            e -> {
                textField.setVisible(false);
                label.setVisible(true);
                String newValue = textField.getValue();
                if (newValue.isEmpty()) {
                    remove(label, textField);
                } else {
                    label.setText(newValue);
                }
            }
        );

        textField.addBlurListener(
            e -> {
                textField.setVisible(false);
                label.setVisible(true);
            }
        );

        add(label, textField);
    }
}
