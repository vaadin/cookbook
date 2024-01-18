package com.vaadin.recipes.recipe.localstorage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("localstorage")
@Metadata(
    howdoI = "Use the browser local storage to store and retrieve data",
    description = "Values stored in the local storage are stored in the browser and will be available on all consequent visits until they are removed. They are not tied to the server session so they will remain even after the session has ended.",
    tags = {}
)
public class LocalStorage extends Recipe {

    private static final String NAME_KEY = "name";
    private Div stored = new Div();

    public LocalStorage() {
        // Latest Vaadin versions include built-in WebStorage support.
        // With older versions, use the web storage helper (with exactly
        // the same API) from
        // https://vaadin.com/directory/component/flow-viritin

        TextField name = new TextField("Your name");

        Button store = new Button(
            "Save in local storage",
            e -> {
                WebStorage.setItem(NAME_KEY, name.getValue());
                showStoredValue();
            }
        );
        Button clear = new Button(
            "Clear local storage",
            e -> {
                WebStorage.removeItem(NAME_KEY);
                showStoredValue();
            }
        );
        showStoredValue();
        add(name, store, stored, clear);
    }

    private void showStoredValue() {
        WebStorage.getItem(
            NAME_KEY,
            value -> {
                stored.setText("Stored value: " + (value == null ? "<no value stored>" : value));
            }
        );
    }
}
