package com.vaadin.recipes.recipe.responsivegrid;

import java.util.Collection;

import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("responsive-grid")
@Metadata(
    howdoI = "Make a responsive Grid that has a different set of columns depending on the browser width.",
    description = "This recipe demonstrates example of a responsove Grid. It has a different set of columns depending on the browser width. When the browser is narrow one column which combines all the info is shown, and on the wide browser four columns is shown. Resize the browser to see it in action.",
    sourceFiles = { "Person.java" },
    tags = { Tag.GRID, Tag.FLOW, Tag.USABILITY }
)
public class ResponsiveGrid extends Recipe {

    private Registration listener;
    private int breakpointPx = 1000;
    private Grid<Person> grid;

    public ResponsiveGrid() {
        grid = createGrid();
        add(grid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
                adjustVisibleGridColumns(grid, event.getWidth());
        }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
                adjustVisibleGridColumns(grid, browserWidth);
        }));
    }
 
    
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
        
    private Grid<Person> createGrid() {
        Grid<Person> grid = new Grid<>();
        grid.addComponentColumn(item -> {
                VerticalLayout vl = new VerticalLayout();
                vl.add(new Html("<div><b>Name:</b> "+item.getName()+"</div>"));
                vl.add(new Html("<div><b>Title:</b> "+item.getTitle()+"</div>"));
                vl.add(new Html("<div><b>Email:</b> "+item.getEmail()+"</div>"));
                vl.add(new Html("<div><b>DoB:</b> "+item.getDate().toString()+"</div>"));
                return vl;
        }).setHeader("Persons").setVisible(false);
        grid.addColumn(Person::getName).setHeader("Name");
        grid.addColumn(Person::getTitle).setHeader("Title");
        grid.addColumn(Person::getEmail).setHeader("Email");
        grid.addColumn(Person::getDate).setHeader("Date of Birth");
        grid.setItems(createExamplePersons(20));
        return grid;
    }

    private void adjustVisibleGridColumns(Grid<Person> grid, int width) {
        boolean[] visibleCols;
        // Change which columns are visible depending on browser width
        if (width > breakpointPx) {
            visibleCols = new boolean[]{false, true, true, true, true};
        } else {
            visibleCols = new boolean[]{true, false, false, false, false};
        }
        for (int c = 0; c < visibleCols.length; c++) {
            grid.getColumns().get(c).setVisible(visibleCols[c]);
        }
    }

    private Collection<Person> createExamplePersons(int count) {
        ExampleDataGenerator<Person> generator = new ExampleDataGenerator<>(
                Person.class, 123);
        generator.setData(Person::setName, DataType.FULL_NAME);
        generator.setData(Person::setTitle, DataType.OCCUPATION);
        generator.setData(Person::setEmail, DataType.EMAIL);
        generator.setData(Person::setDate, DataType.DATE_OF_BIRTH);
        return generator.create(count);
    }    


}
