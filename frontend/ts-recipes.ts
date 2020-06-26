import { registerRecipe } from "./util";

registerRecipe({
  url: "circular-progress-indicator",
  howDoI: "Show progress as a circular indicator",
  sourceFiles: ["circular-progress-indicator-component.ts"],
  import: async () => {
    await import(
      "./recipe/circular-progress-indicator/circular-progress-indicator"
    );
  },
});
