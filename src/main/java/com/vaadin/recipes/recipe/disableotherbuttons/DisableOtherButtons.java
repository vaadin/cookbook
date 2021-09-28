package com.vaadin.recipes.recipe.disableotherbuttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("disable-other-buttons")
@Metadata(howdoI = "Disable other buttons immediately when one button is clicked",
        description = "Same effect as disableOnClick, but applied to multiple buttons instead of only one.",
        tags = { Tag.FLOW, Tag.FORM, Tag.USABILITY })
public class DisableOtherButtons extends Recipe {
    public DisableOtherButtons() {
        Button actualButton = new Button("Main button");
        Button otherButton1 = new Button("Some other button");
        Button otherButton2 = new Button("Yet another button");

        // JS listener that immediately disables the other buttons
        actualButton.getElement().executeJs(
                "this.addEventListener('click', function() {$0.disabled = true; $1.disabled = true})", otherButton1,
                otherButton2);

        actualButton.setDisableOnClick(true);

        actualButton.addClickListener(event -> {
            try {
                // Delay so that buttons remain disabled for a while
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Notification.show("Done processing");

            actualButton.setEnabled(true);
            // Buttons disabled through JS are enabled again through JS
            otherButton1.getElement().executeJs("this.disabled = false");
            otherButton2.getElement().executeJs("this.disabled = false");
        });

        add(actualButton, otherButton1, otherButton2);
    }
}