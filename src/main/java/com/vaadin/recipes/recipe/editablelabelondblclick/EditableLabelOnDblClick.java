package com.vaadin.recipes.recipe.editablelabelondblclick;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("editable-label-on-dblclick")
@Metadata(
        howdoI = "create an editable Label when double-clicked", 
        description = "Create a Label that can turn into a text field for editing on double-click.",
        tags = { Tag.JAVA })
public class EditableLabelOnDblClick extends Recipe {

    public EditableLabelOnDblClick() {
        String initialContent = "Double-click me to edit ...";
        Label label = new Label(initialContent);
        TextField textField = new TextField();
        textField.setValue(initialContent);

        textField.setVisible(false);

        label.getElement().addEventListener("dblclick", e -> {
            label.setVisible(false);
            textField.setVisible(true);
            textField.focus();
        });

        textField.addValueChangeListener(e -> {
            textField.setVisible(false);
            label.setVisible(true);
            String newValue = textField.getValue();
            if (newValue.isEmpty()) {
                remove(label, textField);
            } else {
                label.setText(newValue);
            }
        });

        textField.addBlurListener(e -> {
            textField.setVisible(false);
            label.setVisible(true);
        });

        add(label, textField);
    }
}
