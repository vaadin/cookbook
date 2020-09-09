package com.vaadin.recipes.recipe.dynamicallychangeprogressbarcolor;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.recipes.recipe.allowusertopickacolor.ColorPicker;

@Route("dynamically-change-progressbar-color")
@Metadata(
    howdoI = "Dynamically set the progress bar color",
    description = "A code snippet for changing the color of a Vaadin progress bar through the Java component API.",
    sourceFiles = { "recipe/dynamicallychangeprogressbarcolor/dynamically-change-progressbar-color.css" },
    tags = { Tag.THEME }
)
@CssImport(
    themeFor = "vaadin-progress-bar",
    value = "./recipe/dynamicallychangeprogressbarcolor/dynamically-change-progressbar-color.css"
)
public class DynamicallyChangeProgressBarColor extends Recipe {

    public DynamicallyChangeProgressBarColor() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.5);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue("#0000ff");
        colorPicker.addValueChangeListener(
            e -> {
                // This variable is not available in progress bar but defined by the CSS import
                progressBar.getStyle().set("--progress-color", colorPicker.getValue());
            }
        );

        add(progressBar, colorPicker);
    }
}
