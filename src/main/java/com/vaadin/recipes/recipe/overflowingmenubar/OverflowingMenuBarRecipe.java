package com.vaadin.recipes.recipe.overflowingmenubar;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.time.LocalDate;

@Route("overflowing-menubar")
@Metadata(
        howdoI = "Allow MenuBar overflow button",
        description = "MenuBar automatically creates an overflow button when the buttons do not fit into MenuBar's width." +
                " However, if you need a MenuBar inside a HorizontalLayout (f.e. a toolbar) it won't shrink as expected.",
        tags = Tag.LAYOUT
)
public class OverflowingMenuBarRecipe extends Recipe {

    public OverflowingMenuBarRecipe() {
        this.add(new Div(new Text("Simple MenuBar creates an overflow button.")));
        final var widerMenuBar = createWiderMenuBar();
        widerMenuBar.setWidthFull(); // <-- fits to parent, allows shrinking and then growing
        this.add(widerMenuBar);

        /* *** */
        this.add(new Div(new Text("MenuBar inside a HorizontalLayout does not create an overflow button properly.")));
        final var notWorkingMenuBar = createMenuBar();
        notWorkingMenuBar.addThemeName(ButtonVariant.LUMO_ERROR.getVariantName());
        notWorkingMenuBar.setWidthFull(); // <-- Is not enough! Does not shrink.

        final HorizontalLayout notWorkingToolbar = createFullWidthHorizontalLayout(notWorkingMenuBar);
        this.add(notWorkingToolbar);

        /* *** */
        this.add(new Div(new Text("This shrinks the MenuBar when needed and grows it back.")));
        final var menuBar = createMenuBar();
        menuBar.addThemeName(ButtonVariant.LUMO_SUCCESS.getVariantName());
        // 3 em is to accommodate the overflow button (triple dot)
        menuBar.setMinWidth(3, Unit.EM); // <-- !!! needed to change min-width from `auto` so it could shrink !!!

        final HorizontalLayout toolbar = createFullWidthHorizontalLayout(menuBar);
        toolbar.expand(menuBar); // <-- flex-grow so it could grow when upsizing
        // also would work with `menuBar.setWidthFull()` instead of `toolbar.expand(menuBar)`
//         menuBar.setWidthFull();
        this.add(toolbar);
    }

    private HorizontalLayout createFullWidthHorizontalLayout(MenuBar menuBar) {
        final var toolbar = new HorizontalLayout(menuBar);
        toolbar.add(createOtherFields());
        toolbar.setWidthFull(); // <--
        return toolbar;
    }

    private MenuBar createMenuBar() {
        final var menuBar = new MenuBar();
        addMenuItems(menuBar);
        return menuBar;
    }

    private MenuBar createWiderMenuBar() {
        final var menuBar = createMenuBar();
        addMenuItems(menuBar);
        return menuBar;
    }

    private void addMenuItems(MenuBar menuBar) {
        menuBar.addItem("Try");
        menuBar.addItem("resizing");
        menuBar.addItem("the");
        menuBar.addItem("browser");
        menuBar.addItem("window");
    }

    private Component[] createOtherFields() {
        return new Component[]{
                new TextField(null, "Random text"),
                new DatePicker(LocalDate.now())
        };
    }
}
