import { Flow } from "@vaadin/flow-frontend/Flow";
import { Route, Router, RouterLocation } from "@vaadin/router";
import { tsRecipeRoutes } from "./ts-routes";
import RecipeInfo from "./generated/com/vaadin/recipes/data/RecipeInfo";
import * as RecipeEndpoint from "./generated/RecipeEndpoint";

import "./views/recipes-list-view";
import "./views/recipe-view";

const { serverSideRoutes } = new Flow({
  imports: () => import("../target/frontend/generated-flow-imports"),
});

const routes: Route[] = [
  {
    path: "/",
    component: "recipes-list-view",
    action: async (_context, _commands) => {
      await initRecipes();
    },
  },
  {
    path: "",
    component: "recipe-view",
    action: async (_context, _commands) => {
      await initRecipes();
    },
    children: [
      ...tsRecipeRoutes,
      ...serverSideRoutes, // IMPORTANT: this must be the last entry in the array
    ],
  },
];

export const router = new Router(document.querySelector("#outlet"));
router.setRoutes(routes);

export const recipes: RecipeInfo[] = [];

async function initRecipes() {
  if (recipes.length === 0) {
    recipes.push(...tsRecipeRoutes.map((route) => route.info));
    recipes.push(...(await RecipeEndpoint.list()));
    recipes.sort((a, b) =>
      a.howDoI < b.howDoI ? -1 : a.howDoI == b.howDoI ? 0 : 1
    );
  }
}

// Log page updates to GA
function sendPageview(e: CustomEvent) {
  const routerLocation = e.detail.location as RouterLocation;
  if (location.hostname === "localhost" || location.hostname === "127.0.0.1") {
    console.log("Location changed. GA disabled locally.", routerLocation);
  } else {
    ga("set", "page", routerLocation.pathname);
    ga("send", "pageview");
  }
}
window.addEventListener(
  "vaadin-router-location-changed",
  sendPageview as EventListener
);
