window.Vaadin.Flow.logBrowserErrors = {
    throwError() {
        throw new Error("This is a test message in an imported javascript, e. g. the gridConnector.js");
    },
}