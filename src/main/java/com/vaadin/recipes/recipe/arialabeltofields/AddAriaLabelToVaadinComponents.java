package com.vaadin.recipes.recipe.arialabeltofields;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
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
        yearSelect = new ComboBox<>("ComboBox example");
        yearSelect.setItems(IntStream.range(1900, 2021).boxed().collect(toList()));
        datePicker = new DatePicker("DatePicker example");
        dateTimePicker = new DateTimePicker("DateTimePicker example");
        checkbox = new Checkbox("CheckBox example");
        upload = new Upload();
        saveButton = new Button("S");
        add(searchField, yearSelect, datePicker, dateTimePicker, checkbox, upload, saveButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // helps to prevent possible timing issues
        getElement().executeJs("this.$server.addAriaLabels()");
    }

    @ClientCallable
    protected void addAriaLabels() {
        // we can utilize public inputElement property: https://github.com/vaadin/vaadin-text-field/blob/v3.0.1/src/vaadin-text-field-mixin.js#L384
        searchField.getElement()
                .executeJs("this.inputElement.setAttribute('aria-label', 'Search')");
        // it is recommend to remove auto generated aria-labelledby attribute
        searchField.getElement()
                .executeJs("this.inputElement.removeAttribute('aria-labelledby')");

        // the focused input element slightly deeper, but we can still utilize public properties
        yearSelect.getElement()
                .executeJs("this.inputElement.inputElement.setAttribute('aria-label', 'Year born')");
        // remove auto generated aria-labelledby attribute
        yearSelect.getElement()
                .executeJs("this.inputElement.inputElement.removeAttribute('aria-labelledby')");

        // we need to go into shadow dom to find underlying text-field for the aria-label
        datePicker.getElement()
                .executeJs("this.shadowRoot.querySelector('[part=\"text-field\"]').setAttribute('aria-label', 'Birth date')");

        // sometimes we need to get quite deep...
        dateTimePicker.getElement()
                .executeJs("this.querySelector('[slot=\"date-picker\"]').shadowRoot.querySelector('[part=\"text-field\"]').setAttribute('aria-label', 'Event date')");
        // ... or even deeper
        // this also means that this is not robust for the possible future updates of the component
        dateTimePicker.getElement()
                .executeJs("this.querySelector('[slot=\"time-picker\"]').shadowRoot.querySelector('vaadin-combo-box-light')" +
                        ".querySelector('[role=\"application\"]').setAttribute('aria-label', 'Event time')");

        // here we have public focusElement property
        checkbox.getElement()
                .executeJs("this.focusElement.setAttribute('aria-label', 'Task completed')");

        // need to get button element from the shadow dom
        upload.getElement()
                .executeJs("this.shadowRoot.querySelector('[part=\"upload-button\"]').shadowRoot.querySelector('button').setAttribute('aria-label', 'Upload resume')");

        // using shorthand query selector for getting element which id is `button`
        saveButton.getElement()
                .executeJs("this.$.button.setAttribute('aria-label', 'Save')");
    }

}
