package com.vaadin.recipes.recipe.gridrowheight;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.Set;

@Route("grid-row-height")
@Metadata(
        howdoI = "implement different row height in a grid",
        description = "Learn how to add rows with different heights",
        tags = {Tag.PUSH, Tag.GRID}
)
@CssImport(value = "recipe/gridrowheight/grid-styles.css", themeFor = "vaadin-grid")
public class GridRowHeight extends Recipe {

    private final Set<HeightData> stockCollect = Set.of(
            new HeightData("small", 30),
            new HeightData("little", 40),
            new HeightData("middle", 60),
            new HeightData("big", 90),
            new HeightData("bigger", 130));

    private final Grid<HeightData> grid;

    public GridRowHeight() {

        grid = new Grid<>();
        grid.addColumn(
                TemplateRenderer.
                    <HeightData>of("<div style='height: [[item.height]]px;'>[[item.name]]</div>")
                    .withProperty("name", HeightData::getName)
                    .withProperty("height", HeightData::getHeight))
                .setHeader("Name");
        grid.addColumn(heightData -> String.format("%d px", heightData.getHeight()))
                .setHeader("Height");
        grid.setItems(stockCollect);
        add(grid);
    }

    private class HeightData {
        private final String name;
        private final Integer height;

        public HeightData(String name, Integer height) {
            this.name = name;
            this.height = height;
        }

        public String getName() {
            return name;
        }

        public Integer getHeight() {
            return height;
        }
    }
}
