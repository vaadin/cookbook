import React, { type PropsWithChildren } from 'react';
import css from './recipe-view.module.css';
import type { ViewConfig } from './ViewConfig.js';
import CodeViewer from './CodeViewer.js';
import { Icon } from './icons.js';

type RecipeHeaderProps = Readonly<{
  config: ViewConfig;
}>;

function RecipeHeader({ config: { title, detail: { description, tags } = {} } }: PropsWithChildren<RecipeHeaderProps>) {
  return (
    <header className={css.header}>
      <div className="container-fluid">
        <a href="/" className={css.backLink}>
          <Icon>arrow-left</Icon>
          <span>Back to Cookbook</span>
        </a>
        {title && <h3 className={css.title}>{title.trim().replace(/^\w/, (c) => c.toUpperCase())}</h3>}
        {description && description.length > 0 && <p className="paragraph-sm">{description}</p>}
        <span className="tag-group">
          {tags?.map((tag, index) => (
            <span key={index} className="tag">
              {tag}
            </span>
          ))}
        </span>
      </div>
    </header>
  );
}

type RecipeProps = Readonly<{
  config?: ViewConfig;
  children?: React.ReactNode;
}>;

function Recipe({ config, children }: PropsWithChildren<RecipeProps>) {
  return (
    <div className={css.host}>
      {config ? <RecipeHeader config={config} /> : null}
      <div className={css.example}>{children}</div>
      {config?.detail?.sourceFiles?.length && (
        <CodeViewer
          className={`${css.code} container-fluid`}
          files={config.detail.sourceFiles.filter((file) => file != null)}
        ></CodeViewer>
      )}
    </div>
  );
}

export default Recipe;
