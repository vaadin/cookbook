import { RouteWithAction } from "@vaadin/router";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";

export interface RecipeInfoWithImport extends RecipeInfo {
  import: () => Promise<void>;
}

interface RecipeRoute extends RouteWithAction {
  info: RecipeInfo;
}

export const tsRecipeRoutes: RecipeRoute[] = [];
