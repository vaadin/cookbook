package com.vaadin.recipes.recipe.showsvg;

import com.vaadin.flow.component.Svg;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("show-svg")
@Metadata(
    howdoI = "Show inline svg from any resource",
    description = "Recipe creates an element containing inline svg. " +
    "Example used from here: https://www.w3schools.com/graphics/tryit.asp?filename=trysvg_circle",
    sourceFiles = { "SvgWrapper.java", "recipe/showsvg/showsvg.css" }
)
@CssImport("./recipe/showsvg/showsvg.css")
public class ShowSvg extends Recipe {

    public ShowSvg() {
        add(new Paragraph("A css styled SVG example"));

        // e. g. load svg code from file, classpath, ...
        Svg styleSvg = new Svg("""
                <svg height="100" width="100">
                  <circle cx="50" cy="50" r="40" stroke="black" stroke-width="3" fill="red" />
                  Sorry, your browser does not support inline SVG.
                </svg>
        """);

        // class 'styled-svg' overrides stroke and fill, see showsvg.css
        styleSvg.addClassName("styled-svg");
        styleSvg.getStyle().set("stroke", "red").set("fill", "green");

        add(styleSvg);
    }
}
