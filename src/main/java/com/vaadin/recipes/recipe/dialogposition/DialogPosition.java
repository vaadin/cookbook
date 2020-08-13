package com.vaadin.recipes.recipe.dialogposition;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Route("dialog-position")
@Metadata(howdoI = "A simple way to SET and GET the position of a vaadin dialog")
public class DialogPosition extends Recipe {

    public DialogPosition() {
        MyDialog myDialog = new MyDialog();
        myDialog.add(new H4("My Dialog"), new Span("hold mouse click to move dialog. Click outside or press ESC to close it."));
        myDialog.setDraggable(true);
        myDialog.setModal(false);
        myDialog.setCloseOnEsc(true);
        myDialog.setCloseOnOutsideClick(true);

        TextField top = new TextField("top");
        TextField left = new TextField("left");

        add(new HorizontalLayout(top, left,
                new Button("set Position", buttonClickEvent -> myDialog.setPosition(new MyDialog.Position(String.format("%s", top.getValue()), String.format("%s", left.getValue())))),
                new Button("get Position", buttonClickEvent -> myDialog.getPosition(position -> {
                    top.setValue(position.getTop());
                    left.setValue(position.getLeft());
                }))));

        add(new Button("open dialog", buttonClickEvent -> myDialog.open()));
    }

    public static class MyDialog extends Dialog {

        private static final String SET_PROPERTY_IN_OVERLAY_JS = "this.$.overlay.$.overlay.style[$0]=$1";
        public static final String RETURN_PROPERTY_FROM_OVERLAY_JS = "return this.$.overlay.$.overlay.style[$0]";

        public void getXPosition(SerializableConsumer<String> consumer) {
            getElement().executeJs(RETURN_PROPERTY_FROM_OVERLAY_JS, "top").then(String.class, consumer);
        }

        public void setXPosition(String xPosition) {
            getElement().executeJs(SET_PROPERTY_IN_OVERLAY_JS, "top", xPosition);
        }

        public void getYPosition(SerializableConsumer<String> consumer) {
            getElement().executeJs(RETURN_PROPERTY_FROM_OVERLAY_JS, "left").then(String.class, consumer);
        }

        public void setYPosition(String yPosition) {
            getElement().executeJs(SET_PROPERTY_IN_OVERLAY_JS, "left", yPosition);
        }

        public void setPosition(Position position) {
            setYPosition(position.getLeft());
            setXPosition(position.getTop());
        }

        public void getPosition(SerializableConsumer<Position> consumer) {
            getElement().executeJs("return [" +
                    "this.$.overlay.$.overlay.style['top'], this.$.overlay.$.overlay.style['left']" +
                    "]")
                    .then(String.class, s -> {
                        String[] split = StringUtils.split(s, ',');
                        Objects.nonNull(split);
                        Position position = new Position(split[0], split[1]);
                        consumer.accept(position);
                    });
        }

        public static class Position {
            private String top;
            private String left;

            public Position() {
            }

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
