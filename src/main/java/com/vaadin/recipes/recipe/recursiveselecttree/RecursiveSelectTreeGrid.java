package com.vaadin.recipes.recipe.recursiveselecttree;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.grid.AbstractGridMultiSelectionModel;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.selection.SelectionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jcgueriaud
 */
public class RecursiveSelectTreeGrid<T> extends TreeGrid<T> {

    @Override
    public GridSelectionModel<T> setSelectionMode(SelectionMode selectionMode) {
        if (SelectionMode.MULTI == selectionMode) {
            GridSelectionModel<T> model = new AbstractGridMultiSelectionModel<T>(this) {
                @Override
                protected void fireSelectionEvent(SelectionEvent<Grid<T>, T> event) {
                    ((RecursiveSelectTreeGrid<T>) this.getGrid()).fireEvent((ComponentEvent<Grid<?>>) event);
                }

                @Override
                public void selectFromClient(T item) {
                    updateSelection(new HashSet<>(getChildrenRecursively(Collections.singletonList(item), 99)),
                        Collections.emptySet());
                }

                @Override
                public void deselectFromClient(T item) {
                    updateSelection(Collections.emptySet(), new HashSet<>(getChildrenRecursively(Collections.singletonList(item), 99)));
                }

            };
            setSelectionModel(model, selectionMode);
            return model;
        } else {
            return super.setSelectionMode(selectionMode);
        }
    }


    protected Collection<T> getChildrenRecursively(Collection<T> items,
                                                   int depth) {
        List<T> itemsWithChildren = new ArrayList<>();
        if (depth < 0) {
            return itemsWithChildren;
        }
        items
            .forEach(item -> {
                itemsWithChildren.add(item);
                if (getDataCommunicator().hasChildren(item)) {
                    itemsWithChildren.addAll(
                        getChildrenRecursively(getDataProvider()
                            .fetchChildren(
                                new HierarchicalQuery<>(null, item))
                            .collect(Collectors.toList()), depth - 1));
                }
            });
        return itemsWithChildren;
    }
}
