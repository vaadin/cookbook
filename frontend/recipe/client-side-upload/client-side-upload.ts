import "@vaadin/upload";
import {UploadResponseEvent} from "@vaadin/upload";
import { html } from "lit";
import Tag from "../../generated/com/vaadin/recipes/recipe/Tag";
import { Recipe, recipeInfo } from "../recipe";
import {customElement} from "lit/decorators.js";
import {Notification} from "@vaadin/notification";

@recipeInfo({
  url: "client-side-upload",
  howDoI: "Upload files to server from a TypeScript view",
  description:
    "Learn how to upload files to server from a TypeScript view.",
  sourceFiles: [
    "com/vaadin/recipes/recipe/clientsideupload/FileUploadEndpoint.java",
  ],
  tags: [Tag.FUSION],
})
@customElement("client-side-upload")
export class UploadView extends Recipe {

  render() {
    return html`
      <vaadin-upload target="api/fileupload" @upload-response=${this.handleResponse}></vaadin-upload>
    `;
  }

  handleResponse(e: UploadResponseEvent){
    if (e.detail.xhr.status == 200) {
      Notification.show("Upload success!")
    } else {
      Notification.show("OOps, something went wrong.")
    }
  }
}
