package com.vaadin.recipes.recipe.dynamicallychangeprogressbarcolor;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.allowusertopickacolor.ColorPicker;

@Route("dynamically-change-progressbar-color")
@Metadata(howdoI = "Dynamically set the progress bar color", sourceFiles = {
        "recipe/dynamicallychangeprogressbarcolor/dynamically-change-progressbar-color.css" })
@CssImport(themeFor = "vaadin-progress-bar", value = "./recipe/dynamicallychangeprogressbarcolor/dynamically-change-progressbar-color.css")
public class DynamicallyChangeProgressBarColor extends VerticalLayout {

    public DynamicallyChangeProgressBarColor() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.5);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue("#0000ff");
        colorPicker.addValueChangeListener(e -> {
            // This variable is not available in progress bar but defined by the CSS import
            progressBar.getStyle().set("--progress-color", colorPicker.getValue());
        });

        add(progressBar, colorPicker);
    }
}