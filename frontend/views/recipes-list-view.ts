import { TextFieldElement } from "@vaadin/vaadin-text-field";
import "@vaadin/vaadin-text-field";
import "@vaadin/vaadin-details";
import "@vaadin/vaadin-checkbox";
import "@vaadin/vaadin-checkbox/vaadin-checkbox-group";
import { customElement, html, LitElement, property, query } from "lit-element";
import { repeat } from "lit-html/directives/repeat";
import { recipes } from "../";
import {
  registerStyles,
  css,
} from "@vaadin/vaadin-themable-mixin/register-styles";

registerStyles(
  "vaadin-text-field",
  css`
    :host([theme~="vcom"]) {
      --lumo-font-family: var(--font-main), Verdana, sans‑serif;
    }

    :host([theme~="vcom"].form-field) {
      margin-bottom: 0 !important;
    }

    :host([theme~="vcom"]) [part="input-field"] {
      height: var(--field-height);
      font-family: var(--font-main), Verdana, sans‑serif;
      font-size: 1rem;
      line-height: 1.1;
      background: var(--field-background-color);
      border: 1px solid var(--field-border-color);
      color: var(--field-value-color);
      padding: var(--space-xs);
      border-radius: var(--roundness-sm);
      transition: all 0.2s ease-in;
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
  `
);

registerStyles(
  "vaadin-details",
  css`
    [part="summary-content"] {
      min-width: 0;
    }

    :host([theme~="cookbook"]) [part="summary"] {
      padding: 0;
    }
  `
);

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
          --recipes-list-view-header-height: 80px;
        }

        .recipes-list-view-header {
          background-color: var(--color-white);
          position: -webkit-sticky;
          position: sticky;
          top: 0;
          z-index: 1000;
          height: var(--recipes-list-view-header-height);
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
          flex: none;
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

        .recipes-list-container {
          display: flex;
          align-items: flex-start;
          padding-top: var(--space-md);
          padding-bottom: var(--space-xl);
        }

        .recipes-list-tags {
          width: var(--recipes-filter-column-width);
          box-sizing: border-box;
          padding-right: var(--space-md);
          flex: none;
          position: -webkit-sticky;
          position: sticky;
          top: var(--recipes-list-view-header-height);
          z-index: 1000;
          background-color: var(--color-alloy-lighter);
          max-height: calc(100vh - var(--recipes-list-view-header-height));
          overflow: auto;
        }

        .recipes-list-tags vaadin-details {
          margin: 0;
        }

        .recipes-list-tags h6 {
          margin: 0;
          padding: 0;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .recipes-list-tags .selected-tags {
          font-weight: var(--text-weight-regular);
        }

        .recipes-list-tags vaadin-details[opened] .selected-tags {
          display: none;
        }

        .recipes-list-tags vaadin-checkbox-group,
        .recipes-list-tags vaadin-checkbox {
          font-family: inherit;
          font-size: var(--text-size-sm);
          color: var(--color-graphite);
        }

        .recipes-list-tags vaadin-checkbox {
          display: block;
        }

        .recipes-list {
          list-style: none;
          margin: 0;
          padding: 0;
        }

        .recipe-title {
          margin-bottom: 0;
        }

        .recipe:first-child .recipe-title {
          padding-top: 0;
        }

        p.recipe-description {
          margin-bottom: 0;
        }

        .recipe a {
          color: inherit;
          text-decoration: none;
        }

        @media (max-width: 600px) {
          recipes-list-view {
            --recipes-filter-column-width: auto;
            --recipes-list-view-header-height: 118px;
          }

          .recipes-list-view-header > .container-fluid {
            flex-wrap: wrap;
          }

          .recipes-list-view-header-search {
            order: 1;
            margin-top: var(--space-xs);
            min-width: 50vw;
          }

          .recipes-list-view-header-links {
            margin-left: auto;
          }

          .recipes-list-tags {
            padding-right: 0;
            border-bottom: 1px solid var(--color-alloy-darker);
            padding: var(--space-xs) 0;
            margin-bottom: var(--space-md);
          }

          .recipes-list-container {
            flex-direction: column;
            align-items: stretch;
            padding-top: 0;
          }
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
            <a
              href="https://github.com/vaadin/cookbook#vaadin-cookbook"
              class="link-with-arrow"
            >
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
          <!--TODO: collapse when viewport is small, and show selected ones in the summary (“All” or “Java, TypeScript, Lorem” if a subset is selected) -->
          <vaadin-details theme="reverse cookbook" opened>
            <h6 slot="summary">Tags<span class="selected-tags">: All</span></h6>
            <vaadin-checkbox-group>
              <vaadin-checkbox checked>Java</vaadin-checkbox>
              <vaadin-checkbox checked>TypeScript</vaadin-checkbox>
              <vaadin-checkbox checked>Lorem</vaadin-checkbox>
              <vaadin-checkbox checked>Ipsum</vaadin-checkbox>
              <vaadin-checkbox checked>Dolor</vaadin-checkbox>
              <vaadin-checkbox checked>Sit</vaadin-checkbox>
              <vaadin-checkbox checked>Amet</vaadin-checkbox>
              <vaadin-checkbox checked>Consectetur</vaadin-checkbox>
              <vaadin-checkbox checked>Adipiscising</vaadin-checkbox>
              <vaadin-checkbox checked>Elit</vaadin-checkbox>
              <vaadin-checkbox checked>Sed do</vaadin-checkbox>
              <vaadin-checkbox checked>Eiusmod</vaadin-checkbox>
              <vaadin-checkbox checked>Tempor</vaadin-checkbox>
            </vaadin-checkbox-group>
          </vaadin-details>
        </div>
        <ul class="recipes-list">
          ${repeat(
            recipes.filter((recipe) =>
              recipe.howDoI.toLowerCase().includes(this.filter)
            ),
            (recipe) => recipe.url,
            (recipe) =>
              html` <li class="recipe">
                <h5 class="recipe-title">
                  <a href="${recipe.url}"
                    >${recipe.howDoI
                      .trim()
                      .replace(/^\w/, (c) => c.toUpperCase())}</a
                  >
                </h5>
                <p
                  class="paragraph-sm recipe-description"
                  ?hidden=${recipe.description?.length === 0}
                >
                  ${recipe.description}
                </p>
                <div class="recipe-tags">
                  ${recipe.tags?.map(
                    (tag) => html`<span class="tag water">${tag}</span> `
                  )}
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
