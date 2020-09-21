package com.vaadin.recipes.recipe.oslightdarktheme;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("os-light-dark-theme")
@Metadata(
    howdoI = "Automatically apply light or dark theme based on OS setting",
    description = "Automatically switch application between light and dark theme to match OS settings.",
    sourceFiles = { "recipe/oslightdarktheme/prefers-color-scheme.js" },
    tags = {Tag.THEME, Tag.USABILITY}
)
@JsModule("./recipe/oslightdarktheme/prefers-color-scheme.js")
public class OSLightDarkTheme extends Recipe {

    public OSLightDarkTheme() {
        add(new Paragraph("This view will switch between light and dark themes depending on your OS setting (via the browsers 'prefers-color-scheme')."));
        add(new Paragraph("Usually you want to apply this to your main layout instead of a single view."));
        add(new Paragraph("In this example, the whole application will be affected after you visit this view."));
    }
}