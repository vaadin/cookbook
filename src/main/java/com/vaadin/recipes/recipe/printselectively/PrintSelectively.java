package com.vaadin.recipes.recipe.printselectively;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("print-selectively")
@Metadata(
    howdoI = "Print a subset of components",
    description = "Specify the printable components",
    tags = { Tag.FLOW }
)
@CssImport(value = "./recipe/print-selectively/shared-styles.css")
public class PrintSelectively extends Recipe {

    public PrintSelectively() {
        Div printable = new Div(new Span("printable"));
        Div nonPrintable = new Div(new Span("non-printable"));

        // The classname is used to control print-ability via CSS
        printable.addClassName("printable");

        Button print = new Button("Print");
        print.addClickListener(e -> print.getElement().executeJs("print();"));

        add(printable, nonPrintable, print);
    }
}
