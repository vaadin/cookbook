import { css, html, nothing } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import type { DetailedHTMLProps } from 'react';
import { Recipe, recipeInfo } from '../recipe';

declare global {
  interface HTMLElementTagNameMap {
    'geo-location': GeoLocation;
  }
}

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'geo-location': DetailedHTMLProps<HTMLAttributes<GeoLocation>, GeoLocation>;
    }
  }
}

@recipeInfo({
  url: 'geo-location',
  howDoI: 'Get the position (geo location) of the user',
  description:
    'Show how you watch the geo location of the user and keep track of the coordinates, altitude, direction and speed.',
})
@customElement('geo-location')
export class GeoLocation extends Recipe {
  @property({ type: Object })
  accessor #position: GeolocationPosition | undefined;

  @property({ type: Object })
  accessor #error: GeolocationPositionError | undefined;

  #watchId = -1;

  render() {
    return html`
      <section>
        <h2>Position</h2>
        ${this.#position
          ? html`
              <p>Latitude: ${this.#position?.coords.latitude} [± ${this.#position?.coords.accuracy} meters]</p>
              <p>Longitude: ${this.#position?.coords.longitude} [± ${this.#position?.coords.accuracy} meters]</p>
              <p>
                Altitude:
                ${this.#position?.coords.altitude
                  ? html`
                      ${this.#position?.coords.altitude} meters [± ${this.#position?.coords.altitudeAccuracy} meters]
                    `
                  : 'not available'}
              </p>
              <p>
                Heading to:
                ${this.#position?.coords.heading ? html` ${this.#position?.coords.heading} ` : 'not available'}
              </p>
              <p>
                Speed:
                ${this.#position?.coords.speed
                  ? html` ${this.#position?.coords.speed} meters per second `
                  : 'not available'}
              </p>
              <p>Updated at: ${new Date(this.#position?.timestamp!)}</p>
            `
          : `Updating...`}
      </section>

      ${this.#error
        ? html`
            <section>
              <h2>Error</h2>
              <p>Code: ${this.#error?.code}</p>
              <p>Message: ${this.#error?.message}</p>
            </section>
          `
        : nothing}

      <div class="next-step">
        See the <a href="leaflet-map">showing a map</a> recipe to see how to show a location on a map.
      </div>
    `;
  }

  connectedCallback() {
    super.connectedCallback();

    // https://developer.mozilla.org/en-US/docs/Web/API/PositionOptions
    const options: PositionOptions = {
      enableHighAccuracy: true,
      timeout: 10 * 1000, // 10 seconds
      maximumAge: 100, // 100ms old values are still OK
    };

    // https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/watchPosition
    this.#watchId = navigator.geolocation.watchPosition(
      (position: GeolocationPosition) => {
        this.#position = position;
        this.#error = undefined;
      },
      (error: GeolocationPositionError) => {
        this.#position = undefined;
        this.#error = error;
      },
      options,
    );
  }

  disconnectedCallback() {
    navigator.geolocation.clearWatch(this.#watchId);
    super.disconnectedCallback();
  }

  static styles = css`
    :host {
      display: block;
      margin: 20px;
    }

    p {
      margin: 0;
    }

    .next-step {
      margin-top: 3em;
    }
  `;
}
