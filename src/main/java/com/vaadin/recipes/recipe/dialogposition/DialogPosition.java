package com.vaadin.recipes.recipe.dialogposition;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import org.apache.commons.lang3.StringUtils;

@Route("dialog-position")
@Metadata(howdoI = "A simple way to keep the position of a vaadin dialog and set it after reopening")
public class DialogPosition extends Recipe {

    private static final MyDialog.Position INITIAL_POSITION = new MyDialog.Position("0px", "0px");
    private final MyDialog myDialog = new MyDialog();
    private final Button resetPosition = new Button("reset Position", buttonClickEvent -> myDialog.setPosition(INITIAL_POSITION));

    private MyDialog.Position lastPosition ;

    public DialogPosition() {

        myDialog.add(new VerticalLayout(
            new H4("My Dialog"),
            new Span("Keep left mouse clicked and move dialog around. After closing the dialog and you can reopen it at the same location again."),
            new Button("close", this::closeDialog)));
        myDialog.setDraggable(true);
        myDialog.setModal(false);
        myDialog.addOpenedChangeListener(event -> resetPosition.setEnabled(event.isOpened()));

        add(new Button("open dialog", this::openDialog));
        resetPosition.setEnabled(false);
        add(resetPosition);
    }

    private void openDialog(ClickEvent<Button> buttonClickEvent) {
        myDialog.open();
        myDialog.setPosition(lastPosition != null ? lastPosition : INITIAL_POSITION);
    }

    private void closeDialog(ClickEvent<Button> buttonClickEvent) {
        myDialog.getPosition(position -> {
            lastPosition = position;
            myDialog.close();
        });;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        myDialog.close();
    }

    public static class MyDialog extends Dialog {

        private static final String SET_PROPERTY_IN_OVERLAY_JS = "this.$.overlay.$.overlay.style[$0]=$1";

        public void setPosition(Position position) {
            enablePositioning(true);
            getElement().executeJs(SET_PROPERTY_IN_OVERLAY_JS, "left", position.getLeft());
            getElement().executeJs(SET_PROPERTY_IN_OVERLAY_JS, "top", position.getTop());
        }

        private void enablePositioning(boolean positioningEnabled) {
            getElement().executeJs(SET_PROPERTY_IN_OVERLAY_JS, "align-self", positioningEnabled ? "flex-start" : "unset");
            getElement().executeJs(SET_PROPERTY_IN_OVERLAY_JS, "position", positioningEnabled ? "absolute" : "relative");
        }

        public void getPosition(SerializableConsumer<Position> consumer) {
            getElement().executeJs("return [" +
                        "this.$.overlay.$.overlay.style['top'], this.$.overlay.$.overlay.style['left']" +
                    "]")
                    .then(String.class, s -> {
                        String[] split = StringUtils.split(s, ',');
                        if (split.length == 2 && split[0] != null && split[1] != null) {
                            Position position = new Position(split[0], split[1]);
                            consumer.accept(position);
                        }
                    });
        }

        public static class Position {
            private String top;
            private String left;

            public Position(String top, String left) {
                this.top = top;
                this.left = left;
            }

            public String getTop() {
                return top;
            }

            public void setTop(String top) {
                this.top = top;
            }

            public String getLeft() {
                return left;
            }

            public void setLeft(String left) {
                this.left = left;
            }
        }
    }
}
