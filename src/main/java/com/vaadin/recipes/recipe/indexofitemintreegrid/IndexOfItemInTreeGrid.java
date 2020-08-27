package com.vaadin.recipes.recipe.indexofitemintreegrid;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataCommunicator;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchyMapper;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("index-of-item-in-treeGrid")
@Metadata(howdoI = "add the missing index of item method in the TreeGrid")
public class IndexOfItemInTreeGrid  extends Recipe {

    private Span indexLabel;

    public static class CustomTreeGrid<T> extends TreeGrid<T> {

       /** This is the new method for getting the row index of an item in a treegrid. This works but is prone to break in the future versions due to
          * its usage of reflection to access private methods to get access to the index.
          *
          * @param <T>
        */
        public int getIndexForItem(T item) {
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
    public IndexOfItemInTreeGrid(){
        CustomTreeGrid<Person> grid = new CustomTreeGrid<>();
        grid.setWidth("90%");
        grid.addHierarchyColumn(p -> p.getName()).setHeader("Name");
        Person grandDad = new Person("Granddad", null);
        Person dad = new Person("Dad", grandDad);
        List<Person> people = new ArrayList<>();
        people.add(grandDad);
        people.add(dad);
        for(int i=0;i<10;i++){
            Person child = new Person("Child "+i, dad);
            people.add(child);
        }
        Person brother = new Person("Brother", grandDad);
        people.add(brother);
        people.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));
        grid.addCollapseListener(e -> updateIndexInfo(grid.getIndexForItem(brother)));
        grid.addExpandListener(e -> updateIndexInfo(grid.getIndexForItem(brother)));
        add(grid);
        // add label that we update on collapse and expand that shows the current index of the brother
        this.indexLabel=new Span();
        this.indexLabel.getElement().setAttribute("style", "padding-left:10px");
        this.indexLabel.setText("foo"+grid.getTreeData().getRootItems().size());
        add(indexLabel);
    }

    private void updateIndexInfo(int indexForBrother){
        indexLabel.setText("The row for the Brother is currently: "+indexForBrother);
    }
}
