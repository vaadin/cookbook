package com.vaadin.recipes.recipe.filetree;

import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Route("file-tree")
@Metadata(
    howdoI = "Show files and folders recursively in a TreeGrid",
    description = "Learn how to display file system folders easily using a Vaadin TreeGrid."
)
public class FileTree extends Recipe {
    private static final File rootFile = new File(System.getProperty("user.home") + File.separator + "Desktop");

    private static class Tree<T> extends TreeGrid<T> {

        Tree(ValueProvider<T, ?> valueProvider) {
            Column<T> only = addHierarchyColumn(valueProvider);
            only.setAutoWidth(true);
        }
    }

    public FileTree() {
        Tree<File> filesTree = new Tree<>(File::getName);
        filesTree.setItems(Collections.singleton(rootFile), this::getFiles);
        filesTree
            .getColumns()
            .stream()
            .findFirst()
            .ifPresent(
                fileColumn -> {
                    fileColumn.setComparator(Comparator.naturalOrder());
                    GridSortOrder<File> sortOrder = new GridSortOrder<>(fileColumn, SortDirection.ASCENDING);
                    filesTree.sort(Collections.singletonList(sortOrder));
                    filesTree.setWidthFull();
                    filesTree.setHeight("300px");
                    filesTree
                        .asSingleSelect()
                        .addValueChangeListener(
                            event -> {
                                File file = event.getValue();
                                if (file != null && file.isFile()) { // deselecting: file == null
                                    // don't do anything
                                } else {
                                    // do something
                                }
                            }
                        );
                }
            );

        this.add(filesTree);
        setSizeFull();
    }

    private List<File> getFiles(File parent) {
        if (parent.isDirectory()) {
            File[] list = parent.listFiles();
            if (list != null) {
                return Arrays.asList(list);
            }
        }

        return Collections.emptyList();
    }
}
