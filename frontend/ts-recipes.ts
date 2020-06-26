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
  const folder = "recipe/" + recipeInfo.url + "/";
  const absoluteSourceFiles = [
    recipeInfo.url + ".ts",
    ...recipeInfo.sourceFiles,
  ].map((relativePath) => folder + relativePath);
  const modifiedRecipeInfo = Object.assign(recipeInfo, {
    sourceFiles: absoluteSourceFiles,
  });
  tsRecipeRoutes.push({
    path: recipeInfo.url,
    component: recipeInfo.url,
    action: recipeInfo.import,
    info: modifiedRecipeInfo,
  });
};

registerRecipe({
  url: "circular-progress-indicator",
  howDoI: "Show progress as a circular indicator",
  sourceFiles: ["circular-progress-indicator-component.ts"],
  import: async () => {
    await import(
      "./recipe/circular-progress-indicator/circular-progress-indicator"
    );
  },
});
