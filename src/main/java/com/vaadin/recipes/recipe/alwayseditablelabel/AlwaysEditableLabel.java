package com.vaadin.recipes.recipe.alwayseditablelabel;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("always-editable-label")
@Metadata(
        howdoI = "Create an always-editable Label",
        description = "Create a Label component that is always in editable mode.",
        tags = { Tag.JAVA })
public class AlwaysEditableLabel extends Recipe {

    public AlwaysEditableLabel() {
        Label label = new Label("I'm an editable label ... ");
        label.getElement().setAttribute("contenteditable", true);

        add(label);
    }
}