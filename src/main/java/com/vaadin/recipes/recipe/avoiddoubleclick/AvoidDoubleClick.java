package com.vaadin.recipes.recipe.avoiddoubleclick;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.flow.dom.DomEvent;

@Route("avoid-double-click")
@Metadata(
    howdoI = "Avoid double-clicks on a Button",
    description = "Prevent accidental double submits of forms and other double clicks on buttons with this Vaadin Java code snippet.",
    tags = { Tag.USABILITY }
)
public class AvoidDoubleClick extends Recipe {

    public AvoidDoubleClick() {
        Button button = new Button("this button has accidental double-click protection");
        button
            .getElement()
            .addEventListener(
                "click",
                e -> {
                    double detail = e.getEventData().get("event.detail").asDouble();
                    if (detail > 1) {
                        // double click, ignore
                    } else {
                        Notification.show("Single click, do something");
                    }
                }
            )
            .addEventData("event.detail");
        add(button);
    }
}
