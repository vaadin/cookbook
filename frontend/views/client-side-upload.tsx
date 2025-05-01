import '../recipe/client-side-upload/client-side-upload.js';
import Tag from '../generated/com/vaadin/recipes/recipe/Tag.js';
import Recipe from './_shared/Recipe.js';
import type { ViewConfig } from './_shared/ViewConfig.js';
export const config: ViewConfig = {
  title: 'Upload files to server from a TypeScript view',
  detail: {
    description: 'Learn how to upload files to server from a TypeScript view.',
    sourceFiles: ['com/vaadin/recipes/recipe/clientsideupload/FileUploadEndpoint.java'],
    tags: [Tag.TYPE_SCRIPT, Tag.HILLA],
  },
};
export default function ClientSideUpload() {
  return (
    <Recipe>
      <client-side-upload />
    </Recipe>
  );
}
