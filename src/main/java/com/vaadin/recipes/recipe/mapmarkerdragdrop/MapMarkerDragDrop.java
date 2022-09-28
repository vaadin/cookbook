package com.vaadin.recipes.recipe.mapmarkerdragdrop;

import com.vaadin.flow.component.map.MapVariant;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.util.HashMap;

@Route("map-marker-drag-drop")
@Metadata(
        howdoI = "Drag&drop markers on a Vaadin Map",
        description = "Adds an interaction for moving markers and an event that notifies about changes to the markers coordinates",
        sourceFiles = { "CustomMap.java", "recipe/map-marker-drag-drop/map-marker-drag-drop.js" }
)
public class MapMarkerDragDrop extends Recipe {
    public MapMarkerDragDrop() {
        CustomMap map = new CustomMap();
        map.setCenter(new Coordinate(-3.9622641509434, 42.03954321121262));
        map.addThemeVariants(MapVariant.BORDERLESS);
        add(map);

        // Add markers for cities
        HashMap<MarkerFeature, String> cities = new HashMap<>();
        cities.put(new MarkerFeature(new Coordinate(13.404954, 52.520008)), "Berlin");
        cities.put(new MarkerFeature(new Coordinate(114.162813, 22.279328)), "Hong Kong");
        cities.put(new MarkerFeature(new Coordinate(37.617298, 55.755825)), "Moscow");
        cities.put(new MarkerFeature(new Coordinate(-74.005974, 40.712776)), "New York");
        cities.put(new MarkerFeature(new Coordinate(-43.2093727, -22.9110137)), "Rio de Janeiro");
        cities.keySet().forEach(map.getFeatureLayer()::addFeature);

        // Register event listener for when markers are dropped after a translate interaction
        map.addMarkerDropListener(event -> {
            MarkerFeature marker = event.getMarker();
            if (marker != null) {
                // Update marker position
                marker.setCoordinates(event.getCoordinate());
                // Provide some visual feedback
                Notification.show(String.format("Moved city %s to %s%n", cities.get(marker), event.getCoordinate()));
            }
        });
    }
}
