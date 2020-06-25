import { css, customElement, html, LitElement } from "lit-element";
import "@vaadin/vaadin-app-layout/theme/lumo/vaadin-app-layout";
import { AppLayoutElement } from "@vaadin/vaadin-app-layout/src/vaadin-app-layout";
import "@vaadin/vaadin-app-layout/vaadin-drawer-toggle";
import "@vaadin/vaadin-tabs/theme/lumo/vaadin-tab";
import "@vaadin/vaadin-tabs/theme/lumo/vaadin-tabs";
import "../all-recipes/all-recipes";

@customElement("main-view")
export class MainEndpoint extends LitElement {
  static get styles() {
    return [
      css`
        :host {
          display: block;
          height: 100%;
        }
      `,
    ];
  }

  render() {
    return html`
      <vaadin-app-layout primary-section="drawer">
        <vaadin-drawer-toggle
          slot="navbar touch-optimized"
        ></vaadin-drawer-toggle>
        <all-recipes slot="drawer"></all-recipes>
        <slot></slot>
      </vaadin-app-layout>
    `;
  }

  private _routerLocationChanged() {
    // @ts-ignore
    AppLayoutElement.dispatchCloseOverlayDrawerEvent();
  }

  connectedCallback() {
    super.connectedCallback();
    window.addEventListener(
      "vaadin-router-location-changed",
      this._routerLocationChanged
    );
  }

  disconnectedCallback() {
    super.disconnectedCallback();
    this.removeEventListener(
      "vaadin-router-location-changed",
      this._routerLocationChanged
    );
  }
}
