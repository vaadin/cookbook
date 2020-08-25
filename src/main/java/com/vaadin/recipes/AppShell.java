package com.vaadin.recipes;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.NavigationState;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;
import com.vaadin.recipes.recipe.Recipe;

@PWA(name = "Recipes", shortName = "recipes")
public class AppShell implements AppShellConfigurator {

    private static final String TS_RECIPE_INFO_JSON = "ts-recipe-info.json";
    private Map<String, RecipeInfo> tsRecipes = new HashMap<>();

    public AppShell() {
        try (InputStream res = getClass().getClassLoader().getResourceAsStream(TS_RECIPE_INFO_JSON)) {
            if (res == null) {
                System.err.println("No '" + TS_RECIPE_INFO_JSON + "' found");
            } else {
                RecipeInfo[] infos = new ObjectMapper().readerForArrayOf(RecipeInfo.class).readValue(res);
                for (RecipeInfo info : infos) {
                    tsRecipes.put(info.getUrl(), info);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        VaadinRequest request = settings.getRequest();
        String pathInfo = request.getPathInfo();
        Optional<NavigationState> target = request.getService().getRouter().resolveNavigationTarget(pathInfo,
                request.getParameterMap());

        if (target.isPresent()) {
            // Server side route
            Class<? extends Recipe> recipeClass = (Class<? extends Recipe>) target.get().getNavigationTarget();
            String howDoI = getHowDoI(AllRecipes.getRecipeInfo(recipeClass));
            setRouteMeta(settings, howDoI);
        } else {
            RecipeInfo recipeInfo = tsRecipes.get(pathInfo);
            if (recipeInfo == null && pathInfo != null && pathInfo.startsWith("/")) {
                recipeInfo = tsRecipes.get(pathInfo.substring(1));
            }

            if (recipeInfo != null) {
                // TS route
                setRouteMeta(settings, getHowDoI(recipeInfo));
            }
        }
    }

    private void setRouteMeta(AppShellSettings settings, String howDoI) {
        settings.setPageTitle(howDoI + " - Vaadin Cookbook");
        settings.addMetaTag("description", howDoI);
    }

    private String getHowDoI(RecipeInfo recipeInfo) {
        return "How do I " + recipeInfo.getHowDoI();

    }
}
