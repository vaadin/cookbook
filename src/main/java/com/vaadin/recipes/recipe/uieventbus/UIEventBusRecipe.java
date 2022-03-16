package com.vaadin.recipes.recipe.uieventbus;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("ui-eventbus")
@Metadata(
    howdoI = "Communicate between components attached to one UI",
    description = "Sometime a component needs to talk to its siblings, for example a component wants to expand and allow other components to collapse. Using an event bus allows for loose coupling. The event bus scope shall be the UI, that is usually a browser window or tab."
)
public class UIEventBusRecipe extends Recipe {

    public UIEventBusRecipe() {
        for (int i = 0; i < 5; i++) {
            add(new MyButton());
        }
    }

    private class ButtonActivateEvent extends ComponentEvent<Button> {

        public ButtonActivateEvent(Button source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    private class MyButton extends Button {
        private Registration registration;

        private MyButton() {
            deactivate();
            addClickListener(
                event -> {
                    activate();
                    // Fire an event on the event bus
                    ComponentUtil.fireEvent(UI.getCurrent(), new ButtonActivateEvent(this, false));
                }
            );
        }

        private void activate() {
            this.setText("Active!");
        }

        private void deactivate() {
            this.setText("Click me");
        }

        @Override
        protected void onAttach(AttachEvent attachEvent) {
            super.onAttach(attachEvent);
            // Register to events from the event bus
            registration =
                ComponentUtil.addListener(
                    attachEvent.getUI(),
                    ButtonActivateEvent.class,
                    event -> {
                        if (event.getSource() != this) {
                            deactivate();
                        }
                    }
                );
        }

        @Override
        protected void onDetach(DetachEvent detachEvent) {
            super.onDetach(detachEvent);
            // Unregister from the event bus
            registration.remove();
        }
    }
}
