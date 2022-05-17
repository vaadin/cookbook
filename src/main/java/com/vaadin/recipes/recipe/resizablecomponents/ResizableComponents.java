package com.vaadin.recipes.recipe.resizablecomponents;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("resizable-components")
@Metadata(
        howdoI = "Enable users to resize components",
        description = "Provide users with the ability to resize any component. " +
                "You can enable users to resize the component's height, its width, or both the height and width.",
        tags = { Tag.JAVA, Tag.USABILITY }
)

public class ResizableComponents extends Recipe {

    ResizableComponents() {

        // The TextArea is used just as an example. The same approach should work with other components
        TextArea resizableVertically = new TextArea("Vertically resizable");
        resizableVertically.setWidth("300px");
        resizableVertically.getStyle().set("resize", "vertical");
        resizableVertically.getStyle().set("overflow", "auto");
        add(resizableVertically);

        TextArea resizableHorizontally = new TextArea("Horizontally resizable");
        resizableHorizontally.setWidth("300px");
        resizableHorizontally.getStyle().set("resize", "horizontal");
        resizableHorizontally.getStyle().set("overflow", "auto");
        add(resizableHorizontally);

        TextArea resizableInBothDirections = new TextArea("Vertically & Horizontally resizable");
        resizableInBothDirections.setWidth("300px");
        resizableInBothDirections.getStyle().set("resize", "both");
        resizableInBothDirections.getStyle().set("overflow", "auto");
        add(resizableInBothDirections);

    }
}
