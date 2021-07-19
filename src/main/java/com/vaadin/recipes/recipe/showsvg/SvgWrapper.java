package com.vaadin.recipes.recipe.showsvg;

import com.vaadin.flow.component.html.Div;

// wrapper is needed as element to set svg-code as 'innerHTML'
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
