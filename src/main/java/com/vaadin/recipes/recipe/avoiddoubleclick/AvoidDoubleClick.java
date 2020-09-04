package com.vaadin.recipes.recipe.avoiddoubleclick;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import elemental.json.JsonValue;

@Route("avoid-double-click")
@Metadata(howdoI = "Avoid double-clicks on a Button")
public class AvoidDoubleClick extends Recipe {

    public AvoidDoubleClick() {
        Button button = new Button("click me twice");
        button.getElement().addEventListener("click", e -> {
            JsonValue detail = e.getEventData().get("event.detail");
            if (detail.asNumber() > 1) {
                Notification.show("Double click, ignore me");
            } else {
                Notification.show("Single click, do something");
            }

        }).addEventData("event.detail");
        add(button);
    }
}
