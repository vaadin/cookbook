package com.vaadin.recipes.recipe.treegridwithicons;

import java.util.Optional;

import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
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

		TreeGrid<FileItem> treegrid = new TreeGrid<>();

		treegrid.setItems(data.getRootFiles(), data::getChildFiles);

		treegrid.addColumn(
				TemplateRenderer.<FileItem> of("<vaadin-grid-tree-toggle "
						+ "leaf='[[item.leaf]]' expanded='{{expanded}}' level='[[level]]'>"
						+ "<vaadin-icon icon='[[item.icon]]'></vaadin-icon>&nbsp;&nbsp;"
						+ "[[item.name]]"
						+ "</vaadin-grid-tree-toggle>")
						.withProperty("leaf", item -> !treegrid.getDataCommunicator().hasChildren(item))
				.withProperty("icon", FileItem::getIcon)
				.withProperty("name", FileItem::getName))
			.setHeader("Name");
		treegrid.addColumn(FileItem::getLastModified).setHeader("Last Modified");
		treegrid.addColumn(item -> Optional.ofNullable(item.getSize()).map(size -> size / 1024 + " KB").orElse(null)).setHeader("Size");


		treegrid.setHeightByRows(true);
		treegrid.expandRecursively(data.getRootFiles(), 2);
		add(treegrid);
    }

}
