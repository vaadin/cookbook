import { RouteWithAction } from "@vaadin/router";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";

interface Recipe {
  tag: string;
  title: string;
  import: () => Promise<void>;
}
interface RecipeRoute extends RouteWithAction {
  info: RecipeInfo;
}
export const tsRecipeRoutes: RecipeRoute[] = [];
const registerRecipe = (data: Recipe) => {
  tsRecipeRoutes.push({
    path: data.tag,
    component: data.tag,
    action: data.import,
    info: {
      title: data.title,
      url: data.tag,
    },
  });
};
registerRecipe({
  tag: "circular-progress-view",
  title: "See progress as a circular indicator",
  import: async () => {
    await import("./recipe/circular-progress-indicator/circular-progress-view");
  },
});
