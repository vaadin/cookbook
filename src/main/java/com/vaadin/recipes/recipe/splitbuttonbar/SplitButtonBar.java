package com.vaadin.recipes.recipe.splitbuttonbar;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("split-button-bar")
@Metadata(
    howdoI = "Split a button layout into left and right groups",
    description = "Learn how to use Vaadin HorizontalLayout to group contents into left and right groups.",
    tags = { Tag.LAYOUT }
)
public class SplitButtonBar extends Recipe {

    public SplitButtonBar() {
        add(new H4("Use a wrapper layout and expand"), withWrapperLayout());
        add(new H4("Set margin-right: auto on second button"), withMarginLeft());
        add(new H4("Add a invisible element and expand it"), withSpacerElement());
        setWidthFull();
    }

    public HorizontalLayout withWrapperLayout() {
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");
        Button save = new Button("Save");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout leftGroup = new HorizontalLayout(delete);
        HorizontalLayout buttonLayout = new HorizontalLayout(leftGroup, cancel, save);
        buttonLayout.expand(leftGroup); // <--
        buttonLayout.setWidthFull();
        return buttonLayout;
    }

    public HorizontalLayout withMarginLeft() {
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");
        Button save = new Button("Save");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(delete, cancel, save);
        delete.getStyle().setMarginRight("auto"); // <--
        buttonLayout.setWidthFull();
        return buttonLayout;
    }

    public HorizontalLayout withSpacerElement() {
        Button delete = new Button("Delete");
        Div spacer = new Div();
        Button cancel = new Button("Cancel");
        Button save = new Button("Save");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(delete, spacer, cancel, save);
        buttonLayout.expand(spacer); // <--
        buttonLayout.setWidthFull();
        return buttonLayout;
    }
}
