package com.vaadin.recipes.recipe.treegridsubtreehighlight;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("treegrid-subtree-highlight")
@Metadata(
    howdoI = "Highlight the subtree of a selected parent node",
    description = "How to highlight the sub-tree of a highlighted row in a Vaadin TreeGrid.",
    sourceFiles = {
        "Department.java", "DepartmentData.java", "recipe/treegridsubtreehighlight/treegrid-subtree-highlight.css",
    }
)
@CssImport(themeFor = "vaadin-grid", value = "./recipe/treegridsubtreehighlight/treegrid-subtree-highlight.css")
public class TreeGridSubTreeHighlight extends Recipe {

    private TreeGrid<Department> grid;

    public TreeGridSubTreeHighlight() {
        this.setSizeFull();
        createBasicTreeGridUsage();
    }

    private void createBasicTreeGridUsage() {
        DepartmentData departmentData = new DepartmentData();

        grid = new TreeGrid<>();

        grid.setItems(departmentData.getRootDepartments(), departmentData::getChildDepartments);
        grid.addHierarchyColumn(Department::getName).setHeader("Department Name");
        grid.addColumn(Department::getManager).setHeader("Manager");

        grid
            .asSingleSelect()
            .addValueChangeListener(
                event -> {
                    refreshChildItems(event.getOldValue());
                    refreshChildItems(event.getValue());
                }
            );

        grid.setClassNameGenerator(
            department -> {
                if (
                    grid.asSingleSelect().getValue() != null &&
                    grid.asSingleSelect().getValue().equals(department.getParent())
                ) {
                    return "parent-selected";
                } else return null;
            }
        );
        grid.setAllRowsVisible(true);
        add(grid);
    }

    private void refreshChildItems(Department department) {
        if (department != null) {
            grid.getTreeData().getChildren(department).forEach(child -> {
                grid.getDataProvider().refreshItem(child, false);
            });
         }
    }
}
