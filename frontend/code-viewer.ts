import "@vaadin/vaadin-tabs";
import {
  customElement,
  html,
  LitElement,
  property,
  unsafeCSS,
} from "lit-element";
import { unsafeHTML } from "lit-html/directives/unsafe-html";
import { getSimpleName } from "./util";
import { getSource } from "./generated/RecipeEndpoint";
import "@vaadin/vaadin-text-field/vaadin-text-area";
import {
  registerStyles,
  css,
} from "@vaadin/vaadin-themable-mixin/register-styles";
//@ts-ignore
import * as Prism from "./prism.js";
import prismCss from "./prism.css";

registerStyles(
  "vaadin-tabs",
  css`
    :host([theme~="cookbook-code"]) {
      box-shadow: inset 0 -1px 0 0 var(--color-graphite-darker);
      color: var(--color-graphite-lighter);
    }

    [part="forward-button"],
    [part="back-button"] {
      color: var(--color-graphite);
    }
  `
);

registerStyles(
  "vaadin-tab",
  css`
    :host([theme~="cookbook-code"]) {
      color: var(--color-stainless);
    }

    :host([theme~="cookbook-code"][selected]),
    :host([theme~="cookbook-code"][active]) {
      color: var(--color-alloy-lighter);
    }

    :host([theme~="cookbook-code"][selected]) {
      border-bottom: 2px solid var(--color-water);
    }

    :host([theme~="cookbook-code"][selected])::before,
    :host([theme~="cookbook-code"][selected])::after {
      display: none !important;
    }
  `
);

@customElement("code-viewer")
export class CodeViewer extends LitElement {
  @property({ type: Array })
  private _files: string[] = [];

  set files(files: string[]) {
    this._files = files;
    this.forceRefresh();
  }
  get files() {
    return this._files;
  }
  @property({ type: String })
  contents = "";

  @property({ type: String })
  language = "none";

  createRenderRoot() {
    return this;
  }

  render() {
    return html`
      <style>
        code-viewer {
          display: block;
          background-color: var(--color-charcoal);
          font-size: var(--text-size-sm);
          border-radius: var(--roundness-lg);
        }

        ${unsafeCSS(prismCss)} pre[class*="language-"] {
          background: transparent;
        }
      </style>

      <vaadin-tabs @selected-changed=${this.viewSource} theme="cookbook-code">
        ${this.files.map(
          (file) =>
            html`<vaadin-tab theme="cookbook-code"
              >${getSimpleName(file)}</vaadin-tab
            >`
        )}
      </vaadin-tabs>
      ${/*Don't reuse these elements. This is needed because Prism
          removes the markers lit-html uses to track slots */
      unsafeHTML(
        `<pre><code class="language-${this.language}">${this.escapeHtml(
          this.removeMetaInfoFromCode(this.contents)
        )}
        </code></pre>`
      )}
    `;
  }

  viewSource(e: CustomEvent) {
    const fileIndex = e.detail.value;
    this.viewSourceFile(fileIndex);
  }

  async viewSourceFile(index: number) {
    if (this.files.length <= index) {
      // Hack to handle initial rendering
      setTimeout(() => {
        this.viewSourceFile(index);
      }, 500);
      return;
    }

    const file = this.files[index];
    this.contents = await getSource(file);
    this.language = file.split(".").reverse()[0];

    // Wait for LitElement to finish updating the DOM before higlighting
    await this.updateComplete;
    //@ts-ignore
    Prism.highlightAllUnder(this);
  }

  escapeHtml(unsafe: string) {
    return unsafe
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  }

  removeMetaInfoFromCode(code: string) {
    if (!code)
      return '';
    code = this.removeMetadataTag(code);
    return code
      .substring(code.indexOf("import"))
        .replace(new RegExp("import.*com.vaadin.recipes.recipe.Recipe;"), "")
        .replace(new RegExp("import.*com.vaadin.recipes.recipe.Metadata;"), "")
        .replace(new RegExp("import.*com.vaadin.recipes.recipe.Tag;"), "")
        .replace("extends Recipe", "extends VerticalLayout");
      ;
  }

  removeMetadataTag(code: string): string {
    const metaLen = "@Metadata".length;
    const startIdx = code.indexOf("@Metadata");
    let endIdx = -1;
    let openBrackets = 0;
    for (let i = startIdx + metaLen; i < code.length; i++) {
      if(code.charAt(i)=='(') {
        ++openBrackets;
      } else if (code.charAt(i)==')') {
        --openBrackets;
        if (openBrackets == 0) {
          endIdx = i + 1;
          break;
        }
      }
    }
    return code.replace(code.slice(startIdx, endIdx), "");
  }

  forceRefresh() {
    this.viewSourceFile(0);
  }
}
