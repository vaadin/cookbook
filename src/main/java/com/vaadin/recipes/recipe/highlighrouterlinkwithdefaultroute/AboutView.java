package com.vaadin.recipes.recipe.highlighrouterlinkwithdefaultroute;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = AboutView.ROUTE, layout = HighlightRouterLinkWithDefaultRoute.class)
public class AboutView extends Div {

    public static final String ROUTE = "about";

    public AboutView() {
        add("About");
    }
}
