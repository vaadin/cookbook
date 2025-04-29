import '@vaadin/tabs';
import '@vaadin/tabsheet';
import '@vaadin/text-area';
import { html, LitElement, nothing, type PropertyValues } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import { until } from 'lit/directives/until.js';
import { repeat } from 'lit/directives/repeat.js';
import { getSource } from './generated/RecipeEndpoint.js';
import * as Prism from 'prismjs';
import 'prismjs/components/prism-java.js';
import 'prismjs/components/prism-typescript.js';
import { getSimpleName } from './util.js';
import prismCss from 'prismjs/themes/prism-okaidia.css?construct';
import css from './code-viewer.css?construct';

const METADATA_LENGTH = '@Metadata'.length;

function removeMetadataTag(code: string): string {
  const startIdx = code.indexOf('@Metadata');
  let endIdx = -1;
  let openBrackets = 0;
  for (let i = startIdx + METADATA_LENGTH; i < code.length; i++) {
    if (code.charAt(i) == '(') {
      ++openBrackets;
    } else if (code.charAt(i) == ')') {
      --openBrackets;
      if (openBrackets == 0) {
        endIdx = code.substring(i).indexOf('\n') + i + 1;
        break;
      }
    }
  }
  return code.replace(code.slice(startIdx, endIdx), '');
}

function removeMetaImports(code: string): string {
  return code
    .split('\n')
    .filter((line) => !line.includes('import com.vaadin.recipes.recipe'))
    .join('\n');
}

function removeMetaInfoFromCode(code: string, language: string): string {
  if (!code) {
    return '';
  }

  code = code.substring(code.search(/^import/gm)); // remove package
  if (language == 'java') {
    code = code.replace('extends Recipe', 'extends VerticalLayout');
    // add VerticalLayout import if needed
    if (!code.includes('import com.vaadin.flow.component.orderedlayout.VerticalLayout')) {
      code = 'import com.vaadin.flow.component.orderedlayout.VerticalLayout\n' + code;
    }
  }
  code = removeMetadataTag(code);
  code = removeMetaImports(code);
  return code;
}

@customElement('code-snippet')
class CodeSnippet extends LitElement {
  static readonly style = [prismCss];

  @property()
  accessor file = '';

  #language?: string;
  #code?: Promise<string>;

  willUpdate(changedProps: PropertyValues) {
    if (changedProps.has('file')) {
      this.#language = this.file.substring(this.file.lastIndexOf('.') + 1);
      this.#code = getSource(this.file)
        .then((code) => removeMetaInfoFromCode(code, this.#language!))
        .then((code) =>
          code
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;'),
        );
    }
  }

  updated() {
    Prism.highlightAllUnder(this.shadowRoot!);
  }

  render() {
    return this.#language && this.#code
      ? html`<pre><code class="language-${this.#language}">${until(this.#code, html`Loading...`)}</code></pre>`
      : nothing;
  }
}

@customElement('code-viewer')
export class CodeViewer extends LitElement {
  static readonly style = [css];

  @property()
  accessor files: readonly string[] = [];

  render() {
    return html`
      <vaadin-tabsheet>
        <vaadin-tabs theme="cookbook-code">
          ${repeat(
            this.files,
            (file) => file,
            (file) => html`<vaadin-tab id=${file} theme="cookbook-code">${getSimpleName(file)}</vaadin-tab>`,
          )}
        </vaadin-tabs>
        ${repeat(
          this.files,
          (file) => file,
          (file) => html`<code-snippet tab=${file}></code-snippet>`,
        )}
      </vaadin-tabsheet>
    `;
  }
}
