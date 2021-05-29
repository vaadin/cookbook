package com.vaadin.recipes.recipe.customlayout;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;

public class CustomLayout extends Html {
    private Map<String, Component> locations = new HashMap<>();

    public CustomLayout(String template) {
        super(template);
    }

    public CustomLayout(InputStream stream) {
        super(stream);
    }

    public void add(Component child, String location) {
        remove(location);
        locations.put(location, child);

        // Establish parent-child relationship, but leave DOM attaching to us
        getElement().appendVirtualChild(child.getElement());

        // Attach to the specified location in the actual DOM
        getElement().executeJs("this.querySelector('[location=\"'+$0+'\"]').appendChild($1)", location,
                child.getElement());

        // Ensure the element is removed from the DOM when it's detached
        child.addDetachListener(detachEvent -> {
            detachEvent.unregisterListener();
            getElement().executeJs("this.querySelector('[location=\"'+$0+'\"]').lastChild.remove()", location);

            // Also clear the bookkeeping
            locations.remove(location, child);
        });
    }

    public void remove(String location) {
        Component oldChild = locations.remove(location);
        if (oldChild != null) {
            remove(oldChild);
        }
    }

    public void remove(Component child) {
        getElement().removeVirtualChild(child.getElement());
    }
}