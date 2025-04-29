import type { ViewConfig as _ViewConfig } from '@vaadin/hilla-file-router/types.js';
import type RecipeInfo from 'Frontend/generated/com/vaadin/recipes/data/RecipeInfo.js';

export type ViewConfigRecipeInfo = Omit<RecipeInfo, 'url' | 'howDoI'>;

export type ViewConfig = _ViewConfig &
  Readonly<{
    detail: ViewConfigRecipeInfo;
  }>;
