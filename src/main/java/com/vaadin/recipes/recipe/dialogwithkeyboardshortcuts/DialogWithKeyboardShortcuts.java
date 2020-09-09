package com.vaadin.recipes.recipe.dialogwithkeyboardshortcuts;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("dialog-with-keyboard-shortcuts")
@Metadata(
    howdoI = "Show a dialog where Enter submits and Esc closes",
    description = "Improve UX with keyboard shortcuts for confirming or dismissing a dialog. Code example using the Vaadin Java component API.",
    tags = { Tag.KEYBOARD }
)
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

        okButton.addClickListener(
            event -> {
                Notification.show("Accepted");
                dialog.close();
            }
        );
        okButton.addClickShortcut(Key.ENTER);

        // Prevent click shortcut of the OK button from also triggering when
        // another button is focused
        ShortcutRegistration shortcutRegistration = Shortcuts
            .addShortcutListener(buttons, () -> {}, Key.ENTER)
            .listenOn(buttons);
        shortcutRegistration.setEventPropagationAllowed(false);
        shortcutRegistration.setBrowserDefaultAllowed(true);

        dialog.open();
    }
}
