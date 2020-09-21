package com.vaadin.recipes.recipe.allowusertopickacolor;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("allow-user-to-pick-a-color")
@Metadata(
    howdoI = "Allow the user to pick a color",
    description = "Learn how to implement a color picker input field in a Vaadin Java view. Define the server-side API for an HTML color input field and synchronize values.",
    sourceFiles = "ColorPicker.java"
)
public class AllowUserToPickAColor extends Recipe {

    public AllowUserToPickAColor() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.addValueChangeListener(
            e -> {
                Notification.show("Thank you for picking " + colorPicker.getValue(), 5000, Position.MIDDLE);
            }
        );
        add(colorPicker);
    }
}
