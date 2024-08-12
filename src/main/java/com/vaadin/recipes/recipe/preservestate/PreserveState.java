package com.vaadin.recipes.recipe.preservestate;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("preserve-state")
@Metadata(howdoI = "Preserve some UI state", description = "Store some parts of a view's state so that the state remains if the user navigates back to the same view again.", tags = {
        Tag.FLOW })
public class PreserveState extends Recipe {

    @UIScope
    @Component
    public static class PreservedState {
        private String value = "";
    }

    private final PreservedState state;

    public PreserveState(PreservedState state) {
        this.state = state;

        add(new Paragraph(
                "The text in this field will remain even if you navigate to some other view in the application and then back again to this view."),
                new TextField("State", this.state.value,
                        event -> this.state.value = event.getValue()));
    }
}
