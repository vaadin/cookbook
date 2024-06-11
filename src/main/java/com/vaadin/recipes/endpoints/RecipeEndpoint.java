package com.vaadin.recipes.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.Nonnull;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;

@AnonymousAllowed
@Endpoint
public class RecipeEndpoint {

    @Nonnull
    public List<@Nonnull RecipeInfo> list() {
        return AllRecipes.getRecipes();
    }

    @Nonnull
    public String getSource(String fullPath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (stream != null) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        return "<Not found>";
    }
}
