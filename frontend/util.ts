export const getSimpleName = (fullName: string) => {
  return fullName.substring(fullName.lastIndexOf("/") + 1);
};

import { RouteWithAction } from "@vaadin/router";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";

interface RecipeInfoWithImport extends RecipeInfo {
  import: () => Promise<void>;
}
interface RecipeRoute extends RouteWithAction {
  info: RecipeInfo;
}
export const tsRecipeRoutes: RecipeRoute[] = [];
export const registerRecipe = (recipeInfo: RecipeInfoWithImport) => {
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
