if (!window.Vaadin.Flow.custom) {
    window.Vaadin.Flow.custom = {};
}

/**
 * A function that adds mouse event listeners to the given grid to select
 * cells like in Excel. Starts with a mouse down event and fires a
 * custom event "excel-like-selected-cells" on mouse up.
 * @param grid grid to extend
 */
window.Vaadin.Flow.custom.initGridExcelCellSelection = function (grid) {

    let startTdOfMove;
    let selectedKeys = new Set();
    let selectedColumns = new Set();
    let selectedIndexes = new Set();

    /**
     * Returns the inner table of the grid.
     */
    function getInnerTable() {
        return grid.$.table;
    }


    /**
     * Checks, whether the given element is either a vaadin grid cell or a td. In the first case, returns the
     * containing TD, in the second case the element itself. In all other cases undefined.
     */
    function getTdElement(element) {
        if (element.tagName === "VAADIN-GRID-CELL-CONTENT") {
            return element.offsetParent;
        }
        return element.tagName === "TD" ? element : undefined;
    }

    /**
     * Checks, whether the given event target is either a vaadin grid cell or a td. In the first case, returns the
     * containing TD, in the second case the element itself. In all other cases undefined.
     */
    function getTdElementFromEvent(event) {
        return getTdElement(event.target);
    }

    /**
     * Marks the given element (either a vaadin grid cell or td) visually as "selected". Does not modify the internal
     * selection cache.
     */
    function addSelectedStyle(element) {
        let td = getTdElement(element);
        if (td) {
            td.style.backgroundColor = "gray";
            return td;
        }
        return undefined;
    }

    /**
     * Removes the visual "selected" mark from the given element (either a vaadin grid cell or td) visually.
     * Does not modify the internal selection cache.
     */
    function removeSelectedStyle(element) {
        let td = getTdElement(element);
        if (td) {
            td.style.backgroundColor = "";
            return td;
        }
        return undefined;
    }


    /**
     * Remove all visual "selected" styles.
     */
    function removeAllSelectedStyles() {
        getInnerTable().querySelectorAll("td").forEach(element => {
            removeSelectedStyle(element);
        });
    }

    /**
     * Interpretes the data from the given TD and adds it to the internal selection cache.
     */
    function addToCache(td) {
        selectedKeys.add(grid.__getRowModel(td.parentNode).item.key);
        selectedIndexes.add(td.parentNode.index);

        if (td._column && td._column._flowId) {
            selectedColumns.add(td._column._flowId);
        }
    }

    /**
     * Clears the internal selection caches. Does not modify the "startTdToMove" variable.
     */
    function clearCaches() {
        // do not clear the startTdToMove in here!
        selectedKeys.clear();
        selectedColumns.clear();
        selectedIndexes.clear();
    }

    /**
     * This listener marks all table data cells between the "startTdOfMove" element and the event target (when
     * that refers to a TD, too) as selected (visually and in selection cache).
     */
    function mouseMoveEventListener(event) {
        if (!startTdOfMove) {
            console.warn("no start td cached, maybe the grid element was clicked");
        } else {
            // no need to check for mouse down, since we only add this listener on mousedown event
            let td = getTdElementFromEvent(event);
            if (td) {
                clearCaches();
                removeAllSelectedStyles();

                let table = getInnerTable();

                let startRowOfMove = startTdOfMove.parentNode.rowIndex;
                let startCellOfMove = startTdOfMove.cellIndex;
                let currentRow = td.parentNode.rowIndex;
                let currentCell = td.cellIndex;

                let initRow = startRowOfMove <= currentRow ? startRowOfMove : currentRow;
                let maxRow = startRowOfMove <= currentRow ? currentRow : startRowOfMove;

                let initCell = startCellOfMove <= currentCell ? startCellOfMove : currentCell;
                let maxCell = startCellOfMove <= currentCell ? currentCell : startCellOfMove;

                for (let row = initRow; row <= maxRow; row++) {
                    for (let cell = initCell; cell <= maxCell; cell++) {
                        let tdToMark = table.rows[row].cells[cell];
                        if (tdToMark) {
                            addToCache(tdToMark);
                            addSelectedStyle(tdToMark);
                        }
                    }
                }
            }
        }
    }

    // update td styles on scroll - this is necessary since the grid reuses the inner table cells for new data
    // this leads to marked cells, even if the data itself has not been selected
    getInnerTable().addEventListener("scroll", event => {
        let table = event.target;
        table.querySelectorAll("td").forEach(td => {
            if(selectedIndexes.has(td.parentNode.index) && selectedColumns.has(td._column._flowId)) {
                addSelectedStyle(td);
            } else {
                removeSelectedStyle(td);
            }
        });
    });

    // Initiates the cell selection. Sets the event target td as start td.
    grid.addEventListener("mousedown", event => {
        const innerTable = getInnerTable();

        // prevent default text selection (looks strange)
        innerTable.style.userSelect = "none";

        // update selections
        clearCaches();
        removeAllSelectedStyles();

        let td = getTdElementFromEvent(event);
        if (td) {
            startTdOfMove = td;
            addSelectedStyle(td);
            addToCache(td);
        } else {
            startTdOfMove = undefined;
        }

        grid.addEventListener("mousemove", mouseMoveEventListener);
    });

    // Stops the cell selection and fires the custom event to the server.
    grid.addEventListener("mouseup", e => {
        startTdOfMove = undefined;
        grid.dispatchEvent(new CustomEvent("excel-like-selected-cells", {
            detail: {
                selectedKeys: Array.from(selectedKeys.values()),
                selectedColumns: Array.from(selectedColumns.values())
            }
        }))
        getInnerTable().style.userSelect = "";
        grid.removeEventListener("mousemove", mouseMoveEventListener)
    });

};

