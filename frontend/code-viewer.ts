import "@vaadin/vaadin-tabs";
import {
  css,
  customElement,
  html,
  LitElement,
  property,
  query,
} from "lit-element";
import { getSimpleName } from "./util";
import { getSource } from "./generated/RecipeEndpoint";
import "@vaadin/vaadin-text-field/vaadin-text-area";
import { TextAreaElement } from "@vaadin/vaadin-text-field/vaadin-text-area";

@customElement("code-viewer")
export class CodeViewer extends LitElement {
  @property({ type: Array })
  _files: string[] = [];

  set files(files: string[]) {
    this._files = files;
    this.forceRefresh();
  }
  get files() {
    return this._files;
  }
  @property({ type: String })
  contents: string = "";
  viewSourceBound = this.viewSource.bind(this);

  @query("vaadin-text-area")
  codeViewer: TextAreaElement | undefined;

  static get styles() {
    return css`
      :host {
        height: 100%;
        display: flex;
        flex-direction: column;
      }
      vaadin-tabs {
        width: 100%;
      }
      vaadin-text-area {
        width: 100%;
        overflow: auto;
      }
    `;
  }
  render() {
    return html`
      <vaadin-tabs @selected-changed=${this.viewSourceBound}
        >${this.files.map(
          (file) => html`<vaadin-tab>${getSimpleName(file)}</vaadin-tab>`
        )}</vaadin-tabs
      >
      <vaadin-text-area .value=${this.contents}></vaadin-text-area>
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
    this.contents = await getSource(this.files[index]);
    this.codeViewer!.scrollTop = 0;
  }

  forceRefresh() {
    this.viewSourceFile(0);
  }
}
