package com.vaadin.recipes.endpoints;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;

import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Endpoint
public class RecipeEndpoint {

    @Autowired
    private AllRecipes allRecipes;

    public List<RecipeInfo> list() {
        return allRecipes.getRecipes();
    }
}