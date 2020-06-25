import { css, customElement, html, LitElement } from "lit-element";

@customElement("intro-view")
export class IntroView extends LitElement {
  static get styles() {
    return css`
      :host {
        display: block;
        margin: 1em;
      }
    `;
  }
  render() {
    return html` <div>Hello</div> `;
  }
}
