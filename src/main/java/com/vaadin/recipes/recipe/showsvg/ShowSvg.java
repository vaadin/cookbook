package com.vaadin.recipes.recipe.showsvg;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.allowusertopickacolor.ColorPicker;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

        add(new Label("styled example"));

        // e. g. load svg code from file, classpath, ...
        SvgWrapper styleSvg = new SvgWrapper("<svg height=\"100\" width=\"100\">\n" +
                "  <circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"3\" fill=\"red\" />\n" +
                "  Sorry, your browser does not support inline SVG.  \n" +
                "</svg> ");

        // class 'styled-svg' overrides stroke and fill, see showsvg.css
        styleSvg.addClassName("styled-svg");
        styleSvg.getStyle().set("stroke", "red").set("fill", "green");

        add(styleSvg);


    }

}
