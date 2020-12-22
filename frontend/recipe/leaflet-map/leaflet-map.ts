import { css, customElement, html, property, unsafeCSS } from "lit-element";
import { Recipe, recipeInfo } from "../recipe";
//@ts-ignore
import { map, tileLayer } from "leaflet/dist/leaflet-src.esm.js";
import leafletStyles from "leaflet/dist/leaflet.css";

@recipeInfo({
  url: "leaflet-map",
  howDoI: "Show a map",
  description:
    "Learn how to show a map around a given position with a Vaadin TypeScript client-side view.",
})
@customElement("leaflet-map")
export class LeafletMap extends Recipe {
  @property({ type: Object })
  private position = {
    latitude: 60.4508,
    longitude: 22.2649,
  };

  render() {
    return html`
      <div id="map"></div>
      <p>
        See the <a href="geo-location">geo location</a> recipe to see how to get
        the current user location.
      </p>
    `;
  }

  firstUpdated(_changedProperties: any) {
    super.firstUpdated(_changedProperties);

    // Check more details from the Leaflet web site
    // https://leafletjs.com/examples.html
    const leafmap = map(this.renderRoot.querySelector("#map"), {
      center: [this.position.latitude, this.position.longitude],
      zoom: 13,
    });

    // Get your own access token from https://www.mapbox.com/studio/account/tokens/
    const accessToken =
      "pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
    tileLayer(
      `https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=${accessToken}`,
      {
        attribution:
          'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        id: "mapbox/streets-v11",
        tileSize: 512,
        zoomOffset: -1,
        accessToken: accessToken,
      }
    ).addTo(leafmap);

    // force Leaflet to recalculate the map size when rendered inside a shadow DOM
    setTimeout(() => leafmap.invalidateSize(), 0);
  }

  static styles = [
    css`
      :host {
        display: block;
        margin: 20px;
      }

      #map {
        height: 300px;
      }
    `,
    unsafeCSS(leafletStyles),
  ];
}
