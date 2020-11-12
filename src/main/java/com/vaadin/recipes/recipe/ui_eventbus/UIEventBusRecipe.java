package com.vaadin.recipes.recipe.ui_eventbus;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.util.Random;

@Route("ui-eventbus")
@Metadata(
        howdoI = "Communicate between components attached to one UI",
        description = "Sometime a component needs to talk to its siblings, for example a component wants to expand and allow other components to collapse. Using an event bus allows for loose coupling. The event bus scope shall be the UI, that is usually a browser window or tab."
)
public class UIEventBusRecipe extends Recipe {
    public UIEventBusRecipe(){
        for (int i=0; i < 2 + new Random().nextInt(30); i++){
            add(new MyButton());
        }
    }

    private class ButtonExpandEvent extends ComponentEvent<Button>{
        public ButtonExpandEvent(Button source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    private class MyButton extends Button {
        private Registration reg;
        private MyButton(){
            simulateCollapse();
            addClickListener( event -> {
                simulateExpand();
                ComponentUtil.fireEvent(UI.getCurrent(), new ButtonExpandEvent(this, false));
            });
        }

        private void simulateExpand(){
            this.setText("Expanded!");
        }

        private void simulateCollapse(){
            this.setText("Collapsed!");
        }

        @Override
        protected void onAttach(AttachEvent attachEvent) {
            super.onAttach(attachEvent);
            reg = ComponentUtil.addListener(UI.getCurrent(), ButtonExpandEvent.class, event -> {
                if (event.getSource() != this){
                    simulateCollapse();
                }});
        }

        @Override
        protected void onDetach(DetachEvent detachEvent) {
            super.onDetach(detachEvent);
            reg.remove();
        }
    }
}
