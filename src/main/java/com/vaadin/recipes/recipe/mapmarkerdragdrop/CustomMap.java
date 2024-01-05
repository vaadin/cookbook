package com.vaadin.recipes.recipe.mapmarkerdragdrop;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.map.configuration.layer.VectorLayer;
import com.vaadin.flow.component.map.configuration.source.VectorSource;
import com.vaadin.flow.shared.Registration;

import elemental.json.JsonArray;

@JsModule("./recipe/map-marker-drag-drop/map-marker-drag-drop.js")
public class CustomMap extends Map {
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Enable marker drag&drop for this map instance, see `map-marker-drag-drop.js`
        getElement().executeJs("window.Vaadin.Flow.setupMarkerDragDrop($0)", getElement());
    }

    public Registration addMarkerDropListener(ComponentEventListener<MapMarkerDropEvent> listener) {
        return addListener(MapMarkerDropEvent.class, listener);
    }

    @DomEvent("map-marker-drop")
    public static class MapMarkerDropEvent extends ComponentEvent<CustomMap> {
        private final Coordinate coordinate;
        private final MarkerFeature marker;

        public MapMarkerDropEvent(CustomMap source, boolean fromClient,
                                  @EventData("event.detail.markerId") String markerId,
                                  @EventData("event.detail.coordinate") JsonArray coordinate) {
            super(source, fromClient);

            this.coordinate = new Coordinate(coordinate.getNumber(0), coordinate.getNumber(1));
            this.marker = findMarker(source, markerId);
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        public MarkerFeature getMarker() {
            return marker;
        }

        private MarkerFeature findMarker(CustomMap map, String markerId) {
            return (MarkerFeature) map.getConfiguration().getLayers().stream()
                    .filter(layer -> layer instanceof VectorLayer)
                    .map(layer -> (VectorSource) ((VectorLayer) layer).getSource())
                    .flatMap(source -> source.getFeatures().stream())
                    .filter(feature -> feature instanceof MarkerFeature && feature.getId().equals(markerId))
                    .findFirst().orElse(null);
        }
    }
}
