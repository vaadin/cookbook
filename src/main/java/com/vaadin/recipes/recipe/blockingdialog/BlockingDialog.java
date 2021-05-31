package com.vaadin.recipes.recipe.blockingdialog;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Route("blocking-dialog")
@Metadata(
        howdoI = "Simulate a blocking dialog as is known from Swing programming or Eclipse RCP",
        description = "From classic Java programming, often a dialog is shown blocking the rest of the program execution until a user decision is made. In Vaadin, as you cannot block the application server's http response thread, this has to be simulated blocking the UI, and letting another thread execute the continuation code.",
        tags = { Tag.JAVA }
)

public class BlockingDialog extends Recipe {

    // This class can be copied into a utility class and is an inner class only because that way the code is
    // shown in the Vaadin Cookbook environment.
    public static class YesNoDecider {

        // to be called from within a UI's thread
        public static void yesNo(String question, Consumer<Boolean> consumer) {

            Dialog dialog = new Dialog();

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            dialog.add(new Span(question));
            dialog.add(new Button("yes", e -> future.complete(true) ));
            dialog.add(new Button("no", e -> future.complete(false) ));
            dialog.setModal(true); // in fact, the extra indirection through a command that is re-executed a number of times would allow for non-modality, too.
            dialog.setCloseOnEsc(false); // allowing to remove the yes/np buttons would keep the user-code un-executed forever, leaking memory and possibly other resources.
            dialog.setCloseOnOutsideClick(false); // allowing to remove the yes/no buttons would keep the user-code un-executed forever, leaking memory and possibly other resources.

            // functional identity mapping with a little side effect
            future.thenApply(value -> {
                dialog.close();
                synchronized (dialog) {
                    dialog.notify();
                }
                return value;
            });

            UI ui = UI.getCurrent(); // in the UI thread, get the UI instance
            new Thread(() -> { // create a worker thread to run the continuation as we will have to release the UI thread
                UI.setCurrent(ui); // in case the command uses 'UI.getCurrent()' assign the parent thread's UI to this new child thread.
                synchronized (dialog) {
                    try {
                        Boolean answer = future.get(); // wait for the user to complete the 'Boolean' future (that is, make a decision)
                        ui.access(() -> {
                            consumer.accept(answer);
                            ui.push();
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // one may consider instead using a thread pool, see ExecutorService for example.

            dialog.open();
        }
    }

    public BlockingDialog(){

        // Consider the implicit Consumer<Boolean> below as the user code that can in principle be arbitrarily complex.
        // In Swing, it would be all the application code following the user's decision -- the 'continuation'.
        // The main work migrating a Swing application's blocking dialog is to refactor that continuation into the
        // Consumer instance.
        Button openDialog = new Button("Open a seemingly *blocking* dialog", event -> YesNoDecider.yesNo("Would you like to save the world?", answer -> {
            if (answer) {
                Notification.show("Brilliant. Welcome to the league of super heroes. We will get in touch soon, with your first assignment.");
            }
            else {
                Notification.show("I understand. We all have a dayjob. Never mind.");
            }
        }));

        add(openDialog);
    }
}
