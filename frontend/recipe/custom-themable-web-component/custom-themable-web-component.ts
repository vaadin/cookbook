import { PolymerElement, html } from "@polymer/polymer";
import { ThemableMixin } from "@vaadin/vaadin-themable-mixin";

export class CustomThemableWebComponent extends ThemableMixin(PolymerElement) {
  static get template() {
    return html`<div>The contents of the div</div>`;
  }
  static get is() {
    return "custom-themable-web-component";
  }
}
customElements.define(
  CustomThemableWebComponent.is,
  CustomThemableWebComponent
);
