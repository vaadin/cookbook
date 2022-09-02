package com.vaadin.recipes.recipe.logininitcredentials;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("login-form-initialize-credentials")
@Metadata(
  howdoI = "Initialize the username and password values in a LoginForm",
  description = "Learn how to display an initial value in the Login Form fields. This is not recommended for real use cases but could be handy for demos or guest access.", 
  tags = { Tag.JAVA }
)
public class LoginFormInitializeCredentials extends Recipe {

  public LoginFormInitializeCredentials() {
    LoginForm loginForm = new LoginForm();
    // Set the initial values for the username and password fields.
    loginForm.getElement().executeJs("this.$.vaadinLoginUsername.value = $0;", "guestUsername");
    loginForm.getElement().executeJs("this.$.vaadinLoginPassword.value = $0;", "guestPassword");

    add(loginForm);
  }

}
