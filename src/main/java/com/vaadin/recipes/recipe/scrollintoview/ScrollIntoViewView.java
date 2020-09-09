package com.vaadin.recipes.recipe.scrollintoview;

import java.util.stream.Stream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("scroll-into-view")
@Metadata(howdoI = "Scroll a component into view", description = "Learn how to scroll a component into view in a Vaadin Java app.")
public class ScrollIntoViewView extends Recipe {

  ScrollIntoViewView() {
    var content = new VerticalLayout();
    var lastComponent = new VerticalLayout(new H2("This is the last heading"), new Paragraph("Latest message"));
    var scrollButton = new Button("Scroll to latest");

    // Create content
    Stream.generate(() -> new VerticalLayout(new H2("This is a heading"), new Paragraph(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")))
        .limit(10).forEach(content::add);
    content.add(lastComponent);

    // Add call for scrolling to the last component
    scrollButton.addClickListener(e -> {
      lastComponent.getElement().callJsFunction("scrollIntoView");
    });

    add(scrollButton, content);
  }
}
