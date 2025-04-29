import '@vaadin/upload';
import { UploadResponseEvent } from '@vaadin/upload';
import { html } from 'lit';
import type { DetailedHTMLProps } from 'react';
import Tag from '../../generated/com/vaadin/recipes/recipe/Tag';
import { Recipe, recipeInfo } from '../recipe';
import { customElement } from 'lit/decorators.js';
import { Notification } from '@vaadin/notification';

declare global {
  interface HTMLElementTagNameMap {
    'client-side-upload': UploadView;
  }
}

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'client-side-upload': DetailedHTMLProps<HTMLAttributes<UploadView>, UploadView>;
    }
  }
}

@recipeInfo({
  url: 'client-side-upload',
  howDoI: 'Upload files to server from a TypeScript view',
  description: 'Learn how to upload files to server from a TypeScript view.',
  sourceFiles: ['com/vaadin/recipes/recipe/clientsideupload/FileUploadEndpoint.java'],
  tags: [Tag.HILLA],
})
@customElement('client-side-upload')
export class UploadView extends Recipe {
  render() {
    return html` <vaadin-upload target="api/fileupload" @upload-response=${this.handleResponse}></vaadin-upload> `;
  }

  handleResponse(e: UploadResponseEvent) {
    if (e.detail.xhr.status == 200) {
      Notification.show('Upload success!');
    } else {
      Notification.show('Oops, something went wrong.');
    }
  }
}
