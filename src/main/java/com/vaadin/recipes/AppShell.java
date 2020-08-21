package com.vaadin.recipes;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.NavigationState;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.startup.ApplicationRouteRegistry;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.recipe.Recipe;

@PWA(name = "Recipes", shortName = "recipes")
public class AppShell implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        VaadinRequest request = settings.getRequest();
        String pathInfo = request.getPathInfo();
        Optional<NavigationState> target = request.getService().getRouter().resolveNavigationTarget(pathInfo,
                request.getParameterMap());

        if (target.isPresent()) {
            // Server side route
            Class<? extends Recipe> recipeClass = (Class<? extends Recipe>) target.get().getNavigationTarget();
            String howDoI = getHowDoI(recipeClass);
            settings.setPageTitle("Vaadin Cookbook - " + howDoI);
            settings.addMetaTag("description", howDoI);
        }
    }

    private String getHowDoI(Class<? extends Recipe> recipeClass) {
        return "How do I " + AllRecipes.getRecipeInfo(recipeClass).getHowDoI();
    }
}
