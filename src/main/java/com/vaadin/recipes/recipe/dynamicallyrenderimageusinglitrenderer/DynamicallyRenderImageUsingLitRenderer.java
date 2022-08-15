package com.vaadin.recipes.recipe.dynamicallyrenderimageusinglitrenderer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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

    public DynamicallyRenderImageUsingLitRenderer() throws IOException {
        // The images here are read from the resources' folder. But the example would also work if the
        // images were dynamically obtained from other source (for example, if uploaded by the user).
        byte[] imageOne = getClass().getResourceAsStream("/images/image-one.png").readAllBytes();
        byte[] imageTwo = getClass().getResourceAsStream("/images/image-two.png").readAllBytes();

        Foo foo = new Foo(imageOne);
        Grid<Foo> grid = new Grid<>();
        grid.setItems(Arrays.asList(foo));

        grid.addColumn(
                LitRenderer.<Foo>of("<img src=${item.image} />")
                        .withProperty("image", pojo -> {
                            if (pojo.getImage() == null) {
                                return "";
                            }
                            return "data:image;base64," + Base64.getEncoder().encodeToString(pojo.getImage());
                        })
        ).setHeader("Image");


        Button toggleButton = new Button("Replace image in Grid", e -> {
            if (foo.getImage() == imageOne) {
                foo.setImage(imageTwo);
            } else {
                foo.setImage(imageOne);
            }
            grid.getDataProvider().refreshItem(foo);
        });

        add(toggleButton, grid);
    }

    public static class Foo {
        private byte[] image;

        public Foo(byte[] image) {
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
