import { html } from "lit";

window.myListRenderer = (phones) => {
  return html`
    <div style="display: inline-grid">
      ${phones.map((phone) => html`<span>${phone}</span>`)}
    </div>
  `;
};
