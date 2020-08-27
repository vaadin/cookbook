import * as ts from "typescript";
import RecipeInfo from "./frontend/generated/com/vaadin/recipes/data/RecipeInfo";
import { BaseRoute } from "@vaadin/router";
import Tag from "./frontend/generated/com/vaadin/recipes/recipe/Tag";
const debug = (...theArgs) => {
  // console.log(theArgs);
};
debug("Running");

const glob = require("glob");
const path = require("path");

const fs = require("fs");

const args = process.argv.slice(2);
export const firstToLower = (text: string) => {
  if (!text || text.length < 2) {
    return text;
  }
  return text.substr(0, 1).toLowerCase() + text.substr(1);
};

export const recipeToRoute = (
  recipeInfo: RecipeInfo
): RecipeRouteWithAction => {
  const folder = "recipe/" + recipeInfo.url + "/";
  const absoluteSourceFiles = [
    recipeInfo.url + ".ts",
    ...(recipeInfo.sourceFiles || []),
  ].map((relativePath) => folder + relativePath);
  const modifiedRecipeInfo = Object.assign(recipeInfo, {
    sourceFiles: absoluteSourceFiles,
    howDoI: firstToLower(recipeInfo.howDoI),
    tags: ["Tag.TYPESCRIPT", ...(recipeInfo.tags || [])].map((tag) => "" + tag),
  });
  return {
    path: recipeInfo.url,
    component: recipeInfo.url,
    actionString: `./recipe/${recipeInfo.url}/${recipeInfo.url}`,
    info: modifiedRecipeInfo,
  };
};

const getSource = (node: ts.Node, source: ts.SourceFile): string => {
  return source.getFullText().substring(node.pos, node.end);
};

const findRoute = (
  node: ts.Node,
  source: ts.SourceFile
): RecipeRouteWithAction | undefined => {
  if (ts.isDecorator(node)) {
    const decoratorNode = node;
    if (
      ts.isCallExpression(decoratorNode.expression) &&
      ts.isIdentifier(decoratorNode.expression.expression) &&
      decoratorNode.expression.expression.text == "recipeInfo"
    ) {
      const src = getSource(decoratorNode.expression.arguments[0], source)
        .replace(/\n/g, "")
        .replace(/Tag.([A-Za-z]+)/, '"Tag.$1"');
      const recipeInfo = eval("const a= " + src + ";a;");
      const route = recipeToRoute(recipeInfo);
      return route;
    }
  }
  return undefined;
};

const visit = (
  node: ts.Node,
  source: ts.SourceFile,
  routes: RecipeRouteWithAction[]
) => {
  let route = findRoute(node, source);
  if (route) {
    return route;
  }

  node.forEachChild((child) => {
    const r = visit(child, source, routes);
    if (r) {
      route = r;
    }
  });
  return route;
};

interface RecipeRouteWithAction extends BaseRoute {
  info: RecipeInfo;
  actionString: string;
}

const writeIfChanged = (file, contents) => {
  debug("Checking if write is needed for ", file);
  debug("Contents:");
  debug(contents);
  if (fs.existsSync(file) && fs.readFileSync(file, "utf-8") == contents) {
    debug("No write needed for ", file);
    return;
  }
  debug("Writing ", file);
  fs.writeFileSync(file, contents);
};
const tsFileGlobs = args[0];
const routes: RecipeRouteWithAction[] = [];

const g = path.resolve(__dirname, tsFileGlobs);
debug("Looking for " + g);
const files = glob.sync(g);

debug("Found files", files);
files.forEach((file) => {
  debug("Scanning " + file);
  const originalCode = fs.readFileSync(file, "utf8");
  const source = ts.createSourceFile(
    "my-view.ts",
    originalCode,
    ts.ScriptTarget.Latest
  );
  const r = visit(source, source, routes);
  if (r) {
    routes.push(r);
  }
});
const frontend = path.resolve(__dirname, "frontend");
const recipeInfoJson = path.resolve(
  __dirname,
  "target",
  "classes",
  "ts-recipe-info.json"
);
const routesTsTemplateFile = path.resolve(__dirname, "ts-routes.ts.template");
const routesTsFile = path.resolve(frontend, "ts-routes.ts");

writeIfChanged(
  recipeInfoJson,
  JSON.stringify(
    routes.map((route) => route.info),
    null,
    2
  ).replace(/"Tag.([A-Za-z]*)"/g, '"$1"')
);

const routesJson = JSON.stringify(routes, null, 2);
const routesTS = routesJson
  .replace(
    /"actionString": "(.*)"/g,
    'action: async() => { await import("$1");}'
  )
  .replace(/"Tag.([A-Za-z]*)"/g, "Tag.$1");

const tsRoutesTpl = fs.readFileSync(routesTsTemplateFile, "utf-8");

const tsRoutes = tsRoutesTpl.replace(
  /export const tsRecipeRoutes: RecipeRoute\[\] = .*/s,
  "export const tsRecipeRoutes: RecipeRoute[] = " + routesTS + ";"
);
writeIfChanged(routesTsFile, tsRoutes);
