import { Checkbox, CheckboxGroup } from '@vaadin/react-components';
import { Details } from '@vaadin/react-components/Details.js';
import { DetailsSummary } from '@vaadin/react-components/DetailsSummary.js';
import { Icon, InlineIcon } from 'Frontend/views/_shared/icons.js';
import React, { useState, createContext, useContext } from 'react';
import { debounce } from 'ts-debounce';
import { capitalCase } from 'change-case';
import { replaceQueryParameter } from 'Frontend/util.js';
import css from './recipes-list-view.module.css';
import type { ViewConfig } from './ViewConfig.js';
import Tag from 'Frontend/generated/com/vaadin/recipes/recipe/Tag.js';
import { TextField } from '@vaadin/react-components/TextField.js';

type ContextValue = Readonly<{
  views: readonly ViewConfig[];
  filter: string;
  filterTags: readonly Tag[];
  updateFilter(value: string): void;
  updateFilterTags(...tags: Tag[]): void;
}>;

const Context = createContext<ContextValue>({
  views: [],
  filter: '',
  filterTags: [],
  updateFilter() {},
  updateFilterTags() {},
});

type GitHubLinkProps = Readonly<{
  children: string;
  href: string;
}>;

function ThirdPartyLink({ children, href }: GitHubLinkProps) {
  return (
    <a href={href} className="link-with-arrow">
      <span>{children}</span>
      <Icon>arrow-right</Icon>
    </a>
  );
}

function RecipesListHeader() {
  const { filter, updateFilter } = useContext(Context);
  return (
    <div className={css.header}>
      <div className="container-fluid">
        <h4 className={css.title}>Cookbook</h4>
        <TextField
          clearButtonVisible
          placeholder="How do I..."
          value={filter}
          onChange={(e) => updateFilter(e.target.value)}
          className={`${css.headerSearch} form-field`}
        >
          <InlineIcon slot="prefix">search</InlineIcon>
        </TextField>
        <div className={css.headerLinks}>
          <ThirdPartyLink href="https://github.com/vaadin/cookbook/issues/new?assignees=&labels=help+wanted%2C+recipe&projects=&template=recipe.md&title=How+do+I+">
            Missing a recipe? Ask us.
          </ThirdPartyLink>
          <ThirdPartyLink href="https://github.com/vaadin/cookbook#vaadin-cookbook">
            Missing a recipe? Ask us.
          </ThirdPartyLink>
        </div>
      </div>
    </div>
  );
}

const tagMap = new Map(
  Object.values(Tag)
    .sort()
    .map((tag) => [tag, capitalCase(tag).replace(/ /g, '').replace('Flow', 'Vaadin Flow')]),
);

function configMatches(
  { title, detail: { description, tags } = {} }: ViewConfig,
  filter: string,
  filterTags: Tag[],
): boolean {
  const summary = title?.toLowerCase() ?? '';
  const _description = description?.toLowerCase() ?? '';
  if (!summary.includes(filter) && !_description.includes(filter)) {
    return false;
  }
  return filterTags.every((tag) => tags?.includes(tag));
}

type TagItemProps = Readonly<{
  tag: Tag;
  children: string;
}>;

function TagItem({ children, tag }: TagItemProps) {
  const { views, filter, filterTags } = useContext(Context);

  const matchCount = Object.values(views).filter((config) =>
    configMatches(config, filter, [...filterTags, tag]),
  ).length;

  return (
    <Checkbox value={tag} checked={filterTags.includes(tag)}>
      <label slot="label">{children}</label>
      <span className={css.tagCount} hidden={!matchCount}>
        {matchCount}
      </span>
    </Checkbox>
  );
}

type TagListProps = Readonly<{
  isOpened: boolean;
}>;

function TagList({ isOpened }: TagListProps) {
  const { filterTags } = useContext(Context);

  return (
    <div className={css.tags}>
      <Details theme="reverse cookbook" opened={isOpened} className={css.tagFilter}>
        <DetailsSummary slot="summary">
          <h6>
            Filter
            <span className={css.selectedTags}>
              {filterTags.length > 0 ? ': ' : ''}
              {Array.from(tagMap.values(), ([, humanReadableTag]) => humanReadableTag).join(', ')}
            </span>
          </h6>
        </DetailsSummary>
        <CheckboxGroup>
          {Array.from(tagMap.entries(), ([tag, readableTag]) => (
            <TagItem key={tag} tag={tag}>
              {readableTag}
            </TagItem>
          ))}
        </CheckboxGroup>
      </Details>
    </div>
  );
}

type ViewRecordProps = Readonly<{
  url: string;
  config: ViewConfig;
}>;

function ViewRecord({ url, config: { title, detail: { description, tags } = {} } }: ViewRecordProps) {
  const { updateFilterTags } = useContext(Context);

  return (
    <li key={url} className={css.recipe}>
      {title && (
        <h5 className={css.recipeTitle}>
          <a href={url}>{title.trim().replace(/^\w/, (c) => c.toUpperCase())}</a>
        </h5>
      )}
      <p className={`${css.recipeDescription} paragraph-sm`} hidden={!description?.length}>
        {description}
      </p>
      <div className={`${css.recipeTags} tag-group`}>
        {tags?.map((tag) => (
          <span key={tag} className="tag water" onClick={() => updateFilterTags(tag)}>
            {tagMap.get(tag)}
          </span>
        ))}
      </div>
    </li>
  );
}

const logSearch = debounce((value: string) => {
  if (!value) {
    return;
  }

  if ('haas' in window) {
    // @ts-expect-error: window.haas is not typed
    window.haas.tracker.gtm.triggerGAEvent('send', 'event', 'cookbook', 'search', value);
  } else {
    console.log(`Search event: "${value}". GA disabled locally.`);
  }
}, 1000);

type RecipesListProps = Readonly<{
  views: Readonly<Record<string, ViewConfig>>;
}>;

function RecipeList({ views }: RecipesListProps) {
  const [filter, setFilter] = useState(new URLSearchParams(window.location.search).get('search') ?? '');
  const [filterTags, setFilterTags] = useState<Tag[]>([]);

  return (
    <Context.Provider
      value={{
        views: Object.values(views),
        filter,
        filterTags,
        updateFilter(value: string) {
          const lowerCaseValue = value.toLowerCase();
          setFilter(lowerCaseValue);
          replaceQueryParameter('search', lowerCaseValue);
          logSearch(lowerCaseValue);
        },
        updateFilterTags(...tags: Tag[]) {
          setFilterTags(tags);
        },
      }}
    >
      <div className={css.host}>
        <RecipesListHeader />
        <div className={`${css.container} container-fluid`}>
          <TagList isOpened={window.matchMedia('(min-width: 600px)').matches} />
          <ul className={css.list}>
            {Object.entries(views)
              .filter(([, config]) => configMatches(config, filter, filterTags))
              .map(([url, config]) => (
                <ViewRecord key={url} url={url} config={config} />
              ))}
          </ul>
        </div>
      </div>
    </Context.Provider>
  );
}

export default RecipeList;
