package com.vaadin.recipes.recipe.lazydetails;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("lazy-details")
@Metadata(
    howdoI = "Initialize Details content only when opened",
    description = "Avoid initializing the content of a hidden Details panel until it is opened.",
    tags = {Tag.FLOW, Tag.PERFORMANCE}
)
public class LazyDetails extends Recipe {
    public LazyDetails() {
        Span placeholder = new Span("Please wait...");

        Details details = new Details("Expand me", placeholder);
        details.addOpenedChangeListener(event -> {
            // placeholder is attached until content replacement below has been run
            if (event.isOpened() && placeholder.isAttached()) {
                Span realContent = new Span("This is the real content");

                details.removeAll();
                details.add(realContent);
            }
        });

        add(details);
    }
}
