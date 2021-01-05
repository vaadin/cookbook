package com.vaadin.recipes.recipe.distinguisheditablefromnoneditablecells;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.Arrays;
import java.util.List;

@Route("distinguish-editable-from-non-editable-cells")
@Metadata(
        howdoI = "Distinguish editable from non editable cells",
        description = "How to distinguish editable cells from non editable cells using GridPro",
        sourceFiles = { "recipe/distinguisheditablefromnoneditablecells/distinguish-editable-from-non-editable-cells.css" },
        tags = {Tag.THEME, Tag.GRID, Tag.USABILITY}
)
@CssImport(
        themeFor = "vaadin-grid-pro",
        value = "./recipe/distinguisheditablefromnoneditablecells/distinguish-editable-from-non-editable-cells.css"
)
public class DistinguishEditableFromNonEditableCells extends Recipe {

    public DistinguishEditableFromNonEditableCells() {

        GridPro<ChangeMe> grid = new GridPro<>();
        grid.setItems(new ListDataProvider<>(createData()));

        // Column "editable"
        grid.addEditColumn(ChangeMe::getEditable)
                .text(ChangeMe::setEditable)
                .setHeader("Editable");

        // Column "non editable"
        grid.addColumn(ChangeMe::getNonEditable)
                .setHeader("Non editable");

        add(grid);
    }

    private List<ChangeMe> createData() {
        return Arrays.asList(
                new ChangeMe("Editable 1", "Non Editable 1"),
                new ChangeMe("Editable 2", "Non Editable 2"),
                new ChangeMe("Editable 3", "Non Editable 3")
        );
    }

    public static class ChangeMe {
        private String editable;
        private final String nonEditable;

        public ChangeMe(String editable, String nonEditable) {
            this.editable = editable;
            this.nonEditable = nonEditable;
        }

        public String getEditable() {
            return editable;
        }

        public String getNonEditable() {
            return nonEditable;
        }

        public void setEditable(String editable) {
            this.editable = editable;
        }
    }
}
