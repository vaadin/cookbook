package com.vaadin.recipes.recipe.gridcontextmenucell;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.List;
import java.util.Optional;

@Route("grid-context-menu-for-cell")
@Metadata(
		howdoI = "Create a Dynamic Context Menu for specific Grid cells",
		description = "Use a custom event listener to save clicked on column and generate a context menu specific to the clicked cell.",
		tags = { Tag.GRID, Tag.FLOW, Tag.JAVA }
)
public class GridContextMenuForCell extends Recipe {
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_CITY = "city";
	private static final String COLUMN_COUNTRY = "country";

	private String clickedColumn;

	public GridContextMenuForCell() {
		setSizeFull();

		var grid = new Grid<>(Person.class, false);
		grid.addColumn(Person::name).setHeader("Name").setId(COLUMN_NAME);
		grid.addColumn(Person::city).setHeader("City").setId(COLUMN_CITY);
		grid.addColumn(Person::country).setHeader("Country").setId(COLUMN_COUNTRY);
		grid.setItems(List.of(
				new Person("Alice Johnson", "New York", "USA"),
				new Person("Bob Smith", "London", "UK"),
				new Person("Carlos Rivera", "Madrid", "Spain"),
				new Person("Diana Chen", "Shanghai", "China"),
				new Person("Eva Müller", "Berlin", "Germany")
		));

		addContextMenuWithDomEvents(grid);

		add(grid);
	}

	private void addContextMenuWithDomEvents(Grid<Person> grid) {
		var contextMenu = grid.addContextMenu();

		// Add an event listener that sets the clicked column variable before a context menu is shown
		var expression = "function(){const col=element.getEventContext(event).column;return col ? col.id : '';}()";
		grid.getElement().addEventListener("contextmenu", event -> {
			clickedColumn = event.getEventData().get(expression).asString();
		}).addEventData(expression);

		// Generate context menu items based on clicked column and row
		contextMenu.setDynamicContentHandler(person -> {
			var item = Optional.ofNullable(person);
			var columnId = clickedColumn;
			contextMenu.removeAll();
			if (COLUMN_NAME.equals(columnId)) {
				addNameContextMenuItems(contextMenu, item);
			} else if (COLUMN_CITY.equals(columnId)) {
				addCityContextMenuItems(contextMenu, item);
			} else if (COLUMN_COUNTRY.equals(columnId)) {
				addCountryContextMenuItems(contextMenu, item);
			}
			return true;
		});
	}

	private void addNameContextMenuItems(GridContextMenu<Person> contextMenu, Optional<Person> item) {
		var name = item.map(Person::name).orElse("");
		contextMenu.addItem("Add as contact", event2 -> {
			Notification.show(name + " added as contact");
		});
		contextMenu.addItem("Call", event2 -> {
			Notification.show("Calling " + name);
		});
	}

	private void addCityContextMenuItems(GridContextMenu<Person> contextMenu, Optional<Person> item) {
		var city = item.map(Person::city).orElse("");
		contextMenu.addItem("Add to favorites", event2 -> {
			Notification.show(city + " added to favorites");
		});
		contextMenu.addItem("Destroy", event2 -> {
			Notification.show(city + " destroyed");
		});
	}

	private void addCountryContextMenuItems(GridContextMenu<Person> contextMenu, Optional<Person> item) {
		var country = item.map(Person::country).orElse("");
		contextMenu.addItem("Negotiate a trade deal with " + country, event2 -> {
			Notification .show(country + " accepted the trade deal.");
		});
		contextMenu.addItem("Invade", event2 -> {
			Notification.show("Another war started with " + country);
		});
	}

	public record Person(String name, String city, String country) {}

}
