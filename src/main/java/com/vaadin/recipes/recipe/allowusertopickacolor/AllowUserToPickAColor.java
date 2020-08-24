package com.vaadin.recipes.recipe.allowusertopickacolor;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.SourceFiles;

@Route("allow-user-to-pick-a-color")
@Metadata(howdoI = "Allow the user to pick a color")
@SourceFiles("ColorPicker.java")
public class AllowUserToPickAColor extends VerticalLayout {

    public AllowUserToPickAColor() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.addValueChangeListener(e -> {
            Notification.show("Thank you for picking " + colorPicker.getValue(), 5000, Position.MIDDLE);
        });
        add(colorPicker);
    }

}