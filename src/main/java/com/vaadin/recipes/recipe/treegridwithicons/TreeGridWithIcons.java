package com.vaadin.recipes.recipe.treegridwithicons;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("tree-grid-with-icons")
@Metadata(
  howdoI = "Display icons in TreeGrid hierarchy column",
  description = "Display icons in the hierarchy column of a Vaadin TreeGrid.",
  sourceFiles = { "FileItem.java", "FileItemData.java" }
)
public class TreeGridWithIcons extends Recipe {

	public TreeGridWithIcons() {
        setSizeFull();
        createBasicTreeGridUsage();
    }

    private void createBasicTreeGridUsage() {
        FileItemData data = new FileItemData();

        IconTreeGrid treegrid = new IconTreeGrid();

        treegrid.setItems(data.getRootFiles(), data::getChildFiles);

        treegrid.addColumn(FileItem::getLastModified)
                .setHeader("Last Modified");
        treegrid.addColumn(item -> Optional.ofNullable(item.getSize())
                .map(size -> size / 1024 + " KB").orElse(null))
                .setHeader("Size");

        treegrid.setAllRowsVisible(true);
        treegrid.expandRecursively(data.getRootFiles(), 2);
        add(treegrid);
    }

    public class IconTreeGrid extends TreeGrid<FileItem> {

        public IconTreeGrid() {
            addColumn(LitRenderer.<FileItem> of("<vaadin-grid-tree-toggle "
                    + "leaf=${item.leaf} .expanded=${model.expanded} .level=${model.level}>"
                    + "<vaadin-icon icon='${item.icon}'></vaadin-icon>&nbsp;&nbsp;"
                    + "${item.name}" + "</vaadin-grid-tree-toggle>")
                    .withProperty("leaf",
                            item -> !getDataCommunicator().hasChildren(item))
                    .withProperty("icon", FileItem::getIcon)
                    .withProperty("name", FileItem::getName)
                    .withFunction("onClick", item -> {
                        if (getDataCommunicator().hasChildren(item)) {
                            if (isExpanded(item)) {
                                collapse(List.of(item), true);
                            } else {
                                expand(List.of(item), true);
                            }
                        }
                    })).setHeader("Name");
        }
    }
}
