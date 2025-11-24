package com.vaadin.recipes.recipe.tabsheet;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("tabsheet")
@Metadata(
    howdoI = "create a TabSheet component",
    description = "Learn how to change content based on the selected tab in a Vaadin Java app."
)
public class TabSheetView extends Recipe {
    public TabSheetView() {
        // tabsheet component
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        tabSheet.add("Article 1.", new Div("All human beings are born free and equal in dignity and rights. They are endowed with reason and conscience and should act towards one another in a spirit of brotherhood."));
        tabSheet.add("Article 2.", new Div("Everyone is entitled to all the rights and freedoms set forth in this Declaration, without distinction of any kind, such as race, colour, sex, language, religion, political or other opinion, national or social origin, property, birth or other status. Furthermore, no distinction shall be made on the basis of the political, jurisdictional or international status of the country or territory to which a person belongs, whether it be independent, trust, non-self-governing or under any other limitation of sovereignty."));
        tabSheet.add("Article 3.", new Div("Everyone has the right to life, liberty and security of person."));
        tabSheet.add("Article 4.", new Div("No one shall be held in slavery or servitude; slavery and the slave trade shall be prohibited in all their forms."));
        tabSheet.add("Article 5.", new Div("No one shall be subjected to torture or to cruel, inhuman or degrading treatment or punishment."));
        tabSheet.add("Article 6.", new Div("Everyone has the right to recognition everywhere as a person before the law."));

        add(tabSheet);
    }
}
