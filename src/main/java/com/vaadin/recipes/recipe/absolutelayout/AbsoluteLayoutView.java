package com.vaadin.recipes.recipe.absolutelayout;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("absolute-layout")
@Metadata(
    howdoI = "Create AbsoluteLayout that is quite compatible with Vaadin 8",
    description = "Create AbsoluteLayout that is quite compatible with similary named component in Vaadin 8.",
    sourceFiles = { "AbsoluteLayout.java" }
)
public class AbsoluteLayoutView extends Recipe {

    public AbsoluteLayoutView() {
        setSizeFull();
        AbsoluteLayout first = new AbsoluteLayout();
        Span comp1 = new Span("First content component A");
        // This is the trad Vaadin 8 API
        first.addComponent(comp1);
        first.getPosition(comp1).setTop(20f,Unit.PIXELS);
        first.getPosition(comp1).setLeft(20f,Unit.PIXELS);
        Span comp2 = new Span("First content component B");
        first.addComponent(comp2);
        first.getPosition(comp2).setBottom(20f,Unit.PIXELS);
        first.getPosition(comp2).setRight(20f,Unit.PIXELS);
        first.setHeight("500px");
        AbsoluteLayout second = new AbsoluteLayout();
        // This is convenience API added in the example
        second.addTopLeft(new Span("Second content component A"), 20, 20);
        second.addBottomRight(new Span("Second content component B"), 20, 20);
        AbsoluteLayout third = new AbsoluteLayout();
        third.addTopLeft(new Span("Third content component A"), 20, 20);
        third.addBottomRight(new Span("Third content component B"), 20, 20);

        SplitLayout innerLayout = new SplitLayout();
        innerLayout.setOrientation(Orientation.VERTICAL);
        innerLayout.addToPrimary(second);
        innerLayout.addToSecondary(third);
        innerLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        innerLayout.setHeight("500px");

        SplitLayout outerLayout = new SplitLayout();
        outerLayout.addToPrimary(first);
        outerLayout.addToSecondary(innerLayout);
        outerLayout.setSizeFull();
        outerLayout.setSplitterPosition(50d);

        add(outerLayout);
    }
}
