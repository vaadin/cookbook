package com.vaadin.recipes.recipe.customlayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("custom-layout")
@Metadata(howdoI = "Emulate CustomLayout from previous Vaadin versions", description = "A simple layout for adding components into specific locations in a static HTML template.", tags = {
        Tag.LAYOUT }, sourceFiles = "CustomLayout.java")
public class CustomLayoutExample extends Recipe {
    private final CustomLayout customLayout;

    public CustomLayoutExample() {
        customLayout = new CustomLayout(
                "<div><p>Location 1: <span location='one'></span></p><p>Location 2: <span location='two'></span></p></div>");

        add(customLayout, new Hr(), createLocationControl("Location 1", "one"),
                createLocationControl("Location 2", "two"));
    }

    private Component createLocationControl(String title, String name) {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Nothing", "Text", "Button");
        radioButtonGroup.setValue("Nothing");

        radioButtonGroup.addValueChangeListener(event -> {
            switch (event.getValue()) {
            case "Nothing":
                customLayout.remove(name);
                break;
            case "Text":
                customLayout.add(new Span("Text"), name);
                break;
            case "Button":
                customLayout.add(new Button("Button"), name);
                break;
            }
        });

        return new VerticalLayout(new Text("Contents of " + title), radioButtonGroup);
    }
}