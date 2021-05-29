package com.vaadin.recipes.recipe.highlighrouterlinkwithdefaultroute;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@RouteAlias(value = "", layout = HighlightRouterLinkWithDefaultRoute.class)
@Route(value = HomeView.ROUTE, layout = HighlightRouterLinkWithDefaultRoute.class)
public class HomeView extends Div {

    public static final String ROUTE = "home";

    public HomeView() {
        add("Home (default)");
    }
}
