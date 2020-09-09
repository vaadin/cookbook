package com.vaadin.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.NavigationState;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;
import com.vaadin.recipes.recipe.Recipe;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.LoggerFactory;

@PWA(name = "Recipes", shortName = "recipes")
@Push
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
        RecipeInfo recipeInfo = findServerSideRecipe(request);
        if (recipeInfo == null) {
            recipeInfo = findTypeScriptRecipe(request);
        }

        if (recipeInfo != null) {
            setRouteMeta(settings, recipeInfo);
        } else {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !"/".equals(pathInfo)) {
                LoggerFactory.getLogger(getClass()).warn("No recipe info found for {}", request.getPathInfo());
            }
        }
    }

    private RecipeInfo findTypeScriptRecipe(VaadinRequest request) {
        String pathInfo = request.getPathInfo();

        RecipeInfo recipeInfo = tsRecipes.get(pathInfo);
        if (recipeInfo == null && pathInfo != null && pathInfo.startsWith("/")) {
            recipeInfo = tsRecipes.get(pathInfo.substring(1));
        }

        return recipeInfo;
    }

    private RecipeInfo findServerSideRecipe(VaadinRequest request) {
        Optional<NavigationState> target = request
            .getService()
            .getRouter()
            .resolveNavigationTarget(request.getPathInfo(), request.getParameterMap());

        if (!target.isPresent()) {
            return null;
        }
        NavigationState navigationState = target.get();
        Class<?> recipeClass = navigationState.getNavigationTarget();
        if (!(Recipe.class.isAssignableFrom(recipeClass))) {
            recipeClass = navigationState.getRouteTarget().getTarget();
        }

        return findRecipeClass(navigationState, recipeClass);
    }

    private RecipeInfo findRecipeClass(NavigationState navigationState, Class<?> recipeClass) {
        if (recipeClass == null) {
            return null;
        }
        if (Recipe.class.isAssignableFrom(recipeClass)) {
            // Server side route
            return AllRecipes.getRecipeInfo((Class<? extends Recipe>) recipeClass);
        }

        List<Class<? extends RouterLayout>> parents = navigationState.getRouteTarget().getParentLayouts();
        for (Class<? extends RouterLayout> parent : parents) {
            if (Recipe.class.isAssignableFrom(parent)) {
                return AllRecipes.getRecipeInfo((Class<? extends Recipe>) parent);
            }
        }

        return null;
    }

    private void setRouteMeta(AppShellSettings settings, RecipeInfo recipeInfo) {
        settings.setPageTitle(AllRecipes.getTitle(recipeInfo));
        settings.addMetaTag("description", AllRecipes.getDescription(recipeInfo));
    }
}
