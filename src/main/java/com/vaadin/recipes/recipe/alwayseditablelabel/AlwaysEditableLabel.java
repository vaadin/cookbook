package com.vaadin.recipes.recipe.alwayseditablelabel;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("always-editable-label")
@Metadata(
        howdoI = "Create an always-editable label",
        description = "Create a component with a text label that is always in editable mode.",
        tags = { Tag.JAVA })
public class AlwaysEditableLabel extends Recipe {

    public AlwaysEditableLabel() {
        Span label = new Span("I'm an editable label ... ");
        label.getElement().setAttribute("contenteditable", true);

        label.getElement().addEventListener("input",
                e -> Notification.show("Value changed: " + e.getEventData().getString("event.target.innerHTML")))
                .addEventData("event.target.innerHTML");

        add(label);
    }
}