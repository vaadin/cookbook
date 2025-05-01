import { viewsSignal } from '@vaadin/hilla-file-router/runtime.js';
import RecipeList from 'Frontend/views/_shared/RecipeList.js';
import { ViewConfig } from './_shared/ViewConfig.js';

export default function Root() {
  return <RecipeList views={viewsSignal.value as Readonly<Record<string, ViewConfig>>} />;
}
