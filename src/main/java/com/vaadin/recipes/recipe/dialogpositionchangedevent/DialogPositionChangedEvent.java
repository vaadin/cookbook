package com.vaadin.recipes.recipe.dialogpositionchangedevent;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("dialog-position-changed-event")
@Metadata(
    howdoI = "Receive an event, when the user drags a dialog around",
    description = "This sample shows how to workaround the missing 'position-changed' event using native JS events." +
            " A custom event is fired, when the user repositioned the dialog using its dragging feature.",
    tags = { Tag.USABILITY }
)
public class DialogPositionChangedEvent extends Recipe {

    public DialogPositionChangedEvent() {
        CustomDialog dialog = new CustomDialog();
        dialog.add(new Span("Some Content"));

        dialog.setDraggable(true);

        dialog.addPositionChangedListener(event -> {
            Notification.show("Moved to: " + event.getX() + ", " + event.getY());
        });

        add(new Button("Open dialog", event -> dialog.open()));
    }

    public static class CustomDialog extends Dialog {

        private int x;
        private int y;

        public CustomDialog() {
            registerClientSidePositionChangedListener();

            addPositionChangedListener(event -> {
                if (isDraggable() && event.isFromClient()) {
                    x = event.getX();
                    y = event.getY();
                }
            });
        }

        public Registration addPositionChangedListener(ComponentEventListener<PositionChangedEvent> listener) {
            return addListener(PositionChangedEvent.class, listener);
        }

        private void registerClientSidePositionChangedListener() {
            // Registers a pseudo position changed listener for the dialog
            // You can extend it with a "mousemove" listener to get position-changed
            // events everytime the mouse is moved around. But beware, this will lead
            // to a massive amount of events fired.
            getElement().executeJs("" +
                    "let pxFromStyle = (str) => str.substring(0, str.length - 2);" +
                    "" +
                    "let vaadinOverlay = this.$.overlay;" +
                    "let overlay = vaadinOverlay.$.overlay;" +
                    "console.warn(this, overlay);" +
                    "overlay.addEventListener('mousedown', e => {" +
                    "	let oRect = overlay.getBoundingClientRect();" +
                    "	this.__tmp_position_currentX = oRect.x;" +
                    "	this.__tmp_position_currentY = oRect.y;" +
                    "});" +
                    "overlay.addEventListener('mouseup', e => {" +
                    "	let oRect = overlay.getBoundingClientRect();" +
                    "   " +
                    "   if(this.__tmp_position_currentX != oRect.x || this.__tmp_position_currentY != oRect.y) {" +
                    "       this.dispatchEvent(new CustomEvent('position-changed', {" +
                    "				detail: {" +
                    "					x: oRect.x," +
                    "					y: oRect.y" +
                    "				}" +
                    "			}));" +
                    "	}" +
                    "	delete this.__tmp_position_currentX;" +
                    "	delete this.__tmp_position_currentY;" +
                    "});");
        }
    }

    @DomEvent("position-changed")
    public static class PositionChangedEvent extends ComponentEvent<CustomDialog> {

        private final int x;
        private final int y;

        public PositionChangedEvent(CustomDialog source,
                                    boolean fromClient,
                                    @EventData("event.detail.x") int x,
                                    @EventData("event.detail.y") int y) {
            super(source, fromClient);
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

}
