package com.vaadin.recipes.recipe;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;

public class Recipe extends VerticalLayout implements HasDynamicTitle {

    public Recipe() {
        addClassNames("container-fluid", "flow-example");
        Metadata metadata = getClass().getAnnotation(Metadata.class);
        if(metadata != null && metadata.addons().length > 0) {
            for(String addon : metadata.addons()) {
                var parts = addon.split(";",2);
                String name = parts[0];
                String url = parts[1];
                add(new Anchor(url, "This example uses %s add-on.".formatted(name)));
            }
        }
    }

    @Override
    public String getPageTitle() {
        RecipeInfo recipeInfo = AllRecipes.getRecipeInfo(getClass());
        if (recipeInfo != null) {
            return AllRecipes.getTitle(recipeInfo);
        }
        return null;
    }
}
