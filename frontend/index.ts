import { Flow } from "@vaadin/flow-frontend/Flow";
import { Route, Router } from "@vaadin/router";
import { recipes } from "./all-recipes";
import { tsRecipeRoutes } from "./ts-recipes";
import { MainView } from "./views/main-view";

const { serverSideRoutes } = new Flow({
  imports: () => import("../target/frontend/generated-flow-imports"),
});

const routes: Route[] = [
  {
    path: "",
    component: "main-view",
    action: async (_context, _commands) => {
      await import("./views/main-view");
      updateCurrentRecipe(_context.pathname);
    },
    children: [
      {
        path: "",
        component: "intro-view",
        action: async () => {
          await import("./views/intro-view");
        },
      },
      ...tsRecipeRoutes,
      ...serverSideRoutes, // IMPORTANT: this must be the last entry in the array
    ],
  },
];

export const router = new Router(document.querySelector("#outlet"));
router.setRoutes(routes);

export const updateCurrentRecipe = (path?: string) => {
  if (!path) {
    path = router.location.pathname;
  }

  if (path.includes("-")) {
    const tag = path.substr(path.lastIndexOf("/") + 1);
    const recipe = recipes.find((recipe) => recipe.url == tag);
    if (recipe) {
      const mainView = document.querySelector("main-view")! as MainView;
      mainView.recipe = recipe;
    }
  }
};
