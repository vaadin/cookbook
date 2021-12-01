import "@vaadin/text-field";
import { css, html } from "lit";
import { customElement, state } from "lit/decorators.js";
import Tag from "../../generated/com/vaadin/recipes/recipe/Tag";
import { Recipe, recipeInfo } from "../recipe";

@recipeInfo({
  url: "shortcut-listener",
  howDoI: "Listen for keyboard shortcuts",
  description:
    "Learn how to react to various shortcut keys with modifier keys and scopes.",
  tags: [Tag.KEYBOARD],
})
@customElement("shortcut-listener")
export class ShortcutListener extends Recipe {
  @state()
  private value = "";
  @state()
  private values: string[] = [];
  @state()
  private keylog: string[] = [];

  static styles = css`
    .contents {
      display: grid;
      gap: 20px;
      grid-template-columns: 1fr 1fr;
      grid-auto-rows: 1fr;
      margin-bottom: 60px;
    }

    @media (max-width: 600px) {
      .contents {
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
      <div class="contents">
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
