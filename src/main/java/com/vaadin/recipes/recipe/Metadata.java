package com.vaadin.recipes.recipe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Metadata {
    String howdoI();

    /**
     * 150-160 character description that is visible on the listing page and in
     * Google search results.
     */
    String description();

    String[] sourceFiles() default {};

    Tag[] tags() default {};
}
