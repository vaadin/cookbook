package com.vaadin.recipes.recipe.showsvg;

import com.vaadin.flow.component.html.Div;

public class SvgWrapper extends Div {

    public SvgWrapper(String svgContent) {
        super();
        this.setSvgContent(svgContent);
    }

    public void setSvgContent(String svgContent) {

        this.getElement().getNode().runWhenAttached(ui -> {
            ui.beforeClientResponse(this, executionContext -> {
                this.getElement().setProperty("innerHTML", svgContent);
            });
        });

    }

}
