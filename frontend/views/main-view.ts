import { AppLayoutElement } from "@vaadin/vaadin-app-layout/src/vaadin-app-layout";
import "@vaadin/vaadin-app-layout/theme/lumo/vaadin-app-layout";
import "@vaadin/vaadin-app-layout/vaadin-drawer-toggle";
import "@vaadin/vaadin-split-layout";
import "@vaadin/vaadin-tabs";
import { css, customElement, html, LitElement, property, internalProperty } from "lit-element";
import "../all-recipes";
import "../code-viewer";
import RecipeInfo from "../generated/com/vaadin/recipes/data/RecipeInfo";
import { tsRecipeRoutes } from "../ts-routes";
import * as RecipeEndpoint from "../generated/RecipeEndpoint";
import { router } from "..";

@customElement("main-view")
export class MainView extends LitElement {
  @property({ type: Object })
  recipe: RecipeInfo = { howDoI: "", url: "", tags: [] };

  @internalProperty()
  private recipes: RecipeInfo[] = [];

  static get styles() {
    return css`
      :host {
        display: block;
        height: 100%;
      }
      .examplewrapper,
      code-viewer {
        flex: 1;
      }
      .layout {
        height: 100%;
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

  constructor(){
    super();
    this._routerLocationChanged = this._routerLocationChanged.bind(this);
  }

  render() {
    return html`
      <vaadin-app-layout primary-section="drawer">
        <vaadin-drawer-toggle
          slot="navbar touch-optimized"
        ></vaadin-drawer-toggle>
        <span slot="navbar touch-optimized">
          ${this.recipe.tags?.map(
            (tag) => html`<span class="tag">${tag}</span>`
          )}<span class="title">How do I ${this.recipe.howDoI}</span>
        </span>
        <all-recipes slot="drawer" .recipes=${this.recipes}></all-recipes>
        <vaadin-split-layout class="layout" orientation="vertical">
          <div class="examplewrapper">
            <div>${this.recipe.description}</div>
            <slot></slot>
          </div>
          <code-viewer .files=${this.recipe.sourceFiles || []}></code-viewer>
        </vaadin-split-layout>
      </vaadin-app-layout>
    `;
  }

  private _routerLocationChanged() {
    AppLayoutElement.dispatchCloseOverlayDrawerEvent();
    this._updateCurrentRecipe(router.location.pathname);
  }

  async connectedCallback() {
    super.connectedCallback();
    window.addEventListener(
      "vaadin-router-location-changed",
      this._routerLocationChanged
    );

    this.recipes = [...this.recipes, 
      ...tsRecipeRoutes.map((route) => route.info),
      ...(await RecipeEndpoint.list())]
      .sort((a, b) => a.howDoI < b.howDoI ? -1 : a.howDoI == b.howDoI ? 0 : 1);
    
    this._updateCurrentRecipe(router.location.pathname);
  }

  disconnectedCallback() {
    super.disconnectedCallback();
    this.removeEventListener(
      "vaadin-router-location-changed",
      this._routerLocationChanged
    );
  }


  _updateCurrentRecipe(path: string) {  
    if (path.includes("-")) {
      const tag = path.substr(path.lastIndexOf("/") + 1);
      const recipe = this.recipes.find((recipe) => recipe.url == tag);
      if (recipe) {
        this.recipe = recipe;
      }
    }
  }
}
