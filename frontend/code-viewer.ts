import "@vaadin/tabs";
import "@vaadin/text-area";
import { html, LitElement, unsafeCSS } from "lit";
import { customElement, property } from "lit/decorators.js";
import { unsafeHTML } from "lit/directives/unsafe-html.js";
import { getSource } from "./generated/RecipeEndpoint";
import * as Prism from "prismjs";
import "prismjs/components/prism-java.js";
import "prismjs/components/prism-typescript";
import { getSimpleName } from "./util";
// @ts-ignore
import prismCss from "prismjs/themes/prism-okaidia.css?inline";

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
      ${
        /*Don't reuse these elements. This is needed because Prism
          removes the markers lit-html uses to track slots */
        unsafeHTML(
          `<pre><code class="language-${this.language}">${this.escapeHtml(
            this.removeMetaInfoFromCode(this.contents)
          )}
        </code></pre>`
        )
      }
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
    if (!code) return "";

    code = code.substring(code.search(/^import/gm)); // remove package
    code = code.replace("extends Recipe", "extends VerticalLayout");
    code = this.removeMetadataTag(code);
    code = this.removeMetaImports(code);
    return code;
  }

  removeMetaImports(code: string): string {
    return code
      .split("\n")
      .filter(function (line) {
        return line.indexOf("import com.vaadin.recipes.recipe") == -1;
      })
      .join("\n");
  }

  removeMetadataTag(code: string): string {
    const metaLen = "@Metadata".length;
    const startIdx = code.indexOf("@Metadata");
    let endIdx = -1;
    let openBrackets = 0;
    for (let i = startIdx + metaLen; i < code.length; i++) {
      if (code.charAt(i) == "(") {
        ++openBrackets;
      } else if (code.charAt(i) == ")") {
        --openBrackets;
        if (openBrackets == 0) {
          endIdx = code.substring(i).indexOf("\n") + i + 1;
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
