package com.vaadin.recipes.recipe.logininitcredentials;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("login-overlay-initialize-credentials")
@Metadata(
  howdoI = "Initialize the username and password values in a LoginOverlay",
  description = "Learn how to display an initial value in the Login Overlay fields. This is not recommended for real use cases but could be handy for demos or guest access.", 
  tags = { Tag.JAVA }
)
public class LoginOverlayInitializeCredentials extends Recipe {

  private LoginOverlay loginOverlay = new LoginOverlay();

  public LoginOverlayInitializeCredentials() {
    // Initialize i18n with instructions how to close the overlay
    LoginI18n i18n = LoginI18n.createDefault();
    LoginI18n.Header header = new LoginI18n.Header();
    header.setTitle("Log in");
    header.setDescription("Click the Log in button to close");
    i18n.setHeader(header);
    loginOverlay.setI18n(i18n);
    loginOverlay.addLoginListener(listener -> {
      loginOverlay.close();
    });
    // Add button that opens the login overlay and initializes the username and password values
    add(new Button("Open Login Overlay", this::initializeCredentialsAndOpenLogin));
  }


  private void initializeCredentialsAndOpenLogin(ClickEvent<Button> buttonClickEvent) {
    // Set the initial values for the username and password fields.
    loginOverlay.getElement().executeJs("this.$.vaadinLoginForm.$.vaadinLoginUsername.value = $0;", "guestUsername");
    loginOverlay.getElement().executeJs("this.$.vaadinLoginForm.$.vaadinLoginPassword.value = $0;", "guestPassword");
    // Open the login overlay
    loginOverlay.setOpened(true);
  }

}
