package com.vaadin.recipes.recipe.multivalidator;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("multi-validator")
@Metadata(
        howdoI = "Show more than one validation message at a time",
        description = "Use MultiValidator to show all failing validations for a field at once instead of stopping at the first Validator that fails.",
        sourceFiles = {"MultiValidator.java"},
        tags = {Tag.FLOW, Tag.JAVA, Tag.BINDER})
public class MultiValidatorView extends Recipe {
    private final TextField uniTextField;
    private final TextField multiTextField;

    public MultiValidatorView() {
        var instructions = new Div(
                new Paragraph(
                        "Enter something into either field then press the tab key to validate. Both fields have the same 3 constraints:"),
                new UnorderedList(
                        new ListItem("Must be between 4 and 8 characters long"),
                        new ListItem("Must start with a letter"),
                        new ListItem("Must contain only alphanumeric characters")),
                new Paragraph(
                        "The first field will only show the first failing validation message. The second field will show all failing validation messages."));

        uniTextField = new TextField("Field with standard validation");
        uniTextField.setWidth("300px");
        uniTextField.setValueChangeMode(ValueChangeMode.LAZY);
        uniTextField.addValueChangeListener(this::onUniValueChange);

        multiTextField = new TextField("Field with MultiValidator validation");
        multiTextField.setWidth("300px");
        multiTextField.setValueChangeMode(ValueChangeMode.LAZY);
        multiTextField.addValueChangeListener(this::onMultiValueChange);

        add(instructions);
        add(uniTextField);
        add(multiTextField);


        var uniBinder = new Binder<Record>();
        uniBinder.forField(uniTextField)
                .asRequired("Uni Text Field is required.")
                .withValidator(new RegexpValidator("Uni Text Field must start with a letter.",
                        "[\\p{Alpha}].*", true))
                .withValidator(new RegexpValidator(
                        "Uni Text Field must contain only alphanumeric characters.",
                        "[\\p{Alnum}]*", true))
                .withValidator(new StringLengthValidator(
                        "Uni Text Field must be between 4 and 8 characters long.", 4, 8))
                .withNullRepresentation("")
                .bind(Record::getText, Record::setText);


        var multiBinder = new Binder<Record>();
        multiBinder.forField(multiTextField)
                .asRequired("Multi Text Field is required.")
                .withValidator(new MultiValidator<String>()
                        .add(new RegexpValidator("Multi Text Field must start with a letter.",
                                "[\\p{Alpha}].*", true))
                        .add(new RegexpValidator(
                                "Multi Text Field must contain only alphanumeric characters.",
                                "[\\p{Alnum}]*", true))
                        .add(new StringLengthValidator(
                                "Multi Text Field must be between 4 and 8 characters long.", 4, 8)))
                .withNullRepresentation("")
                .bind(Record::getText, Record::setText);
    }

    private void onUniValueChange(
            AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        if (event.isFromClient()) {
            multiTextField.setValue(event.getSource().getValue());
        }
    }

    private void onMultiValueChange(
            AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        if (event.isFromClient()) {
            uniTextField.setValue(event.getSource().getValue());
        }
    }


    private static class Record {
        private String text;

        public Record(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
