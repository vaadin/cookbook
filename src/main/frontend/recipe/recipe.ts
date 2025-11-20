import { LitElement } from "lit";
import RecipeInfo from "../generated/com/vaadin/recipes/data/RecipeInfo";

export const recipeInfo = (_info: RecipeInfo) => (_constructor: Function) => {};

export class Recipe extends LitElement {
  connectedCallback() {
    super.connectedCallback();
    this.classList.add("container-fluid");
  }
}
