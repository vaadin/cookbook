package com.vaadin.recipes.recipe.splitlayoutsplitterposition;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import org.apache.commons.lang3.StringUtils;

@Route("split-layout-splitter-position")
@Metadata(
        howdoI = "Extend the splitter drag end event to obtain the new splitter position.",
        description = "By default, the split layout does not provide any 'user friendly' way to " +
                "obtain the splitter position, when it has been changed by the user. This snippet shows " +
                "how to use the element api to receive the raw client side position and calculate it into " +
                "a percentage value usable on the server side. Based on " +
                "https://github.com/vaadin/flow-components/issues/1517.",
        tags = {Tag.LAYOUT, Tag.USABILITY}
)
public class SplitLayoutSplitterPosition extends Recipe {

    // these may change in future
    public static final String EVENT_KEY_PRIMARY_CHILD = "event.target._primaryChild.style.flexBasis";
    public static final String EVENT_KEY_SECONDARY_CHILD = "event.target._secondaryChild.style.flexBasis";

    public SplitLayoutSplitterPosition() {
        SplitLayout splitLayout = new SplitLayout(new Span("Left"), new Span("Right"));
        splitLayout.setSplitterPosition(30);
        addAndExpand(splitLayout);

        addSplitterDraggedListener(splitLayout, value -> {
            Notification.show("New splitter position: " + value);
        });
    }

    /**
     * Registers an event listener for the given layout to inform the given consumer about any splitter position
     * changes. The consumer will receive the new splitter position as a percentage value (i. e. a value
     * between 0 and 100). Also updates the serverside splitter position.
     *
     * @see SplitLayout#setSplitterPosition(double)
     *
     * @param layout split layout to add the event listener to
     * @param consumer consumer to be called on an event
     * @return registration to remove the event listener
     */
    public static Registration addSplitterDraggedListener(SplitLayout layout, SerializableConsumer<Double> consumer) {
        Element element = layout.getElement();

        return element.addEventListener("splitter-dragend", event -> {
                    String primaryWidth = event.getEventData().get(EVENT_KEY_PRIMARY_CHILD).asText();
                    String secondaryWidth = event.getEventData().get(EVENT_KEY_SECONDARY_CHILD).asText();

                    double splitPosition;
                    if (StringUtils.isBlank(primaryWidth)) {
                        // When not setting a server side split position, the event may provide empty values.
                        // This is a fallback to provide the default value in that case.
                        splitPosition = 50;

                    } else if (primaryWidth.endsWith("px")) {
                        // This is the "default" branch. When moving the splitter, the client side sets
                        // pixel values (at the time of writing this recipe).
                        double pWidth = Double.parseDouble(primaryWidth.replace("px", ""));
                        double sWidth = Double.parseDouble(secondaryWidth.replace("px", ""));

                        splitPosition = (pWidth * 100) / (pWidth + sWidth);

                        // Optional
                        // You may round it a bit to prevent huge decimal amounts. Here we simply round min and max.
                        if (splitPosition < 0.0001) {
                            splitPosition = 0;
                        } else if (splitPosition > 99.9999) {
                            splitPosition = 100;
                        }

                    } else if (primaryWidth.endsWith("%")) {
                        splitPosition = Double.parseDouble(primaryWidth.replace("%", ""));


                    } else {
                        throw new IllegalArgumentException("Given width values are not supported: " + primaryWidth + " / " + secondaryWidth);
                    }

                    // Recommended to keep client and server in sync. You may move this to your own  event listener,
                    // if necessary.
                    layout.setSplitterPosition(splitPosition);

                    consumer.accept(splitPosition);
                })

                .addEventData(EVENT_KEY_PRIMARY_CHILD)
                .addEventData(EVENT_KEY_SECONDARY_CHILD);
    }
}
