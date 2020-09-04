import { Flow } from "@vaadin/flow-frontend/Flow";
import { Route, Router } from "@vaadin/router";
import { tsRecipeRoutes } from "./ts-routes";

const { serverSideRoutes } = new Flow({
  imports: () => import("../target/frontend/generated-flow-imports"),
});

const routes: Route[] = [
  {
    path: "",
    component: "main-view",
    action: async (_context, _commands) => {
      await import("./views/main-view");
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

