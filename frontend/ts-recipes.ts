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

registerRecipe({
  url: "shortcut-listener",
  howDoI: "Listen for keyboard shortcuts",
  sourceFiles: [], // Add any additional files to show here, relative to the same folder
  import: async () => {
    await import("./recipe/shortcut-listener/shortcut-listener");
  },
});
