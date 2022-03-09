package com.vaadin.recipes.recipe.distinguisheditablefromnoneditablecells;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("distinguish-editable-from-non-editable-cells")
@Metadata(
    howdoI = "Distinguish editable from non editable cells in a Vaadin Grid",
    description = "How to distinguish editable cells from non editable cells using Grid Pro",
    tags = { Tag.THEME, Tag.GRID, Tag.USABILITY }
)
public class DistinguishEditableFromNonEditableCells extends Recipe {
    public DistinguishEditableFromNonEditableCells() {
      add(new Html("<section><h4>See official Grid Pro documentation</h4><p>The the documentation for Grid Pro has an example how to do this: <a href=\"https://vaadin.com/docs/latest/ds/components/grid-pro/#styling-editable-cells\">Styling Editable Cells</a></p></section>"));
    }
}
