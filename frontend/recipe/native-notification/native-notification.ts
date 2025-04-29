import '@vaadin/button';
import '@vaadin/text-area';
import '@vaadin/text-field';
import { css, html, nothing } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import type { DetailedHTMLProps } from 'react';
import { Recipe, recipeInfo } from '../recipe';

declare global {
  interface HTMLElementTagNameMap {
    'native-notification': NativeNotification;
  }
}

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'native-notification': DetailedHTMLProps<HTMLAttributes<NativeNotification>, NativeNotification>;
    }
  }
}

@recipeInfo({
  url: 'native-notification',
  howDoI: 'Show a native OS-level notification',
  description: 'Show how you display a native OS-level notification to the user.',
})
@customElement('native-notification')
export class NativeNotification extends Recipe {
  @state()
  accessor #error: string | undefined;

  #title = '';
  #body = '';

  render() {
    return html`
      <div class="flex-column">
        <vaadin-text-field label="Title" @value-changed=${this.#onTitleChanged}></vaadin-text-field>
        <vaadin-text-area label="Body" @value-changed=${this.#onBodyChanged}></vaadin-text-area>
      </div>
      <vaadin-button @click=${this.showNotification}> Show Notification </vaadin-button>

      ${this.#error ? html` <p>The user has not allowed this page to show notifications: ${this.#error}</p> ` : nothing}
    `;
  }

  async showNotification() {
    if (!('Notification' in window)) {
      this.#error = 'This browser does not support native notifications.';
      return;
    }

    // Showing native notificaitons requires an explicit user permission
    // https://developer.mozilla.org/en-US/docs/Web/API/Notification/requestPermission
    let permission = Notification.permission;
    if (permission !== 'granted') {
      // Safari uses the deprecated .requestPermission(callback) API
      // Other browsers use the standard .requestPermission() => Promise API
      let permissionCallback: NotificationPermissionCallback;
      const permissionSafari = new Promise<NotificationPermission>((r) => (permissionCallback = r));
      const permissionModern = Notification.requestPermission(permissionCallback!);

      // If `permissionModern` is defined use it (Safari would return `undefined`).
      // Otherwise, fallback to the callback-based API.
      permission = await (permissionModern || permissionSafari);
    }

    switch (permission) {
      case 'granted':
        // For how to add icon, badge, image, actions and more
        // see https://developer.mozilla.org/en-US/docs/Web/API/Notification/Notification
        const notification = new Notification(this.#title, {
          body: this.#body,
        });
        notification.onclick = (e) => console.log('clicked', e);
        this.#error = undefined;
        break;
      case 'denied':
      case 'default':
        this.#error = permission;
    }
  }

  #onTitleChanged(e: CustomEvent) {
    this.#title = (e.target as HTMLInputElement).value;
  }

  #onBodyChanged(e: CustomEvent) {
    this.#body = (e.target as HTMLInputElement).value;
  }

  static styles = css`
    :host {
      display: block;
      margin: 20px;
    }

    .flex-column {
      display: flex;
      flex-direction: column;
      max-width: 600px;
    }
  `;
}
