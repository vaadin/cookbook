package com.vaadin.recipes.recipe.tabswithroutes;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.util.HashMap;
import java.util.Map;

@Route("tabs-with-routes")
@Metadata(howdoI = "Bind tabs to routes")
public class TabsWithRoutes extends Recipe implements RouterLayout, BeforeEnterObserver {

    public TabsWithRoutes() {
        RouteTabs routeTabs = new RouteTabs();
        routeTabs.add(new RouterLink("View A", ViewA.class));
        routeTabs.add(new RouterLink("View B with parameter 'text'", ViewB.class, "text"));
        add(routeTabs);
    }

    @Route(value = "tabs-with-routes/a", layout = TabsWithRoutes.class)
    private static class ViewA extends Div {

        public ViewA() {
            add(new Text("Content of A"));
        }
    }

    @Route(value = "tabs-with-routes/b", layout = TabsWithRoutes.class)
    private static class ViewB extends Div implements HasUrlParameter<String> {

        @Override
        public void setParameter(BeforeEvent beforeEvent, String s) {
            add(new Text("Content of B with parameter " + s));
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getNavigationTarget() == TabsWithRoutes.class) {
            event.forwardTo(ViewA.class);
        }
    }

    private static class RouteTabs extends Tabs implements BeforeEnterObserver {
        private final Map<RouterLink, Tab> routerLinkTabMap = new HashMap<>();

        public void add(RouterLink routerLink) {
            routerLink.setHighlightCondition(HighlightConditions.sameLocation());
            routerLink.setHighlightAction(
                (link, shouldHighlight) -> {
                    if (shouldHighlight) setSelectedTab(routerLinkTabMap.get(routerLink));
                }
            );
            routerLinkTabMap.put(routerLink, new Tab(routerLink));
            add(routerLinkTabMap.get(routerLink));
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            // In case no tabs will match
            setSelectedTab(null);
        }
    }
}
