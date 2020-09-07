package com.vaadin.recipes.recipe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Metadata {
    String howdoI();

    String description() default "";

    String[] sourceFiles() default {};

    Tag[] tags() default {};
}
