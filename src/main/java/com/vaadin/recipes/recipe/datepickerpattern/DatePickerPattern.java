package com.vaadin.recipes.recipe.datepickerpattern;

import java.time.LocalDate;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("date-picker-pattern")
@Metadata(howdoI = "Use a custom date format based on a pattern text with DatePicker", sourceFiles = {
    "recipe/date-picker-pattern/date-picker-pattern.js" })
public class DatePickerPattern extends Recipe {
  public DatePickerPattern() {

    DatePickerWithPattern datePicker = new DatePickerWithPattern();
    datePicker.setValue(LocalDate.of(2020, 4, 11));
    datePicker.setLabel("Custom date pattern");
    datePicker.setPattern("yyyy-MM-dd");

    // Add a pattern selector for the example
    ComboBox<String> comboBox = new ComboBox<>("Select the date pattern");
    comboBox.setItems("yyyy-MM-dd", "dd MMMM yyyy", "MM/dd/yyyy", "d. MMM yyyy", "M-d-yy", "yyyy'年'M'月'd'日'");
    comboBox.setAllowCustomValue(true);
    comboBox.addValueChangeListener(e -> datePicker.setPattern(e.getValue()));
    comboBox.addCustomValueSetListener(e -> datePicker.setPattern(e.getDetail()));
    comboBox.setValue("yyyy-MM-dd");

    add(datePicker, comboBox);
  }

  @JsModule("./recipe/date-picker-pattern/date-picker-pattern.js")
  @NpmPackage(value = "date-fns", version = "2.16.0")
  public static class DatePickerWithPattern extends DatePicker {

    private String pattern;

    public String getPattern() {
      return pattern;
    }

    public void setPattern(String pattern) {
      this.pattern = pattern;
      applyPattern();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
      super.onAttach(attachEvent);
      applyPattern();
    }

    private void applyPattern() {
      UI.getCurrent().beforeClientResponse(this, ctx -> {
        this.getElement().executeJs("window._setDatePickerPattern(this, $0)", pattern);
      });
    }
  }

}
