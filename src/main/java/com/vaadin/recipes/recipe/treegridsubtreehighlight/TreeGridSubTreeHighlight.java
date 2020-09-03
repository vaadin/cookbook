package com.vaadin.recipes.recipe.treegridsubtreehighlight;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("treegrid-subtree-highlight")
@Metadata(howdoI = "How do I highlight the sub tree of the selected parent node", sourceFiles = { "Department.java",
		"DepartmentData.java", "recipe/treegridsubtreehighlight/treegrid-subtree-highlight.css" })
@CssImport(themeFor = "vaadin-grid", value = "./recipe/treegridsubtreehighlight/treegrid-subtree-highlight.css")
public class TreeGridSubTreeHighlight extends Recipe {

    public TreeGridSubTreeHighlight() {
    	this.setSizeFull();
        createBasicTreeGridUsage();
    }

    private void createBasicTreeGridUsage() {
        DepartmentData departmentData = new DepartmentData();

        TreeGrid<Department> grid = new TreeGrid<>();

        grid.setItems(departmentData.getRootDepartments(),
                departmentData::getChildDepartments);
        grid.addHierarchyColumn(Department::getName)
                .setHeader("Department Name");
        grid.addColumn(Department::getManager).setHeader("Manager");

        grid.asSingleSelect().addValueChangeListener(event -> {
            grid.getDataProvider().refreshAll();
        });
        
        grid.setClassNameGenerator(department -> {
            if (grid.asSingleSelect().getValue() != null && grid.asSingleSelect().getValue().equals(department.getParent())) {
                return "parent-selected";
            }
        	else return null;
        	
        });
		grid.setHeightByRows(true);
        add(grid);
    }
}
