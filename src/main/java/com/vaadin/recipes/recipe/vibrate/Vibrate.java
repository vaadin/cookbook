package com.vaadin.recipes.recipe.vibrate;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("vibrate")
@Metadata(howdoI = "Make a mobile phone vibrate", description = "Integrate with the browser's Vibration API to make the device vibrate (Android only).", tags = {
        Tag.FLOW })
public class Vibrate extends Recipe {

    public Vibrate() {

        boolean android = VaadinSession.getCurrent().getBrowser().isAndroid();
        if (!android) {
            add(new Paragraph(
                    "Note: The Vibration API is only supported on Android at the time when this example was implemented."));
        }

        NumberField timeField = new NumberField("Vibrate time");
        timeField.setSuffixComponent(new Span("ms"));
        timeField.setValue(200d);

        Button vibrateButton = new Button("Vibrate me", event -> {
            getElement().executeJs("navigator.vibrate($0)", timeField.getValue());
        });

        Button patternButton = new Button("Vibrate with a pattern", event -> {
            getElement().executeJs("navigator.vibrate([200, 50, 100, 50, 50])");
        });

        add(timeField, vibrateButton, patternButton);
    }

}
