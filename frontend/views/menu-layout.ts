import { css, customElement, html, LitElement, property } from "lit-element";
import { router } from "../index";
import "@vaadin/vaadin-text-field";

@customElement("menu-layout")
export class MenuLayout extends LitElement {
  @property({ type: Object }) location = router.location;

  static get styles() {
    return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
  }
  render() {
    return html``;
  }
}
