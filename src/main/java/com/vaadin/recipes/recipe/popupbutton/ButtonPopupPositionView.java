package com.vaadin.recipes.recipe.popupbutton;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("button-popup")
@Metadata(
    howdoI = "Show a popup next to a button when it is clicked",
    description = "Code snippet for showing a popover / dialog next to a button for context-sensitive help or actions. Uses the Vaadin Java API. "
)
public class ButtonPopupPositionView extends Recipe {

    public ButtonPopupPositionView() {
        add(createModernApproach());
        add(createOldApproach());
    }

    private Button createModernApproach() {
        var button = new Button("Button + Popover (Modern / V24.5+)");

        var popover = new Popover(new Span("Popover opened"));
        popover.setTarget(button);
        popover.addThemeVariants(PopoverVariant.ARROW);
        popover.setPosition(PopoverPosition.END);

        return button;
    }

    private Button createOldApproach() {
        var dialog = new Dialog(new Span("Dialog opened"));
        return new Button("Button + Dialog (pre V24.5)", event -> openOldApproachDialog(event, dialog));
    }

    private void openOldApproachDialog(ClickEvent<Button> buttonClickEvent, Dialog dialog) {
        dialog.open();
        /* position the dialog next to the button on the left */
        dialog
            .getElement()
            .executeJs(
                "$0.$.overlay.$.overlay.style['align-self']='flex-start';" +
                "$0.$.overlay.$.overlay.style['position']='absolute';" +
                "$0.$.overlay.$.overlay.style['top']= ($1.getBoundingClientRect().top - $1.getBoundingClientRect().height )+ 'px';" +
                "$0.$.overlay.$.overlay.style['left']= ($1.getBoundingClientRect().left + $1.getBoundingClientRect().width) + 'px'",
                dialog,
                buttonClickEvent.getSource() // button
            );
        dialog.open();
    }
}
