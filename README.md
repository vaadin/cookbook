# Vaadin Cookbook

A collection of solutions to common use cases you run into when developing business web applications.

All solutions should be of the type "The user of my app wants to ..." and focus on functionality.

Solutions are implemented using Java, TS or a mix that is appropriate. The focus is on the solution, not how it is implemented.

The project is deployed at https://cookbook.vaadin.com/

## General guidelines for recipes

Try to keep recipes short and show one thing. The focus is on the functionality which can be copy pasted to another project.

Try to keep recipes in one file. Use a few files if really needed. Use common sense.

Recipes cannot be wrong if they work but all recipes can be improved. It is better to improve an existing recipe than create another one which is almost the same.

Recipes are written in TS or Java. The Java version _should_ be compatible with Vaadin 14. The TS version requires Vaadin 16+.

## Contributing a Java UI based recipe

1. If you want to avoid problems with other people contributing the same idea at the same time, create or find an issue describing the intent (one sentence is typically enough) and accept the issue
2. Fork and clone the project
3. Create a Java package for your recipe

```
mkdir src/main/java/com/vaadin/recipes/recipe/recipenamegoeshere/
```

4. Create a Java file in the folder with the following

```
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("recipe-name-goes-here")
@Metadata(
  howdoI = "Short (< 50) character explanation",
  description = "150-160 character description that is shown on the listing page and Google search results."
)
public class RecipeNameGoesHere extends Recipe {
}
```

5. Code the recipe and test it. Start the project using `mvn` and you find your recipe at http://localhost:8080/recipe-name-goes-here

6. If you created multiple files (Java or CSS), refer to them using the `sourceFiles` attribute of `@Metadata`
7. If your recipe needs push, add `tag={Tag.PUSH}` to `@Metadata`. Add any other suitable tags also.
8. Commit and create a PR
9. Wait for 1-2 days for somebody to react to the PR
10. Fix any potential comments
11. :tada:

## Contributing a TS based recipe

1. If you want to avoid problems with other people contributing the same idea at the same time, create or find an issue describing the intent (one sentence is typically enough) and accept the issue
2. Fork and clone the project
3. Create a TS folder for your recipe

```
mkdir frontend/recipe/recipe-name-goes-here/
```

4. Create a TS file in the folder with the following

```
import { html } from "lit";
import { customElement } from "lit/decorators.js";
import { Recipe, recipeInfo } from "../recipe";

@recipeInfo({
  url: "recipe-name-goes-here",
  howDoI: "Short (< 50) character explanation",
  description: "150-160 character description that is shown on the listing page and Google search results."
})
@customElement("recipe-name-goes-here")
export class RecipeNameGoesHere extends Recipe {
  render() {
    return html`
      <div>Template goes here</div>
      `;
  }
}
```

5. Code the recipe and test it. Start the project using `mvn` and you find your recipe at http://localhost:8080/recipe-name-goes-here

6. If you created multiple files (TS or CSS), refer to them using the `sourceFiles` attribute of `@recipeInfo`
7. Add any suitable tags to the `tags` part of `@recipeInfo`
8. Commit and create a PR
9. Wait for 1-2 days for somebody to react to the PR
10. Fix any potential comments
11. :tada:

## Running the project

In most cases you can run the project using

```
mvn
```

or by launching the `Application` class from an IDE.

If you want to run the project on a different port than 8080, you can do

```
PORT=9090 mvn
```
