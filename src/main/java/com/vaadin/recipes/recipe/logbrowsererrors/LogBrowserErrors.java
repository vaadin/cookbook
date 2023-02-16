package com.vaadin.recipes.recipe.logbrowsererrors;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import elemental.json.JsonObject;
import elemental.json.JsonString;
import elemental.json.JsonValue;

import javax.annotation.Nullable;

@Route("log-browser-errors")
@Metadata(
    howdoI = "Log browser errors on the server",
    description = "This sample shows, how you can inform the server about JavaScript errors in the browser. You may for instance log them in your server log" +
            " to allow better tracing of user issues."
)
@JsModule("recipe/log-browser-errors/log-browser-errors.js")
public class LogBrowserErrors extends Recipe {

    private final Pre output;

    public LogBrowserErrors() {

        // This script and the @ClientCallable method should be integrated into the top level layout or component
        // so that it will be available in your whole application.
        getElement().executeJs("""
                 // Register a global error handler, that delegates any error to the server.
                 // Depending on your architecture, you may need to unregister this listener later on.
                 window.addEventListener('error', event => {
                     const {message, filename, lineno: line, colno: col, error } = event;
         
                     this.$server.onLogFromClient(message.toString(), {
                         filename,
                         line,
                         col,
                         level: 'error',
                         message: error ? error.message : undefined,
                         stack: error ? error.stack : undefined
                     });
                 });
                """);

        // Here starts the test playground area.

        Button button1 = new Button("Throw Error in Inline Script", e ->
                getElement().executeJs("throw new Error('This is a test message inside an inline script, " +
                        "that could appear using someComponent.getElement().executeJs(...);')")
        );

        Button button2 = new Button("Throw Error in imported script", e ->
                getElement().executeJs("window.Vaadin.Flow.logBrowserErrors.throwError()")
        );

        output = new Pre();
        output.setWidthFull();
        output.getStyle()
                .set("border", "1px dashed lightgray")
                .set("background-color", "whitesmoke")
                .set("font-size", "0.8rem")
                .set("overflow-y", "auto");

        add(new HorizontalLayout(button1, button2), output);
    }

    /**
     * This method is called by the client side to notify the server about the error.
     * The message contains the error message, while the details contain any additional information,
     * like the stacktrace.
     * @param pMessage error message
     * @param pDetails additional details
     */
    @ClientCallable
    protected void onLogFromClient(
            final String pMessage,
            @Nullable final JsonValue pDetails)
    {
        if (pDetails instanceof JsonObject) {
            JsonValue value = ((JsonObject) pDetails).get("stack");
            if (value instanceof JsonString) {

                // here you should use your own logger
                output.add(value.asString() + "\n\n");
            }
        }

        // This is optional, as the browser logs the errors on its own. You maybe want to inform
        // the user with a more general message, that an error occurred.
        Notification.show("Received client side error: " + pMessage)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}

