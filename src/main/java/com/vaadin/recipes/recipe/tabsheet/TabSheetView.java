package com.vaadin.recipes.recipe.tabsheet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.util.LinkedHashMap;
import java.util.Map;

@Route("tabsheet")
@Metadata(
    howdoI = "create a TabSheet component",
    description = "Learn how to change content based on the selected tab in a Vaadin Java app."
)
public class TabSheetView extends Recipe {
    /**
     * Contents of the TabSheet.
     */
    private final Map<Tab, Component> contents = new LinkedHashMap<>();

    public TabSheetView() {
        this.buildContentAndTabs();

        // tabs component
        final Tabs tabs = new Tabs();

        // display area
        final Div display = new Div();
        display.setSizeFull();

        // show components on the screen
        this.add(tabs, display);

        // update display area whenever tab selection changes
        tabs.addSelectedChangeListener(
            event -> {
                // remove old contents, if there was a previously selected tab
                if (event.getPreviousTab() != null) display.remove(this.contents.get(event.getPreviousTab()));
                // add new contents, if there is a currently selected tab
                if (event.getSelectedTab() != null) display.add(this.contents.get(event.getSelectedTab()));
            }
        );

        // add tabs to the component
        tabs.add(this.contents.keySet().toArray(new Tab[0]));
    }

    /**
     * Builds contents to be displayed and the corresponding tabs. Uses the first
     * articles from <a href=
     * "https://www.un.org/en/universal-declaration-human-rights/index.html">the
     * Universal Declaration of Human Rights</a>.
     */
    private void buildContentAndTabs() {
        final String[] data = new String[] {
            "Article 1.",
            "All human beings are born free and equal in dignity and rights. They are endowed with reason and conscience and should act towards one another in a spirit of brotherhood.",
            "Article 2.",
            "Everyone is entitled to all the rights and freedoms set forth in this Declaration, without distinction of any kind, such as race, colour, sex, language, religion, political or other opinion, national or social origin, property, birth or other status. Furthermore, no distinction shall be made on the basis of the political, jurisdictional or international status of the country or territory to which a person belongs, whether it be independent, trust, non-self-governing or under any other limitation of sovereignty.",
            "Article 3.",
            "Everyone has the right to life, liberty and security of person.",
            "Article 4.",
            "No one shall be held in slavery or servitude; slavery and the slave trade shall be prohibited in all their forms.",
            "Article 5.",
            "No one shall be subjected to torture or to cruel, inhuman or degrading treatment or punishment.",
            "Article 6.",
            "Everyone has the right to recognition everywhere as a person before the law.",
        };
        // create tabs and matching contents
        for (int zmp1 = 0; zmp1 < data.length; zmp1 += 2) this.contents.put(
                new Tab(data[zmp1]),
                new Span(data[zmp1 + 1])
            );
    }
}
