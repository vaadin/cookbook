# Recipes for Vaadin

A collection of solutions to common use cases you run into when developing business web applications.

All solutions should be of the type "The user of my app wants to ..." and focus on functionality.

Solutions are implemented using Java, TS or a mix that is appropriate. The focus is on the solution, not how it is implemented.

The project is currently deployed at https://labs.vaadin.com/recipes/

## Contributing a Java UI based recipe

1. If you want to avoid problems with other people contributing the same idea at the same time, create or find an issue describing the intent (one sentence is typically enough) and accept the issue
2. Fork and clone the project
3. Create a Java package for your recipe

```
mkdir src/main/java/org/vaadin/recipes/recipenamegoeshere/
```

4. Create a Java file in the folder with the following

```
@Route("recipe-name-goes-here")
@Metadata(howdoI = "Short (< 50) character explanation")
public class RecipeNameGoesHere extends Recipe {
}
```

5. Code the recipe and test it. Start the project using `mvn` and you find your recipe at http://localhost:8080/recipe-name-goes-here

6. Commit and create a PR
7. Wait for 1-2 days for somebody to react to the PR
8. Fix any potential comments
9. :tada:

## Contributing a TS based recipe

1. If you want to avoid problems with other people contributing the same idea at the same time, create or find an issue describing the intent (one sentence is typically enough) and accept the issue
2. Fork and clone the project
3. Create a TS folder for your recipe

```
mkdir frontend/recipe/recipe-name-goes-here/
```

4. Create a TS file in the folder with the following

```
import { customElement, html } from "lit-element";
import { Recipe } from "../recipe";

@customElement("recipe-name-goes-here")
export class RecipeNameGoesHere extends Recipe {
  render() {
    return html`
      <div>Template goes here</div>
      `;
  }
}
```

5. Add metadata for the view to `frontend/ts-recipes.ts`:

```
registerRecipe({
  url: "recipe-name-goes-here",
  howDoI: "Short (< 50) character explanation",
  sourceFiles: [], // Add any additional files to show here, relative to the same folder
  import: async () => {
    await import(
      "./recipe/recipe-name-goes-here/recipe-name-goes-here"
    );
  },
});
```

6. Code the recipe and test it. Start the project using `mvn` and you find your recipe at http://localhost:8080/recipe-name-goes-here

7. Commit and create a PR
8. Wait for 1-2 days for somebody to react to the PR
9. Fix any potential comments
10. :tada:
