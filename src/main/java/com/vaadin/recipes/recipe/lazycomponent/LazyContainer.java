package com.vaadin.recipes.recipe.lazycomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.function.SerializableConsumer;

import java.util.function.BiConsumer;

/**
 * Container that display a spinner until the component is loaded
 *
 * @param <T> Component lazy loaded
 */
@CssImport("./styles/loader-placeholder-styles.css")
public class LazyContainer<T extends Component> extends Composite<Div> {

    private boolean loaded = false;
    private T component;
    private BiConsumer<T, UI> componentLoader;
    private Thread loadingThread;
    private Div overlay;

    public LazyContainer() {
        super();
    }

    public LazyContainer(T component, BiConsumer<T, UI> componentLoader)  {
        this();
        setComponent(component, componentLoader);
    }
    /**
     * set the component to the lazy container
     *
     * @param component component Component
     * @param componentLoader function that load the data as a thread and update the component
     *                        All changes on the component should be wrapped into ui.access()
     */
    public void setComponent(T component, BiConsumer<T, UI> componentLoader) {
        this.component = component;
        this.componentLoader = componentLoader;
        getContent().add(component);
        // set the overlay
        overlay = new Div();
        overlay.addClassName("lazy-component-overlay");
        Div loader = new Div();
        loader.addClassName("lazy-component-loader");
        overlay.add(loader);
        getContent().add(overlay);
        // add relative position to the container
        getContent().addClassName("lazy-component-container");
        // run the component loader once/if the component is attached
        runBeforeClientResponse(ui -> {
            loadingThread = new LoadingThread(ui, this);
            loadingThread.start();
        });
    }

    public void setWidthFull() {
        getContent().setWidthFull();
        if (component != null) {
            component.getElement().getStyle().set(ElementConstants.STYLE_WIDTH, "100%");
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup the pending thread
        if (loadingThread != null) {
            loadingThread.interrupt();
            loadingThread = null;
        }
    }

    public BiConsumer<T, UI> getComponentLoader() {
        return componentLoader;
    }

    public T getComponent() {
        return component;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
        if (loaded) {
            overlay.getElement().removeFromParent();
            getContent().removeClassName("lazy-component-container");
        }
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }


    private class LoadingThread extends Thread {
        private final UI ui;
        private final LazyContainer<T> lazyContainer;

        public LoadingThread(UI ui, LazyContainer<T> lazyContainer) {
            this.ui = ui;
            this.lazyContainer = lazyContainer;
        }

        @Override
        public void run() {
            // Update the data for a while
            lazyContainer.getComponentLoader().accept(lazyContainer.getComponent(), ui);
            // Remove the spinner
            ui.access(() -> lazyContainer.setLoaded(true));
        }
    }
}
