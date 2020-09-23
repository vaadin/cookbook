package com.vaadin.recipes.recipe.monkeypatchjavascript;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("monkey-patch-java-script")
@Metadata(
    howdoI = "Patch a Vaadin JavaScript Web Component Until Some Real Fix is Accepted",
    description = "This shows how to replace (faulty / buggy / undesired) JavaScript code in a Vaadin Web Component.",
    sourceFiles = "recipe/monkeypatchjavascript/aria-fix-menu-item.js"
)
@JsModule("./recipe/monkeypatchjavascript/aria-fix-menu-item.js")
public class MonkeyPatchJavaScript extends Recipe {

    public MonkeyPatchJavaScript() {
        setId("monkey-patch-javascript");

        setSizeFull();

        MenuBar menuBar = new MenuBar();

        Text selected = new Text("");
        Div message = new Div(new Text("Selected: "), selected);

        MenuItem project = menuBar.addItem("Project");
        MenuItem permissions = menuBar.addItem("Permissions",
                e -> selected.setText("Permissions"));
        menuBar.addItem("Help", e -> selected.setText("Help"));

        Dialog dialog = new Dialog();
        dialog.add(new Text("Close me with the esc-key or an outside click"));
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        MenuItem dlgItem = project.getSubMenu().addItem("Dialog", event -> {
            selected.setText("Dialog");
            dialog.open();
        } );

        // This here is at the core of the patch!
        dlgItem.getElement().setAttribute("aria-haspopup", "dialog");

        project.getSubMenu().addItem("Edit", e -> selected.setText("Edit"));
        MenuItem delete = project.getSubMenu().addItem("Delete",
                e -> selected.setText("Delete"));

        permissions.setEnabled(false);
        delete.setEnabled(false);

        Checkbox adminCheckbox = new Checkbox("View as admin");
        adminCheckbox.addValueChangeListener(e -> {
            permissions.setEnabled(e.getValue());
            delete.setEnabled(e.getValue());
        });

        Text explanation = new Text("Left-click Project, then right-click the Dialog sub-menu item, Inspect it (with your browser's Developer Tools. You should see that the 'aria-haspopup' attribute is set to 'dialog' which was only enabled (as of Vaadin 14.3.7) through a Monkey Patch that swaps out a function called '__itemsRenderer' in the underlying Web Component's JavaScript.");
        add(menuBar, message, adminCheckbox, explanation);
    }
}
