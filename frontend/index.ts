import { Flow } from "@vaadin/flow-frontend/Flow";
import { Router } from "@vaadin/router";
import { tsRecipeRoutes } from "./ts-recipes";

const { serverSideRoutes } = new Flow({
  imports: () => import("../target/frontend/generated-flow-imports"),
});

const routes = [
  // for client-side, place routes below (more info https://vaadin.com/docs/v15/flow/typescript/creating-routes.html)
  {
    path: "",
    component: "main-view",
    action: async () => {
      await import("./views/main-view");
    },
    children: [
      {
        path: "",
        component: "all-recipes",
        action: async () => {
          await import("./all-recipes/all-recipes");
        },
      },
      ...tsRecipeRoutes,
      ...serverSideRoutes, // IMPORTANT: this must be the last entry in the array
    ],
  },
];

export const router = new Router(document.querySelector("#outlet"));
router.setRoutes(routes);
