package com.vaadin.recipes.recipe.scrolltoitemintreegrid;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataCommunicator;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchyMapper;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("scroll-to-item-in-tree-grid")
@Metadata(howdoI = "Scroll to a specific item when expanding nodes", tags = { Tag.GRID })
public class ScrollToItemInTreeGrid extends Recipe {

    public static class ScrollTreeGrid<T> extends TreeGrid<T> {

        public ScrollTreeGrid(Class<T> beanType) {
            super(beanType);
            initScrollWhenReady();
        }

        public ScrollTreeGrid() {
            super();
            initScrollWhenReady();
        }

        public ScrollTreeGrid(HierarchicalDataProvider<T, ?> dataProvider) {
            super(dataProvider);
            initScrollWhenReady();
        }

        /** The method for scrolling to an item. Takes into account lazy loading nature of grid and does the scroll
         * operation only until the grid has finished loading data
         *
         * @param item the item where to scroll to
         */
        public void scrollToItem(T item){
            int index = getIndexForItem(item);
            if(index>=0){
                this.getElement().executeJs("this.scrollWhenReady($0, true);", index);
            }
        }

        /** This is a method for getting the row index of an item in a treegrid. This works but is prone to break in the future versions due to
         * its usage of reflection to access private methods to get access to the index.
         *
         * @param <T>
         */
        private int getIndexForItem(T item) {
            HierarchicalDataCommunicator<T> dataCommunicator = super.getDataCommunicator();
            Method getHierarchyMapper = null;
            try {
                getHierarchyMapper = HierarchicalDataCommunicator.class.getDeclaredMethod("getHierarchyMapper");
                getHierarchyMapper.setAccessible(true);
                HierarchyMapper<T, ?> mapper = (HierarchyMapper) getHierarchyMapper.invoke(dataCommunicator);
                return mapper.getIndex(item);
            } catch (Exception e) {
                //TODO: handle
                e.printStackTrace();
            }
            return -1;
        }

        private void initScrollWhenReady() {
            runBeforeClientResponse(ui -> getElement().executeJs("this.scrollWhenReady = function(index, firstCall){" +
                    "if(this.loading || firstCall) {var that = this; setTimeout(function(){that.scrollWhenReady(index, false);}, 200);}" +
                    "        else {this.scrollToIndex(index);}" +
                    "};"));
        }


        private void runBeforeClientResponse(SerializableConsumer<UI> command) {
            getElement().getNode().runWhenAttached(ui -> ui
                    .beforeClientResponse(this, context -> command.accept(ui)));
        }
    }

    /** Data entiry to use in the treegrid
     *
     */
    public static class Person {
        private String name;
        private Person parent;

        public Person(){}

        public Person(String name, Person parent) {
            this.name = name;
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public Person getParent() {
            return parent;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setParent(Person parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    /** Demo
     *
     */
    public ScrollToItemInTreeGrid(){
        ScrollTreeGrid<Person> grid = new ScrollTreeGrid<>();
        grid.addHierarchyColumn(p -> p.getName()).setHeader("Name");
        Person grandDad = new Person("Granddad", null);
        Person dad = new Person("Dad", grandDad);
        List<Person> people = new ArrayList<>();
        people.add(grandDad);
        people.add(dad);
        Person firstChild = new Person("First Child", dad);
        people.add(firstChild);
        for(int i=2;i<100;i++){
            Person child = new Person("Child "+i, dad);
            people.add(child);
        }
        Person lastChild = new Person("Last Child", dad);
        people.add(lastChild);
        people.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));
        add(grid);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(new Button("Expand and scroll to first", e -> {grid.expand(grandDad, dad); grid.scrollToItem(firstChild);}));
        buttons.add(new Button("Expand and scroll to last", e -> {grid.expand(grandDad,dad); grid.scrollToItem(lastChild);}));
        add(buttons);
    }
}
