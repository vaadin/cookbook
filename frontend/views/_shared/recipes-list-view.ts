import '@vaadin/checkbox';
import '@vaadin/checkbox-group';
import '@vaadin/details';
import { Details } from '@vaadin/details';
import '@vaadin/text-field';
import { capitalCase } from 'change-case';
import { replaceQueryParameter } from 'Frontend/util.js';
import { html, LitElement, nothing } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import { repeat } from 'lit/directives/repeat.js';
import type { DetailedHTMLProps } from 'react';
import { debounce } from 'ts-debounce';
import Tag from '../../generated/com/vaadin/recipes/recipe/Tag.js';
import { ViewConfig } from './ViewConfig.js';
import css from './recipes-list-view.css?construct';

declare global {
  interface HTMLElementTagNameMap {
    'recipes-list-view': RecipesListView;
  }
}

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'recipes-list-view': DetailedHTMLProps<HTMLAttributes<RecipesListView>, RecipesListView>;
    }
  }
}

@customElement('recipes-list-view')
export class RecipesListView extends LitElement {
  static readonly styles = [css];

  @property()
  accessor filter: string = '';
  @property()
  accessor filterTags: Tag[] = [];
  @property()
  accessor views: Readonly<Record<string, ViewConfig>> = {};

  constructor() {
    super();
    const params = new URLSearchParams(window.location.search);
    const searchParam = params.get('search');
    if (searchParam && searchParam.length > 1) {
      this.filter = searchParam;
    }
  }
  firstUpdated() {
    const details = this.querySelector('.tag-filter') as Details;
    details.opened = window.matchMedia('(min-width: 600px)').matches;
  }

  render() {
    return html`
      <div class="recipes-list-view-header">
        <div class="container-fluid">
          <h4 class="title">Cookbook</h4>

          <vaadin-text-field
            clear-button-visible
            @value-changed="${this.updateFilter}"
            placeholder="How do I..."
            theme="vcom"
            value=${this.filter}
            class="recipes-list-view-header-search form-field"
          >
            <i class="las la-search" aria-hidden="true" slot="prefix"></i>
          </vaadin-text-field>

          <div class="recipes-list-view-header-links">
            <a
              href="https://github.com/vaadin/cookbook/issues/new?assignees=&labels=help+wanted%2C+recipe&projects=&template=recipe.md&title=How+do+I+"
              class="link-with-arrow"
            >
              <span>Missing a recipe? Ask us.</span>
              <div class="icon-wrapper">
                <i class="las la-arrow-right" aria-hidden="true"></i>
              </div>
            </a>
            <br />
            <a href="https://github.com/vaadin/cookbook#vaadin-cookbook" class="link-with-arrow">
              <span>Submit a new recipe</span>
              <div class="icon-wrapper">
                <i class="las la-arrow-right" aria-hidden="true"></i>
              </div>
            </a>
          </div>
        </div>
      </div>

      <div class="recipes-list-container container-fluid">
        <div class="recipes-list-tags">
          <vaadin-details theme="reverse cookbook" opened class="tag-filter">
            <vaadin-details-summary slot="summary">
              <h6>
                Filter<span class="selected-tags">
                  ${this.filterTags.length > 0 ? ': ' : ''} ${this.filterTags.map(this.tagToHumanReadable).join(', ')}
                </span>
              </h6>
            </vaadin-details-summary>
            <vaadin-checkbox-group @value-changed=${this.tagFilterChange}>
              ${Object.values(Tag)
                .sort()
                .map(
                  (tag) => html`
                  <vaadin-checkbox
                    value="${tag}"
                    ?checked=${this.filterTags.includes(tag)}
                    theme="cookbook"
                  ><label slot="label">${this.tagToHumanReadable(tag)}
                    <span
                      class="tag-count"
                      ?hidden=${this.matchCount(tag) === 0}
                      >${this.matchCount(tag)}</span
                    ></vaadin-checkbox
                  ></label>
                `,
                )}
            </vaadin-checkbox-group>
          </vaadin-details>
        </div>
        <ul class="recipes-list">
          ${repeat(
            Object.entries(this.views).filter(([, config]) => this.configMatches(config, this.filter, this.filterTags)),
            ([url]) => url,
            ([
              url,
              {
                title,
                detail: { description, tags },
              },
            ]) =>
              html` <li class="recipe">
                ${title
                  ? html`<h5 class="recipe-title">
                      <a href="${url}">${title?.trim().replace(/^\w/, (c) => c.toUpperCase())}</a>
                    </h5>`
                  : nothing}
                <p class="paragraph-sm recipe-description" ?hidden=${description?.length === 0}>${description}</p>
                <div class="recipe-tags tag-group">
                  ${tags?.map(
                    (tag) =>
                      html`<span class="tag water" @click="${() => this.setFilterTag(tag)}"
                        >${this.tagToHumanReadable(tag)}</span
                      > `,
                  )}
                </div>
              </li>`,
          )}
        </ul>
      </div>
    `;
  }
  tagToHumanReadable(tag: Tag): string {
    return capitalCase(tag).replace(/ /g, '').replace('Flow', 'Vaadin Flow');
  }
  matchCount(tag: Tag): number {
    return Object.values(this.views).filter((config) =>
      this.configMatches(config, this.filter, [...this.filterTags, tag]),
    ).length;
  }

  setFilterTag(tag: Tag) {
    this.filterTags = [tag];
  }

  configMatches({ title, detail: { description, tags } }: ViewConfig, filter: string, filterTags: Tag[]): boolean {
    const summary = title?.toLowerCase() ?? '';
    const _description = description?.toLowerCase() ?? '';
    if (!summary.includes(filter) && !_description.includes(filter)) {
      return false;
    }
    return filterTags.every((tag) => tags?.includes(tag));
  }

  tagFilterChange(e: CustomEvent) {
    this.filterTags = e.detail.value;
  }

  logSearch() {
    if (!this.filter) return;

    if ('haas' in window) {
      //@ts-ignore
      window.haas.tracker.gtm.triggerGAEvent('send', 'event', 'cookbook', 'search', this.filter);
    } else {
      console.log(`Search event: "${this.filter}". GA disabled locally.`);
    }
  }

  #debouncedLog = debounce(this.logSearch, 1000);

  updateFilter(e: CustomEvent) {
    const value = e.detail.value;
    this.filter = value.toLowerCase();

    replaceQueryParameter('search', this.filter);
    this.#debouncedLog();
  }
}
