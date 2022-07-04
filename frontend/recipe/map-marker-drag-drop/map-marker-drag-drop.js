import Translate from 'ol/interaction/Translate';

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.setupMarkerDragDrop = function (map) {
  // Add interaction for moving markers to the map element's OpenLayers configuration
  const translate = new Translate();
  map.configuration.addInteraction(translate);

  // Dispatch a custom DOM event when a marker stops being moved
  translate.on('translateend', event => {
    // Get single feature from event
    const feature = event.features.item(0);
    if (!feature) return;
    // Get updated feature coordinates
    // This assumes that the feature is using a point based geometry,
    // which is the case for marker features
    const coordinate = feature.getGeometry().getCoordinates();

    const customEvent = new CustomEvent('map-marker-drop', {
      detail: {
        markerId: feature.id,
        coordinate,
      }
    });
    map.dispatchEvent(customEvent);
  });
};
