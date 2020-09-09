package com.vaadin.recipes.recipe.copytoclipboard;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("copy-to-clipboard")
@Metadata(howdoI = "Copy text to clipboard", description = "Learn how to copy any text content, like a text area, to the clipboard with a button click using Vaadin's Java component API.", sourceFiles = {
    "recipe/copytoclipboard/copytoclipboard.js" })
@JsModule("./recipe/copytoclipboard/copytoclipboard.js")
public class CopyToClipboard extends Recipe {

  public CopyToClipboard() {
    TextArea textArea = new TextArea("Example text");
    textArea.setValue("This is some example text that you can copy to your clipboard.");

    Button button = new Button("Copy to clipboard", VaadinIcon.COPY.create());
    button
        .addClickListener(e -> UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", textArea.getValue()));
    add(textArea, button);
  }
}
