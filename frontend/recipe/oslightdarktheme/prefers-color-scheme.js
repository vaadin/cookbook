// 1. @JsModule("prefers-color-scheme.js") on your main view
// 2. Put this in /frontend/prefers-color-scheme.js
window.applyTheme = () => {
  const theme = window.matchMedia("(prefers-color-scheme: dark)").matches
    ? "dark"
    : "";
  document.documentElement.setAttribute("theme", theme);
};
window
  .matchMedia("(prefers-color-scheme: dark)")
  .addListener(window.applyTheme);
window.applyTheme();
