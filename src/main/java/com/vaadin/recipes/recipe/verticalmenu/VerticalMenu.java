package com.vaadin.recipes.recipe.verticalmenu;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("vertical-menu")
@Metadata(
    howdoI = "Create vertically oriented MenuBar",
    description = "Sometimes it is preferred to have MenuBar vertically oriented instead of horizontal bar.",
    sourceFiles = { "recipe/verticalmenu/vertical-menu.css" },
    tags = { Tag.JAVA }
)
@CssImport(themeFor = "vaadin-menu-bar", value = "./recipe/verticalmenu/vertical-menu.css")
public class VerticalMenu extends Recipe {

    public VerticalMenu() {
        MenuBar menuBar = new MenuBar();

        // Set menubar to be vertical with css
        menuBar.getElement().setAttribute("theme", "menu-vertical");

        menuBar.setWidth("100px");
        MenuItem first = menuBar.addItem(new Div(new Span("First"),VaadinIcon.ANGLE_RIGHT.create()));
        first.getSubMenu().addItem("Sub1 one");
        MenuItem subOne = first.getSubMenu().addItem("Sub two");
        subOne.getSubMenu().addItem("Sub-sub one");
        subOne.getSubMenu().addItem("Sub-sub two");
        subOne.getSubMenu().addItem("Sub-sub three");
        first.getSubMenu().addItem("Sub1 two");
        first.getSubMenu().addItem("Sub1 three");
        menuBar.addItem("Second");
        menuBar.addItem("Third");
        MenuItem fourth = menuBar.addItem(new Div(new Span("Fourth"),VaadinIcon.ANGLE_RIGHT.create()));
        fourth.getSubMenu().addItem("Sub2 one");
        MenuItem subTwo = fourth.getSubMenu().addItem("Sub2 two");
        subTwo.getSubMenu().addItem("Sub-sub one");
        subTwo.getSubMenu().addItem("Sub-sub two");
        subTwo.getSubMenu().addItem("Sub-sub three");
        fourth.getSubMenu().addItem("Sub2 three");
        menuBar.addItem("Fith");
        menuBar.addItem("Sixth");
        menuBar.setOpenOnHover(true);

        // Adjust the opening position with JavaScript
        menuBar.getElement().executeJs("this._subMenu.addEventListener('opened-changed', function(e) {" +
                "const rootMenu = e.target;" +
                "const button = rootMenu._context.target;" +
                "if(!button) return;" +
                "const rect = button.getBoundingClientRect();" +
                "rootMenu.__x = rect.right;" +
                "rootMenu.__y = rect.top;" +
                "rootMenu.__alignOverlayPosition();" +
                "});");
        
        add(menuBar);
    }
}