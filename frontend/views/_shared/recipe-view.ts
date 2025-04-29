import { html, LitElement, nothing } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import type { DetailedHTMLProps } from 'react';
import '../../code-viewer.js';
import css from './recipe-view.css?construct';
import { ViewConfig } from './ViewConfig.js';

declare global {
  interface HTMLElementTagNameMap {
    'recipe-view': RecipeView;
  }
}

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'recipe-view': DetailedHTMLProps<HTMLAttributes<RecipeView>, RecipeView>;
    }
  }
}

function headerTemplate({ title, detail: { description, tags } }: ViewConfig) {
  return html`<header class="recipe-view-header">
    <div class="container-fluid">
      <a href="/" class="recipe-view-back-link">
        <div class="icon-wrapper">
          <i class="las la-arrow-left" aria-hidden="true"></i>
        </div>
        <span>Back to Cookbook</span>
      </a>
      ${title
        ? html`<h3 class="recipe-view-title">${title.trim().replace(/^\w/, (c) => c.toUpperCase())}</h3>`
        : nothing}
      <p class="paragraph-sm" ?hidden=${description?.length === 0}>${description}</p>
      <span class="tag-group"> ${tags?.map((tag) => html`<span class="tag">${tag}</span> `)} </span>
    </div>
  </header>`;
}

@customElement('recipe-view')
export class RecipeView extends LitElement {
  static readonly styles = [css];

  @property()
  accessor config: ViewConfig | undefined;

  render() {
    return html`
      ${this.config ? headerTemplate(this.config) : nothing}
      <div class="example">
        <slot></slot>
      </div>
      ${this.config?.detail?.sourceFiles?.length
        ? html`<code-viewer
            class="recipe-view-code container-fluid"
            .files=${this.config.detail.sourceFiles}
          ></code-viewer>`
        : nothing}
    `;
  }
}
