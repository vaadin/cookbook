import { css, html, LitElement } from "lit";
import { customElement, property } from "lit/decorators.js";
import { render, TemplateResult } from "lit";
import { recipes, router } from "..";
import "../code-viewer";
import RecipeInfo from "../generated/com/vaadin/recipes/data/RecipeInfo";

@customElement("recipe-view")
export class RecipeView extends LitElement {
  @property({ type: Object })
  recipe: RecipeInfo = { howDoI: "", description: "", url: "", tags: [] };

  routeListener = this.onRouteChange.bind(this);

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

  onRouteChange(e: any) {
    this.onPathChange(e.detail.location.pathname);
  }

  onPathChange(pathname: string) {
    window.scrollTo(0, 0);
    const tag = pathname.split("/")[1];
    const recipe = recipes.find((recipe) => recipe.url == tag);
    if (recipe) {
      this.recipe = recipe;
      this.renderLightDom();
    }
  }

  connectedCallback() {
    super.connectedCallback();
    // Using vaadin-router-location-changed as "onAfterEnter" is not invoked when only the child view changes
    window.addEventListener(
      "vaadin-router-location-changed",
      this.routeListener
    );
    this.onPathChange(router.location.pathname);
  }

  disconnectedCallback() {
    super.disconnectedCallback();
    window.removeEventListener(
      "vaadin-router-location-changed",
      this.routeListener
    );
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
    render(headerTemplate(this.recipe), headerSlot as HTMLElement);

    let codeSlot = this.querySelector("[slot=code]");
    if (!codeSlot) {
      codeSlot = document.createElement("div");
      codeSlot.setAttribute("slot", "code");
      codeSlot.classList.add("recipe-view-code", "container-fluid");
      this.appendChild(codeSlot);
    }
    render(codeTemplate(this.recipe), codeSlot as HTMLElement);
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

    @media (max-width: 600px) {
      .recipe-view-back-link {
        margin-left: 0;
      }
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
    <span class="tag-group">
      ${recipe.tags?.map((tag) => html`<span class="tag">${tag}</span> `)}
    </span>
  </div>
`;

const codeTemplate: (data: any) => TemplateResult = (recipe: RecipeInfo) =>
  html` <code-viewer .files=${recipe.sourceFiles || []}></code-viewer> `;
