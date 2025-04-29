import '@vaadin/number-field';
import '@vaadin/radio-group';
import { css, html } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import type { DetailedHTMLProps } from 'react';
import { Recipe, recipeInfo } from '../recipe.js';
import './circular-progress-indicator-component';

declare global {
  interface HTMLElementTagNameMap {
    'circular-progress-indicator': CircularProgressIndicator;
  }
}

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'circular-progress-indicator': DetailedHTMLProps<
        HTMLAttributes<CircularProgressIndicator>,
        CircularProgressIndicator
      >;
    }
  }
}

@recipeInfo({
  url: 'circular-progress-indicator',
  howDoI: 'Show progress as a circular indicator',
  description: 'Learn to create a circular progress indicator component with LitElement in a Vaadin client-side view.',
  sourceFiles: ['circular-progress-indicator-component.ts'],
})
@customElement('circular-progress-indicator')
export class CircularProgressIndicator extends Recipe {
  @property({ type: Number })
  accessor value: number = 25;
  @property({ type: String })
  accessor color: string = 'blue';

  static get styles() {
    return css`
      :host {
        display: flex;
        flex-direction: column;
      }
    `;
  }
  render() {
    return html`
      <circular-progress-indicator-component
        value="${this.value}"
        color=${this.color}
      ></circular-progress-indicator-component>
      <vaadin-number-field
        @value-changed=${(e: any) => (this.value = e.target.value)}
        max="100"
        min="0"
        label="Value"
        .value=${this.value}
      ></vaadin-number-field>
      <vaadin-radio-group
        @value-changed=${(e: any) => {
          this.color = e.target.value;
        }}
      >
        <vaadin-radio-button checked value="#f00">red</vaadin-radio-button>
        <vaadin-radio-button value="#0f0">green</vaadin-radio-button>
        <vaadin-radio-button value="#00f">blue</vaadin-radio-button>
      </vaadin-radio-group>
    `;
  }
}
