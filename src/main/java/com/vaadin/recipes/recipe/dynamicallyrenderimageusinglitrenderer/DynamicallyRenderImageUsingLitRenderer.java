package com.vaadin.recipes.recipe.dynamicallyrenderimageusinglitrenderer;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Route("dynamically-render-an-image-using-litrenderer")
@Metadata(
        howdoI = "Dynamically render an image using LitRenderer",
        description = "This recipe demonstrates how to dynamically render an image using LitRenderer and Data URL.",
        tags = {Tag.FLOW, Tag.GRID}
)
public class DynamicallyRenderImageUsingLitRenderer extends Recipe {

    public DynamicallyRenderImageUsingLitRenderer() {
        Person joe = new Person(null); //initially no image is set
        Grid<Person> grid = new Grid<>();
        grid.setItems(Arrays.asList(joe));

        grid.addColumn(
                LitRenderer.<Person>of("<img src=${item.image} />")
                        .withProperty("image", person -> {
                            if (person.getImage() == null) {
                                return "";
                            }
                            return "data:image;base64," + Base64.getEncoder().encodeToString(person.getImage());
                        })
        ).setHeader("Image");

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            byte[] imageBytes;
            try {
                imageBytes = buffer.getInputStream(fileName).readAllBytes();
                joe.setImage(imageBytes);
                grid.getDataProvider().refreshItem(joe);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        add(new H3("Upload an image to update the Grid using LitRenderer"), upload, grid);
    }

    public static class Person {
        private byte[] image;

        public Person(byte[] image) {
            this.image = image;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }
    }
}
