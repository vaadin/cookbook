import { RouteWithAction } from "@vaadin/router";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";

interface Recipe {
  tag: string;
  howDoI: string;
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
      howDoI: data.howDoI,
      url: data.tag,
    },
  });
};
registerRecipe({
  tag: "circular-progress-view",
  howDoI: "Show progress as a circular indicator",
  import: async () => {
    await import("./recipe/circular-progress-indicator/circular-progress-view");
  },
});
