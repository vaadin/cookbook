package com.vaadin.recipes.recipe.autocompletecb;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@SuppressWarnings("serial")
@Route(value = "autocomplete-combobox")
@Metadata(
        howdoI = "Create an autocomplete field based on ComboBox",
        description = "Using combobox is an easy way of implementing a typical autocomplete field with filter highlighting support and dropdown toggle removed",
        sourceFiles = {"recipe/autocomplete-combobox/styles.css" },
        tags = { Tag.FLOW }
)
@CssImport(value = "./recipe/autocomplete-combobox/styles.css")
public class AutocompleteComboBox extends Recipe {

	List<String> options = Arrays.asList("Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune");
    String partialValue = "";
	
    public AutocompleteComboBox() {
    	ComboBox<String> cb = new ComboBox<String>();
    	DataProvider<String, String> dataProvider =
    		    DataProvider.fromFilteringCallbacks(
    		        query -> {
    		            int offset = query.getOffset();
    		            int limit = query.getLimit();
    		            List<String> persons = findOptions(query.getFilter().get())
    		                    .stream().skip(offset).limit(limit).toList();
    		            return persons.stream();
    		        },
    		        query -> findOptions(query.getFilter().get()).size());
    	cb.setItems(dataProvider);
    	cb.setLabel("Enter a planet");
    	cb.setClearButtonVisible(true);
    	cb.setPlaceholder("search...");
    	cb.getElement().addEventListener("keydown", ev->{
    		cb.getElement().executeJs("return this.inputElement.value").then(ev2->partialValue = ev2.asString());
    	});
    	cb.setRenderer(LitRenderer.<String>of("<span .innerHTML=${item.html}></span>").withProperty("html", i->highlightOccurrences(i, cb)));

        add(cb);
    }
    
    private Object highlightOccurrences(String value, ComboBox<String> cb) {
    	String filter = partialValue;
		return !Strings.isBlank(filter)?value.replaceAll(filter, "<b class='mark'>" + filter + "</b>"):value;
	}

	private List<String> findOptions(String text) {
    	if (text.isEmpty()) {
            return Arrays.asList();
        } else {
        	return options.stream()
        			.filter(option -> option.toLowerCase().contains(text.toLowerCase()))
        			.collect(Collectors.toList());
        }
    }
 
}
