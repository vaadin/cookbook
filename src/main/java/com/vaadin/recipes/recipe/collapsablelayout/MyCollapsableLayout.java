package com.vaadin.recipes.recipe.collapsablelayout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * A starting point for creating a custom collapsable layout with a configurable header.
 * Could also be extended to include a footer if desired.
 */
public class MyCollapsableLayout extends VerticalLayout {

    private HorizontalLayout header;
    private Div content;
    boolean collapseOnHeaderClick = false;

    /**
     * Creates a new Collapsable layout that will start collapsed.
     */
    public MyCollapsableLayout() {
        this(false);
    }

    /**
     * Creates a new collapsable layout that will start in the mode given.
     *
     * @param startVisible true starts with content hidden, false starts with content shown
     */
    public MyCollapsableLayout(boolean startVisible) {
        setWidthFull();
        setSpacing(false);
        setMargin(false);
        setPadding(false);

        header = new HorizontalLayout();
        header.setWidthFull();
        header.setSpacing(false);
        header.setPadding(false);
        header.setMargin(false);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setHeight("40px");
        header.getStyle().set("border-bottom", "1px solid #eee");

        content = new Div();
        content.setSizeFull();
        content.setVisible(startVisible);

        header.addClickListener(this::onHeaderClick);
        addClassName("collapsable-layout");
        header.addClassName("collapsable-layout-header");
        content.addClassName("collapsable-layout-content");

        add(header);
        addAndExpand(content);
    }

    protected void onHeaderClick(ClickEvent<HorizontalLayout> horizontalLayoutClickEvent) {
        if (collapseOnHeaderClick) {
            toggleContentVisibility();
        }
    }

    /**
     * Is the layout set to collapse when the header is clicked.
     *
     * @return true header clicks toggle state, false header clicks do not toggle state.
     */
    public boolean isCollapseOnHeaderClick() {
        return collapseOnHeaderClick;
    }

    /**
     * Enable or disable the header click toggles collapsed state function.
     *
     * @param collapseOnHeaderClick true header clicks toggle state, false header clicks do not toggle state.
     */
    public void setCollapseOnHeaderClick(boolean collapseOnHeaderClick) {
        this.collapseOnHeaderClick = collapseOnHeaderClick;
    }

    /**
     * Toggle the current visiblity state programmatically.
     */
    public void toggleContentVisibility() {
        boolean previousVisiblity = getContent().isVisible();
        content.setVisible(!content.isVisible());
        internalFireCollapsedStateEvent(previousVisiblity);
    }

    protected void internalFireCollapsedStateEvent(boolean previousVisiblity) {
        fireEvent(new CollapseChangedEvent(this, false, previousVisiblity, content.isVisible()));
    }

    /**
     * Explicitly set the visibility state. Note does not fire events.
     *
     * @param visible true content should be visible, false content should be hidden.
     */
    public void setContentVisible(boolean visible) {
        setContentVisible(visible, false);
    }

    /**
     * Explicitly set the visibility state
     *
     * @param visible    true content should be visible, false content should be hidden.
     * @param fireEvents true fire collapse state change event, false do not fire events.
     */
    public void setContentVisible(boolean visible, boolean fireEvents) {
        boolean previousVisibility = getContent().isVisible();

        content.setVisible(visible);

        if (fireEvents) {
            internalFireCollapsedStateEvent(previousVisibility);
        }
    }

    /**
     * Returns the current content visiblity state.
     *
     * @return true content is visible, false content is hidden
     */
    public boolean isContentVisible() {
        return content.isVisible();
    }

    /**
     * Adds a header component.
     *
     * @param component the component to add.
     */
    public void addHeaderComponent(Component... component) {
        getHeader().add(component);
    }

    /**
     * Removes a header component.
     *
     * @param component the component to remove.
     */
    public void removeHeaderComponent(Component... component) {
        getHeader().remove(component);
    }

    /**
     * Adds a header component as last (in the current set of components) and adds a margin that will push it to the far right.
     *
     * @param component the component to add
     */
    public void addHeaderComponentAsLastAndAlignToRight(Component component) {
        getHeader().addComponentAtIndex(getHeader().getComponentCount(), component);
        component.getElement().getStyle().set("margin-left", "auto");
    }

    /**
     * Adds a component to the content area.
     *
     * @param component the component to add.
     */
    public void addContentComponent(Component... component) {
        getContent().add(component);
    }

    /**
     * Removes a component from the content area.
     *
     * @param component the component to remove.
     */
    public void removeContentComponent(Component... component) {
        getContent().remove(component);
    }

    /**
     * Adds a listener that is triggrered when the visiblity state of the content changes.
     *
     * @param listener the listener to add
     * @return the registration for managing the listener.
     */
    public Registration addCollapseChangeListener(MyCollapsableLayoutCollapseStateListener listener) {
        return addListener(CollapseChangedEvent.class, listener);
    }

    //Returns the content, override to change.
    protected Div getContent() {
        return content;
    }

    //Returns the header, override to change.
    protected HorizontalLayout getHeader() {
        return header;
    }

    /**
     * The interface to listen for collapse change events.
     */
    public interface MyCollapsableLayoutCollapseStateListener extends ComponentEventListener<CollapseChangedEvent> {

        @Override
        void onComponentEvent(CollapseChangedEvent event);
    }

    /**
     * The event that is fired when the collapse state changes.
     */
    public static class CollapseChangedEvent extends ComponentEvent<MyCollapsableLayout> {

        private boolean wasVisible;
        private boolean currentlyVisible;

        public CollapseChangedEvent(MyCollapsableLayout source, boolean fromClient, boolean wasVisible, boolean currentlyVisible) {
            super(source, fromClient);
            this.wasVisible = wasVisible;
            this.currentlyVisible = currentlyVisible;
        }

        public boolean isWasVisible() {
            return wasVisible;
        }

        public boolean isCurrentlyVisible() {
            return currentlyVisible;
        }
    }
}