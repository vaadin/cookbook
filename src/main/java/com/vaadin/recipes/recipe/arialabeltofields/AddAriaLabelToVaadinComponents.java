package com.vaadin.recipes.recipe.arialabeltofields;

import com.vaadin.flow.component.HasAriaLabel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Route("aria-label-to-components")
@Metadata(
        howdoI = "Add aria-label to various Vaadin components",
        description = "Improve accessibility and screen reader compatibility by adding aria-label to various Vaadin components through Element API. Tested with NVDA.",
        tags = {Tag.USABILITY, Tag.ACCESSIBILITY, Tag.WAIARIA}
)
public class AddAriaLabelToVaadinComponents extends Recipe {

    private final TextField searchField;
    private final ComboBox<Integer> yearSelect;
    private final DatePicker datePicker;
    private final DateTimePicker dateTimePicker;
    private final Checkbox checkbox;
    private final Upload upload;
    private final Button saveButton;

    public AddAriaLabelToVaadinComponents() {
        searchField = new TextField("TextField example");
        searchField.setAriaLabel("Search");

        yearSelect = new ComboBox<>("ComboBox example");
        yearSelect.setItems(IntStream.range(1900, 2021).boxed().collect(toList()));
        yearSelect.setAriaLabel("Year born");

        datePicker = new DatePicker("DatePicker example");
        datePicker.setAriaLabel("Birth date");

        dateTimePicker = new DateTimePicker("DateTimePicker example");
        dateTimePicker.setDateAriaLabel("Event date");
        dateTimePicker.setTimeAriaLabel("Event time");

        checkbox = new Checkbox("CheckBox example");
        checkbox.setAriaLabel("Task completed");

        upload = new Upload();
        ((HasAriaLabel) upload.getUploadButton()).setAriaLabel("Upload resume");

        saveButton = new Button("S");
        saveButton.setAriaLabel("Save");

        add(searchField, yearSelect, datePicker, dateTimePicker, checkbox, upload, saveButton);
    }
}
