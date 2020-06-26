package com.vaadin.recipes.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Endpoint
public class RecipeEndpoint {

    @Autowired
    private AllRecipes allRecipes;

    public List<RecipeInfo> list() {
        return allRecipes.getRecipes();
    }

    public String getSource(String fullPath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (stream != null) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        return "<Not found>";
    }
}