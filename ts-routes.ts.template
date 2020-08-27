import { RouteWithAction } from "@vaadin/router";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";
import Tag from "./generated/com/vaadin/recipes/recipe/Tag";

export interface RecipeInfoWithImport extends RecipeInfo {
  import: () => Promise<void>;
}

interface RecipeRoute extends RouteWithAction {
  info: RecipeInfo;
}

export const tsRecipeRoutes: RecipeRoute[] = [];
