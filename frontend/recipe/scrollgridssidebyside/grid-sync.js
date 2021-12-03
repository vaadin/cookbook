const grids = [];

function addGrid(grid) {
  grids.push(grid);
  grid.$.outerscroller.addEventListener("scroll", (_) => syncGrids(grid));
}

function syncGrids(source) {
  grids
    .filter((grid) => grid !== source)
    .forEach((grid) =>
      requestAnimationFrame(
        () =>
          (grid.$.outerscroller.scrollTop = source.$.outerscroller.scrollTop)
      )
    );
}

// Need to "export" to window, Flow doesn't support named imports yet
window.syncGrid = addGrid;
