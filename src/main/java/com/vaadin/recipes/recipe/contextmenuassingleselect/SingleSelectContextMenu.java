package com.vaadin.recipes.recipe.contextmenuassingleselect;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.dom.Element;

import java.util.*;

public  class SingleSelectContextMenu<T> extends AbstractCompositeField<ContextMenu, SingleSelectContextMenu<T>, T> implements HasItems<T>, HasElement, HasTheme {

    private final ContextMenu contextMenu = new ContextMenu();

    private final List<T> items = new ArrayList<>();
    private final Map<T, MenuItem> itemMap = new LinkedHashMap<>();

    private ItemRenderer<T> renderer = item -> new Text(item.toString());

    public SingleSelectContextMenu(T defaultValue) {
        super(defaultValue);
    }

    public void setTarget(Component target) {
        this.getContent().setTarget(target);
    }

    @Override
    public Element getElement() {
        return this.getContent().getElement();
    }

    @Override
    public ContextMenu getContent() {
        this.contextMenu.setOpenOnClick(true);
        return this.contextMenu;
    }

    @Override
    protected void setPresentationValue(T newPresentationValue) {
        this.itemMap.entrySet().stream().filter(tMenuItemEntry -> tMenuItemEntry.getKey().equals(newPresentationValue))
                .findFirst().ifPresent(tMenuItemEntry -> {
            this.setChecked(tMenuItemEntry.getValue());
        });
    }

    @Override
    public void setItems(Collection<T> items) {
        this.items.clear();
        this.items.addAll(items);
        this.rebuildMenu();
    }

    public void setRenderer(ItemRenderer<T> renderer) {
        this.renderer = renderer;
        this.rebuildMenu();
    }

    private void rebuildMenu() {
        this.contextMenu.removeAll();
        this.itemMap.clear();
        this.items.forEach(item -> {
            var menuItem = this.contextMenu.addItem(this.renderer.render(item), this::onItemClicked);
            this.itemMap.put(item, menuItem);
            menuItem.setCheckable(true);
        });
    }

    private void setChecked(MenuItem menuItem) {
        this.itemMap.forEach((key, value) -> {
            value.setChecked(value == menuItem);
        });
    }

    private void onItemClicked(ClickEvent<MenuItem> menuItemClickEvent) {
        this.itemMap.forEach((key, value) -> {
            if (menuItemClickEvent.getSource() == value) {
                this.setValue(key);
            }
        });
        this.setChecked(menuItemClickEvent.getSource());
    }

    @FunctionalInterface
    public interface ItemRenderer<T> {
        Component render(T item);
    }
}
