import "@vaadin/vaadin-text-field";
import { css, customElement, html, property } from "lit-element";
import { Recipe, recipeInfo } from "../recipe";

@recipeInfo({
  url: "shortcut-listener",
  howDoI: "Listen for keyboard shortcuts",
  description:
    "Show how you can react to various shortcut keys in different scopes of a view",
  sourceFiles: [],
})
@customElement("shortcut-listener")
export class ShortcutListener extends Recipe {
  @property()
  private value = "";
  @property()
  private values: string[] = [];
  @property()
  private keylog: string[] = [];

  static styles = css`
    :host {
      display: grid;
      gap: 20px;
      grid-template-columns: 1fr 1fr;
      grid-auto-rows: 1fr;
      margin: 20px;
    }

    @media (max-width: 600px) {
      :host {
        grid-template-columns: 1fr;
      }
    }

    .scope {
      border: 3px dashed gray;
      padding: 20px;
    }

    textarea {
      border: 1px solid gray;
      height: 100%;
      width: 100%;
    }
  `;

  constructor() {
    super();
    this.addEventListener("keyup", this.elementWideListener);
  }

  render() {
    return html`
      <div class="scope" @keyup=${this.enterListener}>
        <p>Press <strong>enter</strong> to submit value</p>
        <!-- 'input' fires on every keystroke.
             'change' fires on blur -->
        <vaadin-text-field
          .value=${this.value}
          @input=${this.updateValue}
        ></vaadin-text-field>
      </div>

      <div class="scope" @keyup=${this.shiftEnterListener}>
        <p>Press <strong>shift + enter</strong> to submit value</p>
        <vaadin-text-field
          .value=${this.value}
          @input=${this.updateValue}
        ></vaadin-text-field>
      </div>

      <div>
        <h3>Keys pressed</h3>
        <textarea .value=${this.keylog.join("\n")}></textarea>
      </div>
      <div>
        <h3>Submitted values</h3>
        <ul class="values">
          ${this.values.map((val) => html` <li>${val}</li> `)}
        </ul>
      </div>
    `;
  }

  // See https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/key
  // for keys
  elementWideListener(e: KeyboardEvent) {
    this.keylog = [e.key, ...this.keylog];
  }

  enterListener(e: KeyboardEvent) {
    if (e.key === "Enter") {
      this.submitValue();
    }
  }

  shiftEnterListener(e: KeyboardEvent) {
    if (e.key === "Enter" && e.getModifierState("Shift")) {
      this.submitValue();
    }
  }

  updateValue(e: { target: HTMLInputElement }) {
    this.value = e.target.value;
  }

  submitValue() {
    this.values = [...this.values, this.value];
    this.value = "";
  }
}
