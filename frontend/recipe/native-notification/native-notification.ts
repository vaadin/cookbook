import { css, customElement, html, property } from "lit-element";
import { nothing } from "lit-html";
import { Recipe, recipeInfo } from "../recipe";
import "@vaadin/vaadin-text-field/vaadin-text-field";
import "@vaadin/vaadin-text-field/vaadin-text-area";
import "@vaadin/vaadin-button/vaadin-button";

@recipeInfo({
  url: "native-notification",
  howDoI: "Show a native OS-level notification",
  description:
    "Show how you display a native OS-level notification to the user.",
})
@customElement("native-notification")
export class NativeNotification extends Recipe {

  @property()
  private error?: string;

  private _title = "";
  private _body = "";

  render() {
    return html`
      <div class="flex-column">
        <vaadin-text-field label="Title"
          @value-changed=${this.onTitleChanged}></vaadin-text-field>
        <vaadin-text-area label="Body"
          @value-changed=${this.onBodyChanged}></vaadin-text-area>
      </div>
      <vaadin-button @click=${this.showNotification}>
        Show Notification
      </vaadin-button>

      ${this.error ? html`
        <p>The user has not allowed this page to show notifications:
        ${this.error}</p>
      ` : nothing}
    `;
  }

  async showNotification() {
    if (!('Notification' in window)) {
      this.error = 'This browser does not support native notifications.'
      return;
    }

    // Showing native notificaitons requires an explicit user permission
    // https://developer.mozilla.org/en-US/docs/Web/API/Notification/requestPermission
    let permission = Notification.permission;
    if (permission !== "granted") {
      permission = await Notification.requestPermission();
    }

    switch (permission) {
      case "granted":
        // For how to add icon, badge, image, actions and more
        // see https://developer.mozilla.org/en-US/docs/Web/API/Notification/Notification
        const notification = new Notification(this._title, { body: this._body });
        notification.onclick = e => console.log("clicked", e);
        this.error = undefined;
        break;
      case "denied":
      case "default":
          this.error = permission;
    }
  }

  private onTitleChanged(e: CustomEvent) {
    this._title = (e.target as HTMLInputElement).value;
  }

  private onBodyChanged(e: CustomEvent) {
    this._body = (e.target as HTMLInputElement).value;
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
