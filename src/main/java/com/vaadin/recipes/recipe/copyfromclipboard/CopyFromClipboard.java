package com.vaadin.recipes.recipe.copyfromclipboard;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("copy-from-clipboard")
@Metadata(
    howdoI = "Copy text from clipboard",
    description = "Learn how to copy any text content, like a text area, from the clipboard with a button click using Vaadin's Java component API."
)
public class CopyFromClipboard extends Recipe {

    TextArea textArea = new TextArea("Copy to");

    public CopyFromClipboard() {
        Span text = new Span("This is some example text that you can copy to your clipboard. Click the button after you copied the text and if browser asks for a permission grant it.");

        Button button = new Button("Copy from clipboard", VaadinIcon.PASTE.create());
        button.addClickListener(e -> 
            UI.getCurrent().getPage().executeJs("navigator.clipboard.readText().then(clipText => $0.$server.doPaste(clipText)) ", getElement())
        );
        add(text, textArea, button);
    }
    
    @ClientCallable
    public void doPaste(String clipText) {
        textArea.setValue(clipText);
    }
}

