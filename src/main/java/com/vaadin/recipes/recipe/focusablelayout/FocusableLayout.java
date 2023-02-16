package com.vaadin.recipes.recipe.focusablelayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("focusable-layout")
@Metadata(howdoI = "Create a closable side panel", description = "Create a closable side panel by using focusable custom layout based on FlexLayout. In this example we have side panel on the right where items can be dragged to main Grid. Blur will close the side panel and button click will reopen it.", tags = {
        Tag.GRID, Tag.LAYOUT })
public class FocusableLayout extends Recipe {

    private List<String> draggedItems;
    private Grid<String> dragSource;

    public FocusableLayout() {
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("500px");
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setSpacing(false);
        vLayout.setPadding(false);
        MyLayout mLayout = new MyLayout();
        Button button = new Button(VaadinIcon.MENU.create());
        button.getStyle().set("align-self", "flex-end");
        button.setVisible(false);
        vLayout.add(button);
        mLayout.add(new Span("Select"));
        mLayout.focus();
        mLayout.addBlurListener(event -> {
            mLayout.setVisible(false);
            button.setVisible(true);
        });
        button.addClickListener(event -> {
            mLayout.setVisible(true);
            mLayout.focus();
            button.setVisible(false);
        });
        mLayout.setWidth("500px");
        mLayout.setHeightFull();
        mLayout.setFlexDirection(FlexDirection.COLUMN);

        Grid<String> grid1 = new Grid<>();
        grid1.setSizeFull();
        grid1.addColumn(String::toString);
        grid1.setItems("One", "Two", "Three", "Four");

        Grid<String> grid2 = new Grid<>();
        grid2.setSizeFull();
        grid2.addColumn(String::toString);
        grid2.setItems("Car", "Bus", "Tram", "Train");

        vLayout.add(grid1);
        mLayout.add(grid2);

        ComponentEventListener<GridDragStartEvent<String>> dragStartListener = event -> {
            draggedItems = event.getDraggedItems();
            dragSource = event.getSource();
            grid1.setDropMode(GridDropMode.BETWEEN);
            grid2.setDropMode(GridDropMode.BETWEEN);
        };

        ComponentEventListener<GridDragEndEvent<String>> dragEndListener = event -> {
            draggedItems = null;
            dragSource = null;
            grid1.setDropMode(null);
            grid2.setDropMode(null);
        };

        ComponentEventListener<GridDropEvent<String>> dropListener = event -> {
            Optional<String> target = event.getDropTargetItem();
            if (target.isPresent() && draggedItems.contains(target.get())) {
                return;
            }

            // Remove the items from the source grid
            @SuppressWarnings("unchecked")
            ListDataProvider<String> sourceDataProvider = (ListDataProvider<String>) dragSource
                    .getDataProvider();
            List<String> sourceItems = new ArrayList<>(
                    sourceDataProvider.getItems());
            sourceItems.removeAll(draggedItems);
            dragSource.setItems(sourceItems);

            // Add dragged items to the target Grid
            Grid<String> targetGrid = event.getSource();
            @SuppressWarnings("unchecked")
            ListDataProvider<String> targetDataProvider = (ListDataProvider<String>) targetGrid
                    .getDataProvider();
            List<String> targetItems = new ArrayList<>(
                    targetDataProvider.getItems());

            int index = target.map(item -> targetItems.indexOf(item)
                    + (event.getDropLocation() == GridDropLocation.BELOW ? 1
                            : 0))
                    .orElse(0);
            targetItems.addAll(index, draggedItems);
            targetGrid.setItems(targetItems);
        };

        grid1.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid1.addDropListener(dropListener);

        grid2.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid2.addDragStartListener(dragStartListener);
        grid2.addDragEndListener(dragEndListener);
        grid2.setRowsDraggable(true);

        hLayout.add(vLayout, mLayout);
        add(hLayout);
    }

    public class MyLayout extends FlexLayout implements Focusable<MyLayout> {

        public MyLayout() {
            this.setTabIndex(0);
        }
    }

}
