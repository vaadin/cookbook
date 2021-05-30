package com.vaadin.recipes.recipe.textfieldlabelleft;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("textfield-label-left")
@Metadata(howdoI = "Style TextField so that label appears on the left side", description = "When using FormLayout one can use FormItem to wrap TextField's so that label appears on the left side. However TextField is often used also elsewhere than forms and label on the left might be desired. In this recipe we show how to do that with CSS.", sourceFiles = {
        "recipe/textfieldlabelleft/textfieldlabelleft.css" }, tags = { Tag.THEME })
@CssImport(value = "./recipe/textfieldlabelleft/textfieldlabelleft.css", themeFor = "vaadin-text-field")
public class TextFieldLabelLeft extends Recipe {

    public TextFieldLabelLeft() {
//        setHeight("100px");
        TextField textFieldLeft = new TextField("Label left");
        TextField textFieldRight = new TextField("Label right");
        TextField textField = new TextField("Label top");
        textFieldLeft.addThemeName("label-left");
        textFieldRight.addThemeName("label-right");
        add(textField,textFieldLeft,textFieldRight);
    }
}
