package com.vaadin.recipes.recipe;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;

public class Recipe extends VerticalLayout implements HasDynamicTitle {

    public Recipe() {}

    @Override
    public String getPageTitle() {
        RecipeInfo recipeInfo = AllRecipes.getRecipeInfo(getClass());
        if (recipeInfo != null) {
            return AllRecipes.getTitle(recipeInfo);
        }
        return null;
    }
}
