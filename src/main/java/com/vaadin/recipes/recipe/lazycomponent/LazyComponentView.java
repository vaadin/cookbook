package com.vaadin.recipes.recipe.lazycomponent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.util.Random;

@Route("lazy-component-view")
@Metadata(howdoI = "lazily initialize components", sourceFiles = "LazyContainer.java")
public class LazyComponentView extends Recipe {

    private Div div1 = buildDiv();
    private Div div2 = buildDiv();
    private Div div3 = buildDiv();
    private Div div4 = buildDiv();
    private Div div5 = buildDiv();
    private Div div6 = buildDiv();

    public LazyComponentView() {
        LazyContainer<Div> lazyContainer1 = new LazyContainer<>(div1, this::loadDiv);
        LazyContainer<Div> lazyContainer2 = new LazyContainer<>(div2, this::loadDiv);
        LazyContainer<Div> lazyContainer3 = new LazyContainer<>(div3, this::loadDiv);
        LazyContainer<Div> lazyContainer4 = new LazyContainer<>(div4, this::loadDiv);
        LazyContainer<Div> lazyContainer5 = new LazyContainer<>(div5, this::loadDiv);
        LazyContainer<Div> lazyContainer6 = new LazyContainer<>(div6, this::loadDiv);
        add(lazyContainer1,lazyContainer2,lazyContainer3,
            lazyContainer4,lazyContainer5,lazyContainer6);
    }

    private static Div buildDiv() {
        Div div = new Div();
        div.setText("Not Loaded");
        div.setHeight("150px");
        div.setWidth("150px");
        return div;
    }

    private void loadDiv(Div div, UI ui) {
        try {
            int timeToWait = new Random().nextInt(9000) + 1000;
            Thread.sleep(timeToWait); // load the data for your component and apply it with in ui.access()
            ui.access(() -> div.setText("Content has been loaded in " + timeToWait + "ms"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
