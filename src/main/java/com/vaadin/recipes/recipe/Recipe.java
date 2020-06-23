package com.vaadin.recipes.recipe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Recipe {
    String theUserOfMyAppWantsTo();
}
