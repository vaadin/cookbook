import { LitElement, html, css, customElement } from 'lit-element';

@customElement('empty-view')
export class EmptyView extends LitElement {
  static get styles() {
    return css`
      :host {
        display: block;
      }
    `;
  }

  render() {
    return html`
      <br />
      Content placeholder
    `;
  }
}
