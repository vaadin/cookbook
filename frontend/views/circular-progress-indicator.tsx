import '../recipe/circular-progress-indicator/circular-progress-indicator.js';
import Tag from '../generated/com/vaadin/recipes/recipe/Tag.js';
import Recipe from './_shared/Recipe.js';
import type { ViewConfig } from './_shared/ViewConfig.js';
export const config: ViewConfig = {
  title: 'Show progress as a circular indicator',
  detail: {
    description:
      'Learn to create a circular progress indicator component with LitElement in a Vaadin client-side view.',
    sourceFiles: ['circular-progress-indicator-component.ts'],
    tags: [Tag.TYPE_SCRIPT, Tag.HILLA],
  },
};
export default function CircularProgressIndicator() {
  return (
    <Recipe>
      <circular-progress-indicator />
    </Recipe>
  );
}
