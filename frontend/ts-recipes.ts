import { RouteWithAction } from "@vaadin/router";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";

interface RecipeInfoWithImport extends RecipeInfo {
  import: () => Promise<void>;
}
interface RecipeRoute extends RouteWithAction {
  info: RecipeInfo;
}
export const tsRecipeRoutes: RecipeRoute[] = [];
const registerRecipe = (recipeInfo: RecipeInfoWithImport) => {
  tsRecipeRoutes.push({
    path: recipeInfo.url,
    component: recipeInfo.url,
    action: recipeInfo.import,
    info: recipeInfo,
  });
};

registerRecipe({
  url: "circular-progress-view",
  howDoI: "Show progress as a circular indicator",
  // TODO Add main source automatically and use relative path for the rest
  sourceFiles: [
    "recipe/circular-progress-indicator/circular-progress-indicator.ts",
    "recipe/circular-progress-indicator/circular-progress-view.ts",
  ],
  import: async () => {
    await import("./recipe/circular-progress-indicator/circular-progress-view");
  },
});
