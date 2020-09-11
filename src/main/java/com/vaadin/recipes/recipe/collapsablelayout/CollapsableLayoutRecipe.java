package com.vaadin.recipes.recipe.collapsablelayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("collapsable-layout")
@Metadata(
    sourceFiles = {"MyCollapsableLayout.java"},
    howdoI = "Collapsable layout with header and content",
    description = "A simple starting point for making a custom collapsable layout. Has a simple header and content section. Could be extended to " +
        "have a footer section. Supports expansion and collapse events from external components or from clicks to the header section. Using own " +
        "event so even detaching the content (for performance) could be done if desired."
)
public class CollapsableLayoutRecipe extends Recipe {

    public CollapsableLayoutRecipe() {

        MyCollapsableLayout collapsableLayout = new MyCollapsableLayout();
        add(collapsableLayout);

        //A border to show the outline of the layout itself
        collapsableLayout.getElement().getStyle().set("border", "1px solid #aaa");

        collapsableLayout.addContentComponent(createSomeContent());

        //Add a header caption
        collapsableLayout.addHeaderComponent(new Span("Collapsable layout with custom header"));

        //Add a header button that toggles the visibility on click
        Button collapseButton = new Button("Show", e -> collapsableLayout.toggleContentVisibility());
        collapsableLayout.addHeaderComponentAsLastAndAlignToRight(collapseButton);

        //Change the button caption based on the collapse state change
        collapsableLayout.addCollapseChangeListener(e -> collapseButton.setText(e.isCurrentlyVisible() ? "Hide" : "Show"));
    }

    //Just something to show/hide
    private Component createSomeContent() {

        //Some random content that we might want to show/hide
        DatePicker aContentDatePicker = new DatePicker("Pick a date, or don't");
        ComboBox<String> aContentComboBox = new ComboBox<>("Some content");
        return new VerticalLayout(aContentDatePicker, aContentComboBox);

    }

}
