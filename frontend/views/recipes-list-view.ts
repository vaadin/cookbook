import { TextFieldElement } from "@vaadin/vaadin-text-field";
import "@vaadin/vaadin-text-field";
import {
  customElement,
  html,
  LitElement,
  property,
  query,
} from "lit-element";
import { repeat } from "lit-html/directives/repeat";
import { recipes } from "../";
import { registerStyles, css } from "@vaadin/vaadin-themable-mixin/register-styles";

registerStyles('vaadin-text-field', css`
  :host([theme~="vcom"]) {
    --lumo-font-family: var(--font-main), Verdana, sans‑serif;
  }

  :host([theme~="vcom"].form-field) {
    margin-bottom: 0 !important;
  }

  :host([theme~="vcom"]) [part="input-field"] {
    height: var(--field-height);
    font-family: var(--font-main),Verdana,sans‑serif;
    font-size: 1rem;
    line-height: 1.1;
    background: var(--field-background-color);
    border: 1px solid var(--field-border-color);
    color: var(--field-value-color);
    padding: var(--space-xs);
    border-radius: var(--roundness-sm);
    transition: all .2s ease-in;
  }

  :host([theme~="vcom"]) [part="input-field"]::after {
    display: none;
  }

  :host([theme~="vcom"]) [part="input-field"]:hover {
    background-color: var(--field-background-color-hover);
    border-color: var(--field-border-color-hover);
  }

  :host([theme~="vcom"][focused]) [part="input-field"] {
    border-color: var(--field-border-color-active);
    box-shadow: var(--elevation-sm);
  }
`);

@customElement("recipes-list-view")
export class RecipesListView extends LitElement {
  @property({ type: String })
  filter: string = "";

  updateFilter = this.doUpdateFilter.bind(this);

  @query("#filterField")
  filterField: TextFieldElement | undefined;

  createRenderRoot() {
    return this;
  }

  render() {
    return html`
      <style>
        recipes-list-view {
          display: block;
          --recipes-filter-column-width: 180px;
        }

        .recipes-list-view-header {
          background-color: var(--color-white);
          position: -webkit-sticky;
          position: sticky;
          top: 0;
          z-index: 1000;
        }

        .recipes-list-view-header > .container-fluid {
          min-height: calc(var(--space-lg));
          padding-top: var(--space-sm);
          padding-bottom: var(--space-sm);
          display: flex;
          align-items: center;
        }

        .recipes-list-view-header .title {
          width: var(--recipes-filter-column-width);
          margin: 0;
          padding: 0;
        }

        .recipes-list-view-header-search {
          flex: auto;
        }

        .recipes-list-view-header-search i.las {
          font-size: 1.25em;
          color: var(--color-graphite);
        }

        .recipes-list-view-header-links {
          margin-left: var(--space-lg);
        }

        .recipes-list-view-header-links a {
          font-weight: 600;
          text-decoration: none !important;
        }

        @media (max-width: 600px) {
          .recipes-list-view-header > .container-fluid {
            flex-wrap: wrap;
          }
          .recipes-list-view-header-links {
            margin: var(--space-sm) 0;
          }
        }

        .recipes-list-container {
          padding-top: var(--space-md);
          padding-bottom: var(--space-xl);
        }

        .recipes-list {
          list-style: none;
          margin: 0;
          padding: 0;
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(15rem, 1fr));
          gap: var(--space-sm);
        }

        .recipes-list .card {
          box-shadow: var(--elevation-sm);
          position: relative;
        }

        .recipes-list .card a {
          color: inherit;
          text-decoration: none;
        }

        .recipes-list .card a::before {
          content: "";
          position: absolute;
          top: 0;
          right: 0;
          bottom: 0;
          left: 0;
        }

        .recipes-list .card .feature-box {
          display: flex;
          flex-direction: column;
          height: 100%;
        }

        .recipes-list .card .feature-box-text {
          flex: auto;
          margin-bottom: var(--space-xs);
        }
      </style>

      <div class="recipes-list-view-header">
        <div class="container-fluid">
          <h4 class="title">Cookbook</h4>

          <vaadin-text-field
            clear-button-visible
            id="filterField"
            @value-changed="${this.updateFilter}"
            placeholder="How do I..."
            theme="vcom"
            class="recipes-list-view-header-search form-field"
            >
            <i class="las la-search" aria-hidden="true" slot="prefix"></i>
          </vaadin-text-field>

          <div class="recipes-list-view-header-links">
            <a href="https://github.com/vaadin/cookbook#vaadin-cookbook" class="link-with-arrow">
              <span>Submit a recipe</span>
              <div class="icon-wrapper">
                <i class="las la-arrow-right" aria-hidden="true"></i>
              </div>
            </a>
          </div>
        </div>
      </div>

      <div class="recipes-list-container container-fluid">
        <div class="recipes-list-tags">
        <!-- TODO -->
        </div>
        <ul class="recipes-list">
          ${repeat(
            recipes.filter((recipe) =>
              recipe.howDoI.toLowerCase().includes(this.filter)
            ),
            (recipe) => recipe.url,
            (recipe) =>
              html`
                <li class="card">
                  <div class="feature-box">
                    <div class="feature-box-text">
                      <h5>
                        <a href="${recipe.url}">${recipe.howDoI.trim().replace(/^\w/, (c) => c.toUpperCase())}</a>
                      </h5>
                    </div>
                    <div class="tags">
                    ${recipe.tags?.map(
                      (tag) => html`<span class="tag water">${tag}</span> `
                    )}
                    </div>
                  </div>
                </li>`
          )}
        </ul>
      </div>
    `;
  }

  doUpdateFilter() {
    this.filter = this.filterField?.value.toLowerCase() || "";
  }
}
