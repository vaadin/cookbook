package com.vaadin.recipes.endpoints;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.NonNull;

@AnonymousAllowed
@Endpoint
public class RecipeEndpoint {

    @NonNull
    public List<@NonNull RecipeInfo> list() {
        return AllRecipes.getRecipes();
    }

    @NonNull
    public String getSource(String fullPath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (stream != null) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        return "<Not found>";
    }
}
