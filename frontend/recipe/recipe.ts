import { LitElement } from "lit-element";
import RecipeInfo from "../generated/com/vaadin/recipes/data/RecipeInfo";

export const recipeInfo = (_info: RecipeInfo) => (_constructor: Function) => {
  console.log(_constructor);
};

export class Recipe extends LitElement {}
