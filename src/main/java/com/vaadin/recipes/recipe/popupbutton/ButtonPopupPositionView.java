package com.vaadin.recipes.recipe.popupbutton;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

/**
 * @since Vaadin 24.5
 */
@Route("button-popup")
@Metadata(
    howdoI = "Show a popup next to a button when it is clicked",
    description = "Code snippet for showing a popover next to a button for context-sensitive help or actions. Uses the Vaadin Java API. "
)
public class ButtonPopupPositionView extends Recipe {

    public ButtonPopupPositionView() {
        var button = new Button("Button + Popover");

        var popover = new Popover(new Span("Popover opened"));
        popover.setTarget(button);
        popover.addThemeVariants(PopoverVariant.ARROW);
        popover.setPosition(PopoverPosition.END);
        add(button);
    }
}
