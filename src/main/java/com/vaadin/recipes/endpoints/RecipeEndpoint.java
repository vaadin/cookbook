package com.vaadin.recipes.endpoints;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.recipes.data.Recipe;

@AnonymousAllowed
@Endpoint
public class RecipeEndpoint {

    private static List<Recipe> allRecipes = new ArrayList<>();
    static {
        allRecipes.add(new Recipe("grid-csv-export", "Export grid data as CSV"));
        allRecipes.add(new Recipe("grid-pdf-export", "Export grid data as PDF"));
        allRecipes.add(new Recipe("sidebar-hierarchical-items", "Have sidebar navigation with hierarchical items"));
        allRecipes.add(new Recipe("circular-progress-indicator", "See a circular progress indicator"));
        allRecipes.add(
                new Recipe("auto-dark-light", "Get a light or dark themed UI depending on their system preferences"));
        allRecipes.add(new Recipe("dynamic-svg", "See status as a dynamic vector (SVG) image"));
        allRecipes.add(new Recipe("filtered-item-list", "See a list of items and filter them"));
    }

    public List<Recipe> list() {
        return allRecipes;
    }
}