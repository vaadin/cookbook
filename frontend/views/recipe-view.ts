import { css, customElement, html, LitElement, property } from "lit-element";
import { render, TemplateResult } from "lit-html";
import { recipes } from "../";
import "../code-viewer";
import RecipeInfo from "../generated/com/vaadin/recipes/data/RecipeInfo";
import { Context } from "@vaadin/router";

@customElement("recipe-view")
export class RecipeView extends LitElement {
  @property({ type: Object })
  recipe: RecipeInfo = { howDoI: "", description: "", url: "", tags: [] };

  static get styles() {
    return css`
      :host {
        display: block;
        padding-bottom: var(--space-lg);
      }

      ::slotted([slot="header"]) {
        background-color: var(--color-white);
        padding-top: var(--space-sm);
        padding-bottom: var(--space-sm);
      }

      ::slotted(:not([slot]).container-fluid) {
        display: block;
        min-height: 100px;
        padding-top: var(--space-md);
        padding-bottom: var(--space-md);
      }

      ::slotted(.flow-example) {
        display: flex;
        align-items: center;
        justify-content: center;
      }
    `;
  }

  async onAfterEnter(context: Context) {
    window.scrollTo(0, 0);
    const tag = context.pathname.split("/")[1];
    const recipe = recipes.find((recipe) => recipe.url == tag);
    if (recipe) {
      this.recipe = recipe;
      this.renderLightDom();
      const example = this.querySelector(":not([slot])");
      example?.classList.add("container-fluid");
      if (example?.nodeName.startsWith("flow-container-root")) {
        example.classList.add("flow-example");
      }
    }
  }

  render() {
    return html`
      <slot name="header"></slot>
      <div class="example">
        <slot></slot>
      </div>
      <slot name="code"></slot>
    `;
  }

  renderLightDom() {
    let headerSlot = this.querySelector("[slot=header]");
    if (!headerSlot) {
      headerSlot = document.createElement("div");
      headerSlot.setAttribute("slot", "header");
      headerSlot.classList.add("recipe-view-header");
      this.appendChild(headerSlot);
    }
    render(headerTemplate(this.recipe), headerSlot);

    let codeSlot = this.querySelector("[slot=code]");
    if (!codeSlot) {
      codeSlot = document.createElement("div");
      codeSlot.setAttribute("slot", "code");
      codeSlot.classList.add("recipe-view-code", "container-fluid");
      this.appendChild(codeSlot);
    }
    render(codeTemplate(this.recipe), codeSlot);
  }
}

const headerTemplate: (data: any) => TemplateResult = (
  recipe: RecipeInfo
) => html`
  <style>
    .recipe-view-back-link {
      font-weight: 600;
      text-decoration: none !important;
      margin-left: -1.25em;
    }
    .recipe-view-title {
      margin-bottom: var(--space-xs);
      padding-top: var(--space-sm);
    }
  </style>
  <div class="container-fluid">
    <a href="/" class="recipe-view-back-link">
      <div class="icon-wrapper">
        <i class="las la-arrow-left" aria-hidden="true"></i>
      </div>
      <span>Back to Cookbook</span>
    </a>
    <h3 class="recipe-view-title">
      ${recipe.howDoI.trim().replace(/^\w/, (c) => c.toUpperCase())}
    </h3>
    <p class="paragraph-sm" ?hidden=${recipe.description?.length === 0}>
      ${recipe.description}
    </p>
    ${recipe.tags?.map((tag) => html`<span class="tag">${tag}</span> `)}
  </div>
`;

const codeTemplate: (data: any) => TemplateResult = (recipe: RecipeInfo) =>
  html` <code-viewer .files=${recipe.sourceFiles || []}></code-viewer> `;
