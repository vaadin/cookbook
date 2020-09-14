package com.vaadin.recipes.recipe.cssfromjava;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("css-from-java")
@Metadata(
    howdoI = "Add CSS class definitions to current page in Java",
    description = "This recipe shows how to inject small CSS class definitions into the current page from Java."
)
public class CssFromJava extends Recipe {

    public CssFromJava() {

      Element styleElement = new Element("style").setText(".addlater {color:firebrick}");

      Span span1 = new Span("Fire Brick");
      span1.addClassName("addlater");
      Span span2 = new Span("Fire Brick");
      span2.addClassName("addlater");
      Span span3 = new Span("Fire Brick");
      span3.addClassName("addlater");

        Button buttonMain = new Button("Click to add fire brick");
        buttonMain.addClickListener(
            e -> {
              if (UI.getCurrent().getElement().indexOfChild(styleElement) < 0) {
                UI.getCurrent().getElement().appendChild(styleElement);
              }
            }
        );

        VerticalLayout stage = new VerticalLayout();
        stage.add(span1, span2, span3, buttonMain);
        this.add(stage);
    }
}
