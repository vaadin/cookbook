package com.vaadin.recipes.recipe.highlightweekends;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("datepicker-weekend-highlight")
@Metadata(howdoI = "Style DatePicker so that weekends are highlighted", description = "There is no Java API currently to set CSS class names to specific dates, but Aria attributes provide limited possibility to style individual dates. Note however, that this means that in case you support multiple languages, you need multiple sets of stylings per language. In this example weekends and some selected other dates are highlighted on the calendar popup.", sourceFiles = {
        "recipe/highlightweekend/highlightweekend.css" }, tags = { Tag.THEME })
@CssImport(value = "./recipe/highlightweekend/highlightweekend.css", themeFor = "vaadin-month-calendar")
public class DatePickerHighlightWeekends extends Recipe {

    public DatePickerHighlightWeekends() {
        DatePicker datePicker = new DatePicker();
        datePicker.getElement().getThemeList().add("weekend-highlight");
        add(datePicker);
    }
}
