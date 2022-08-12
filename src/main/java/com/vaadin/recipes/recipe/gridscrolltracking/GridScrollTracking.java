package com.vaadin.recipes.recipe.gridscrolltracking;


import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.List;
import java.util.stream.IntStream;

@Route("grid-scroll-tracking")
@Metadata(
        howdoI = "Remember scroll position in a Grid",
        description = "Learn how to make the Grid keep the scroll position when reloading the screen or navigating back from a another screen.",
        tags = {Tag.JAVA, Tag.GRID, Tag.USABILITY}
)
public class GridScrollTracking extends Recipe implements BeforeEnterObserver {

    private static final int DEBOUNCE_MS = 500;
    private static final String INDEX_PARAM_NAME = "i";

    private final Grid<String> grid;

    public GridScrollTracking() {
        grid = new Grid<>();
        grid.addColumn(String::toString).setHeader("A column");
        grid.setItems(IntStream.range(0, 1000).mapToObj(value -> "Row index " + value).toList());

        registerScrollEventListener();

        add(new Button("Go to another screen", e -> getUI().get().navigate("/")));
        add(grid);
    }

    private void registerScrollEventListener() {
        // grid._firstVisibleIndex is not public api of the vaadin grid. I hope this will not be removed.
        grid.getElement().executeJs(
                "this.$.table.addEventListener('scroll', (e) => {\n" +
                        "clearTimeout($0.__scroll_position_timeout);\n" +
                        "$0.__scroll_position_timeout = setTimeout(() => $0.$server.receiveScrollPosition(this._firstVisibleIndex), " + DEBOUNCE_MS + ");\n" +
                        "})",
                this.getElement() // or wherever receiveScrollPosition(index) is implemented in the backend
        );
    }

    @ClientCallable
    public void receiveScrollPosition(int index) {
        getUI().ifPresent(ui -> ui.getPage().getHistory()
                .replaceState(null, "grid-scroll-tracking?" + INDEX_PARAM_NAME + "=" + index)
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        List<String> index = beforeEnterEvent.getLocation().getQueryParameters().getParameters().get(INDEX_PARAM_NAME);
        if (index.size() > 0) {
            grid.scrollToIndex(Integer.parseInt(index.get(0)));
        }
    }
}
