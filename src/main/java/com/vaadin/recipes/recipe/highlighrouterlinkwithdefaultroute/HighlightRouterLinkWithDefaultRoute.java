package com.vaadin.recipes.recipe.highlighrouterlinkwithdefaultroute;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HighlightCondition;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("highlight-link-with-default-route")
@RoutePrefix("highlight-link-with-default-route")
@Metadata(
    howdoI = "Let a router-link be highlighted even it's target is the default view set up with @RouteAlias(\"\")",
    description = "a RouterLink gets highlight-attribute, when navigating to it's target route. " +
    "But this does not work when navigating to a route by it's default-url @RouteAlias(\"\")." +
    "This recipe shows how to set a highlight-condition that supports this." +
    "For testing enter default-url in address-bar: '.../highlight-link-with-default-route/'",
    sourceFiles = { "recipe/highlight-link-with-default-route/highlight-link-with-default-route.css" }
)
@CssImport("./recipe/highlight-link-with-default-route/highlight-link-with-default-route.css")
public class HighlightRouterLinkWithDefaultRoute extends Recipe implements RouterLayout {

    final Div contentWrapper = new Div();
    final HorizontalLayout linkContainer = new HorizontalLayout();

    public HighlightRouterLinkWithDefaultRoute() {
        add(linkContainer);
        add(contentWrapper);

        contentWrapper.setWidth("500px");
        contentWrapper.setHeight("150px");
        contentWrapper.getStyle().set("border", "2px green solid");

        RouterLink homeLink = new RouterLink("Link to Home", HomeView.class);
        RouterLink aboutLink = new RouterLink("Link to About", AboutView.class);

        homeLink.setHighlightCondition(this.buildHighLightConditionFor(HomeView.class));
        aboutLink.setHighlightCondition(this.buildHighLightConditionFor(AboutView.class));

        linkContainer.setSpacing(true);
        linkContainer.add(homeLink, new Span(" | "), aboutLink);
    }

    private HighlightCondition<RouterLink> buildHighLightConditionFor(Class<?> linkTargetClass) {
        return (link, afterNavigationEvent) -> {
            return afterNavigationEvent
                .getActiveChain()
                .stream()
                .anyMatch(element -> element.getClass() == linkTargetClass);
        };
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentWrapper.removeAll();
        contentWrapper.getElement().appendChild(content.getElement());
    }
}
