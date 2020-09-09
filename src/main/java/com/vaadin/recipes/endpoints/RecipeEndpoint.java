package com.vaadin.recipes.endpoints;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.recipes.data.AllRecipes;
import com.vaadin.recipes.data.RecipeInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;

@AnonymousAllowed
@Endpoint
public class RecipeEndpoint {

    public List<RecipeInfo> list() {
        return AllRecipes.getRecipes();
    }

    public String getSource(String fullPath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (stream != null) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        return "<Not found>";
    }
}
