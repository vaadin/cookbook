package com.vaadin.recipes.recipe.bytearraytoimage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import org.apache.commons.io.IOUtils;

@Route("byte-array-to-image")
@Metadata(
    howdoI = "Convert a byte array to an Image",
    description = "Learn how to use Image and StreamResource to display an image from a byte array."
)
public class ByteArrayToImage extends Recipe {

    public ByteArrayToImage() {
        StreamResource streamResource = new StreamResource("vaadin-logo.png", () -> new ByteArrayInputStream(getImageAsByteArray()));

        Image image = new Image(streamResource, "Vaadin logo");
        add(image);
    }

    private byte[] getImageAsByteArray() {
        try {
            return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("images/vaadin-logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
