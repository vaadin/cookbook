package com.vaadin.recipes.recipe.dialogwithkeyboardshortcuts;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("dialog-with-keyboard-shortcuts")
@Metadata(howdoI = "Show a dialog where Enter submits and Esc closes")
public class DialogWithKeyboardShortcuts extends Recipe {
    public DialogWithKeyboardShortcuts() {
        add(new Button("Show dialog", event -> showDialog()));
    }

    private void showDialog() {
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        HorizontalLayout buttons = new HorizontalLayout(okButton, cancelButton);

        Dialog dialog = new Dialog(new Span("Dialog content goes here"), buttons);

        dialog.setCloseOnEsc(true);
        cancelButton.addClickListener(event -> dialog.close());

        okButton.addClickListener(event -> {
            Notification.show("Accepted");
            dialog.close();
        });
        okButton.addClickShortcut(Key.ENTER);

        dialog.open();
    }
}
