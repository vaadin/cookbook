package com.vaadin.recipes.recipe.stickyvirtuallist;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Route("sticky-virtual-list")
@Metadata(
        howdoI = "create a virtual list with sticky headers",
        description = "A virtual list that can display headers with sticky positioning."
)
public class StickyVirtualListView extends Recipe {
    public static class StickyVirtualList<T> extends VirtualList<T> {
        public StickyVirtualList() {
            super();
            addAttachListener(event -> getElement().executeJs("""
                const observe = (el, opt, cb) => {
                    new MutationObserver((m) => [...m].forEach(cb)).observe(el, opt);
                };
                const updateChild = (el) => {
                    let style = el.style;
                    if (!style.transform) return;
                    let translateY = style.transform.match(/translateY\\((.*?)\\)/)[1];
                    if (style.getPropertyValue('--virtual-list-y-offset') !== translateY) {
                        style.setProperty('--virtual-list-y-offset', translateY);
                    }
                };
                const observeChild = (el) => {
                    observe(el, { attributesList: ["style"], attributeOldValue: true, }, (mutation) => {
                        updateChild(el);
                    });
                    updateChild(el);
                };
                let list = this;
                observe(list, { childList: true }, (mutation) => {
                    if (mutation.type === 'childList') {
                        for (let node of mutation.addedNodes) {
                            if (node.nodeType === Node.ELEMENT_NODE) {
                                observeChild(node);
                            }
                        }
                    }
                });
                let children = list.children;
                for (var i = 0; i < children.length; i++) {
                    observeChild(children[i]);
                }
            """));
        }

        public static void makeSticky(HasStyle component, String top) {
            if (component == null)
                return;
            if (top == null || top.isEmpty())
                top = "0px";
            component.getStyle().setPosition(Style.Position.STICKY);
            component.getStyle().setTop("calc(" + top + " - var(--virtual-list-y-offset))");
        }

        public static void makeSticky(HasStyle component) {
            makeSticky(component, null);
        }
    }

    public static class Item {
        public String headerText;
        public String contentText;
    }

    public static class ItemComponent extends VerticalLayout {
        Div headerText = new Div();
        Div contentText = new Div();

        public ItemComponent() {
            // Add sticky positioning to header
            StickyVirtualList.makeSticky(headerText);

            headerText.getStyle().setPadding("5px")
                    .setPaddingLeft("10px")
                    .setBackgroundColor("#ade7ff")
                    .setColor("black");
            headerText.setWidthFull();
            add(headerText, contentText);
        }

        public void setItem(Item item) {
            headerText.setText(item.headerText);
            contentText.setText(item.contentText);
        }
    }

    public StickyVirtualListView() {
        setHeight("500px");
        setWidthFull();
        var list = new StickyVirtualList<Item>();
        list.setSizeFull();
        list.setItems(getItems());
        list.setRenderer(new ComponentRenderer<>(item -> {
            var itemComponent = new ItemComponent();
            itemComponent.setItem(item);
            return itemComponent;
        }, (itemComponent, item) -> {
            ((ItemComponent) itemComponent).setItem(item);
            return itemComponent;
        }));

        this.add(list);
    }

    private List<Item> getItems() {
        var items = new ArrayList<Item>();
        var end = LocalDate.now();
        var start = end.minusMonths(50);
        var formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (var current = end; current.isAfter(start); current = current.minusMonths(1)) {
            var item = new Item();
            item.headerText = current.format(formatter);
            item.contentText = new Random().ints(500, 3, 9)
                    .mapToObj(numChars -> new Random().ints(numChars, 97, 123)
                            .collect(StringBuilder::new, (sb, codePoint) -> sb.append((char) codePoint), StringBuilder::append)
                            .toString().trim()).collect(Collectors.joining(" "));
            items.add(item);
        }
        return items;
    }


}