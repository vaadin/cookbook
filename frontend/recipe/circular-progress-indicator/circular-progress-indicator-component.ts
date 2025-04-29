import { LitElement, svg, css } from 'lit';
import { customElement, property } from 'lit/decorators.js';

@customElement('circular-progress-indicator-component')
export class CircularProgressIndicatorComponent extends LitElement {
  @property({ type: Number })
  accessor value: number = 0;
  @property({ type: String })
  accessor color: string = 'blue';

  static get styles() {
    return css`
      :host {
        display: block;
      }
      .background {
        fill: none;
        stroke: black;
        stroke-opacity: 0.05;
      }
      .indicator {
        fill: none;
      }
      text {
        text-anchor: middle;
        font-size: 1.5em;
      }
    `;
  }

  render() {
    const value = Math.min(100, Math.max(0, this.value));

    const dx = Math.sin((value / 100.0) * 2 * 3.14) * 50;
    const dy = -Math.cos((value / 100.0) * 2 * 3.14) * 50 + 50;
    const sweep = value > 50 ? 1 : 0;
    const stroke = 5;

    return svg`
      <svg style="height: 100px" viewbox="0 0 120 120">
        <path
          class="background"
          stroke-width="${stroke}"
          d="M60 10 a 50 50 0 1 0 1 0 Z"
        />
        <path
          class="indicator"
          stroke="${this.color}"
          stroke-width="${stroke}"
          d="M60 10 a 50 50 0 ${sweep} 1 ${dx} ${dy}"
        />
        <text x="60" y="70">${value} %</text>
      </svg>
    `;
  }
}
