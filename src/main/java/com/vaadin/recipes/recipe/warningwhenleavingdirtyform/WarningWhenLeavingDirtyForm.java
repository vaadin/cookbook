package com.vaadin.recipes.recipe.warningwhenleavingdirtyform;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("warning-when-leaving-dirty-form")
@Metadata(howdoI = "Warn user when leaving a modified form", description = "Learn how to ask users for confirmation before leaving a dirty form in a Vaadin.", tags = {
    Tag.USABILITY })
public class WarningWhenLeavingDirtyForm extends Recipe implements BeforeLeaveObserver {
  private TextField firstName = new TextField("First Name");
  private TextField lastName = new TextField("Last Name");
  private Binder<Person> binder = new Binder<>(Person.class);

  public WarningWhenLeavingDirtyForm() {
    add(firstName, lastName);
    binder.bindInstanceFields(this);
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent event) {
    if (binder.hasChanges()) {
      final ContinueNavigationAction action = event.postpone();
      final ConfirmDialog dialog = new ConfirmDialog();
      dialog.setText("Are you sure you want to leave? You have unsaved data.");
      dialog.setConfirmButton("Stay", e -> dialog.close());
      dialog.setCancelButton("Leave", e -> action.proceed());
      dialog.setCancelable(true);
      dialog.open();
    }
  }

  public static class Person {
    private String firstName, lastName;

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
  }
}
