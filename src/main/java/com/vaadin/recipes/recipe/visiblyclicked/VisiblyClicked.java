package com.vaadin.recipes.recipe.visiblyclicked;

import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.dom.Element;

@Route("visibly-clicked-button")
@Metadata(
  howdoI = "Client-side visual proof of server-side activation",
  description = "Actions that result in programmatic activation of a Vaadin button don't trigger any visual change to that button. This recipe shows how to make this work."
)
public class VisiblyClicked extends Recipe {

  public VisiblyClicked() {

    class PButton extends Button {
        PButton(String arg) {
            super(arg);
        }

        // this method takes care of clickInClient()
        @Override
        public void clickInClient() {
            programmaticClickAnimation();
            super.clickInClient();
        }

        // this method takes care of click() and shortcutkey presses
        @Override
        public void fireEvent(ComponentEvent componentEvent) {
            if (componentEvent instanceof ClickEvent) {
                ClickEvent clickEvent = (ClickEvent)componentEvent;
                if (!clickEvent.isFromClient()) {
                    programmaticClickAnimation();
                }
            }
            super.fireEvent(componentEvent);
        }
        private void programmaticClickAnimation() {
            this.getElement().executeJs("this.classList.remove('click-animation');" +
                    "setTimeout(() => this.classList.add('click-animation'), 100)");
        }
    }
    UI.getCurrent().getElement().appendChild(new Element("style").setText(".click-animation {animation:remoteclick .6s }"));
    UI.getCurrent().getElement().appendChild(new Element("style").setText("@keyframes remoteclick {from {background-color:#BBBBBB} to {background-color:#EEEEEE}}"));

    Button buttonMain = new PButton("Click me or press Shift-F2)" );
    buttonMain.addClickShortcut(Key.F2, KeyModifier.SHIFT);
    buttonMain.addClickListener(e->{Notification.show("F2 Button Clicked");});

    Button buttonSecondary = new Button("Click me to programmatically click the other button");
    buttonSecondary.addClickListener(e->{buttonMain.click();});

    VerticalLayout stage = new VerticalLayout();
    stage.add(buttonMain, buttonSecondary);
    this.add(stage);

  }

}
