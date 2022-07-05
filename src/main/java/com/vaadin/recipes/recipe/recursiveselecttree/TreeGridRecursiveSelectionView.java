package com.vaadin.recipes.recipe.recursiveselecttree;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.recipes.recipe.treegridsubtreehighlight.Department;
import com.vaadin.recipes.recipe.treegridsubtreehighlight.DepartmentData;

@Route("tree-grid-recursive-selection")
@Metadata(
    howdoI = "select recursively an item and all its children in a TreeGrid",
    description = "How to select recursively an item and all its children in a tree",
    sourceFiles = {
        "RecursiveSelectTreeGrid.java",
        "../treegridsubtreehighlight/Department.java",
        "../treegridsubtreehighlight/DepartmentData.java",
    },
    tags = { Tag.GRID }
)
public class TreeGridRecursiveSelectionView extends Recipe {

    public TreeGridRecursiveSelectionView() {
        this.setSizeFull();
        createBasicTreeGridUsage();
    }

    private void createBasicTreeGridUsage() {
        DepartmentData departmentData = new DepartmentData();

        TreeGrid<Department> grid = new RecursiveSelectTreeGrid<>();

        grid.setItems(departmentData.getRootDepartments(), departmentData::getChildDepartments);
        grid.addHierarchyColumn(Department::getName).setHeader("Department Name");
        grid.addColumn(Department::getManager).setHeader("Manager");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        add(grid);
    }
}
