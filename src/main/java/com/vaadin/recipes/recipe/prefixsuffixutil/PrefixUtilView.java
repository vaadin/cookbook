package com.vaadin.recipes.recipe.prefixsuffixutil;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("prefix-util")
@Metadata(
        howdoI = "Add prefixcomponent",
        description = "Some components do have prefix slot, but do not have Java API yet to add the components to them. This recipe shows how to add components to those slots.",
        sourceFiles = {
            "PrefixSuffixUtil.java",
        }
)
public class PrefixUtilView extends Recipe {
    DatePicker datePicker = new DatePicker();
    Select<String> select = new Select<>();
    ComboBox<String> comboBox = new ComboBox<>();

    public PrefixUtilView() {
        PrefixUtil.setPrefixComponent(datePicker,new Span("date: "));
        PrefixUtil.setPrefixComponent(comboBox, new Span("quantity: "));
        PrefixUtil.setPrefixComponent(select, new Span("product: "));
        select.setItems("Book","Car","Computer","Phone");
        comboBox.setItems("10","20","30","40","50","60","70","80","90","100");

        add(datePicker,select,comboBox);
    }
}
