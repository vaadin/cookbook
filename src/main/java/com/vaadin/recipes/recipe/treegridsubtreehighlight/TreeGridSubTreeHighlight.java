package com.vaadin.recipes.recipe.treegridsubtreehighlight;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("treegrid-subtree-highlight")
@Metadata(howdoI = "How do I highlight the sub tree of the selected parent node", sourceFiles = {
"recipe/treegridsubtreehighlight/treegrid-subtree-highlight.css" })
@CssImport(themeFor = "vaadin-grid", value = "./recipe/treegridsubtreehighlight/treegrid-subtree-highlight.css")
public class TreeGridSubTreeHighlight extends Recipe {

    public TreeGridSubTreeHighlight() {
    	this.setSizeFull();
        createBasicTreeGridUsage();
    }

    private void createBasicTreeGridUsage() {
        DepartmentData departmentData = new DepartmentData();
        TextArea message = new TextArea("");
        message.setHeight("100px");
        message.setReadOnly(true);

        TreeGrid<Department> grid = new TreeGrid<>();

        grid.setItems(departmentData.getRootDepartments(),
                departmentData::getChildDepartments);
        grid.addHierarchyColumn(Department::getName)
                .setHeader("Department Name");
        grid.addColumn(Department::getManager).setHeader("Manager");

        grid.addExpandListener(event -> message.setValue(
                String.format("Expanded %s item(s)", event.getItems().size())
                        + "\n" + message.getValue()));
        grid.addCollapseListener(event -> message.setValue(
                String.format("Collapsed %s item(s)", event.getItems().size())
                        + "\n" + message.getValue()));

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) System.out.println(event.getValue().getName()+" selected");
            grid.getDataProvider().refreshAll();
        });
        
        grid.setClassNameGenerator(department -> {
            if (grid.asSingleSelect().getValue() != null && grid.asSingleSelect().getValue().equals(department.getParent())) {
                System.out.println(department.getName()+" has selected parent");
                return "parent-selected";
            }
        	else return null;
        	
        });
		grid.setHeightByRows(true);
        add(grid);
    }
}
