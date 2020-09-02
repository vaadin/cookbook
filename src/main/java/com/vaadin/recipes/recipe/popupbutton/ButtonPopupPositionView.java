package com.vaadin.recipes.recipe.popupbutton;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("button-popup")
@Metadata(howdoI = "show a popup next to a button when it is clicked")
public class ButtonPopupPositionView extends Recipe {

    private Button button = new Button("open dialog", this::openDialog);
    private Dialog dialog = new Dialog(new Span("dialog opened"));

    public ButtonPopupPositionView() {
        add(button);
    }

    private void openDialog(ClickEvent<Button> buttonClickEvent) {
        dialog.open();
        /* position the dialog next to the button on the left */
        dialog.getElement().executeJs(
            "$0.$.overlay.$.overlay.style['align-self']='flex-start';" +
                "$0.$.overlay.$.overlay.style['position']='absolute';" +
                "$0.$.overlay.$.overlay.style['top']= ($1.getBoundingClientRect().top - $1.getBoundingClientRect().height )+ 'px';" +
                "$0.$.overlay.$.overlay.style['left']= ($1.getBoundingClientRect().left + $1.getBoundingClientRect().width) + 'px'"
            , dialog, button);
        dialog.open();
    }

}
