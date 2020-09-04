import { css, customElement, html, LitElement, property } from "lit-element";
import { render, TemplateResult } from 'lit-html';
import "../code-viewer";
import RecipeInfo from "../generated/com/vaadin/recipes/data/RecipeInfo";
import { recipes } from "../";

@customElement("recipe-view")
export class RecipeView extends LitElement {
  @property({ type: Object })
  recipe: RecipeInfo = { howDoI: "", url: "", tags: [] };

  static get styles() {
    return css`
      :host {
        display: block;
      }

      .tag {
        padding: 0.3em;
        margin-right: 0.3em;
        font-size: var(--lumo-font-size-s);
        background: lightgrey;
        border-radius: 4px;
      }
    `;
  }

  renderLightDom() {
    const template: (data: any) => TemplateResult = (recipe: RecipeInfo) => html`
      <div slot="header">
        <h3>${recipe.howDoI}</h3>
        <div>${recipe.description}</div>
        ${recipe.tags?.map(
          (tag) => html`<span class="tag">${tag}</span>`
        )}
      </div>
    `;

    let headerSlot = this.querySelector('[slot="header"]');
    if (!headerSlot) {
      headerSlot = document.createElement('div');
      headerSlot.setAttribute('slot', 'header');
      this.appendChild(headerSlot);
    }
    const result = template(this.recipe);
    render(result, headerSlot);
  }

  onAfterEnter(context:any) {
    const tag = context.pathname.substr(context.pathname.lastIndexOf("/") + 1);
    const recipe = recipes.find((recipe) => recipe.url == tag);
    if (recipe) {
      this.recipe = recipe;
      this.renderLightDom();
    }
  }

  render() {
    return html`
      <slot name="header"></slot>
      <div class="examplewrapper">
        <slot></slot>
      </div>
      <code-viewer .files=${this.recipe.sourceFiles || []}></code-viewer>
    `;
  }
}
