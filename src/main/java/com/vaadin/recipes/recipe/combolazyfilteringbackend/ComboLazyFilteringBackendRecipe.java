package com.vaadin.recipes.recipe.combolazyfilteringbackend;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@SuppressWarnings("serial")
@Route("combo-lazy-filtering-backend")
@Metadata(
  howdoI = "Implement ComboBox with Filtering and Lazy loading",
  description = "A simple example that shows how to implement a ComboBox that will trigger a backend call " +
		  		"after typing a value on it, that will only fetch paginated results lazily"
)
public class ComboLazyFilteringBackendRecipe extends Recipe {
	
    List<String> countries = Arrays.asList(Locale.getISOCountries()).stream().map(isoCode->new Locale("es",isoCode).getDisplayCountry()).toList();

	public ComboLazyFilteringBackendRecipe() {
        ComboBox<String> comboBox = new ComboBox<>("Select a country");

        comboBox.setItems(query->{
            Optional<String> filter = query.getFilter();
            int limit = query.getLimit();
            int offset = query.getOffset();
            return simulateBackendQuery(filter,limit,offset);
        });

        add(comboBox);
	}

	private Stream<String> simulateBackendQuery(Optional<String> filter, long limit, long offset) {
		return  countries.stream()
	            .filter(item -> !filter.isPresent() || item.contains(filter.get())).skip(offset).limit(limit);
	}
    
}