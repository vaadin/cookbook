package com.vaadin.recipes.recipe.blockingdialog;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
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
        abstract static class MyCommand implements Command {
            volatile boolean done;
        }

        // to be called from within a UI's thread
        public static void yesNo(String question, Consumer<Boolean> consumer) {

            Dialog dialog = new Dialog();

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            dialog.add(new Span(question));
            dialog.add(new Button("yes", e -> future.complete(true)));
            dialog.add(new Button("no", e -> future.complete(false)));
            dialog.setModal(true); // in fact, the extra indirection through a command that is re-executed a number of times would allow for non-modality, too.
            dialog.setCloseOnEsc(false); // allowing to remove the yes/np buttons would keep the user-code un-executed forever, leaking memory and possibly other resources.
            dialog.setCloseOnOutsideClick(false); // allowing to remove the yes/no buttons would keep the user-code un-executed forever, leaking memory and possibly other resources.
            dialog.open();

            // Command will be run in the UI thread eventually
            final YesNoDecider.MyCommand command = new YesNoDecider.MyCommand() {
                @Override
                public void execute() {
                    if (future.isDone()) {
                        try {
                            consumer.accept(future.get());
                            done = true;
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            UI ui = UI.getCurrent(); // in the UI thread, get the UI instance
            new Thread(() -> { // worker thread trying to let the UI thread run the command which in turn runs the user code as soon as the required user-input boolean is there
                UI.setCurrent(ui); // in case the command uses 'UI.getCurrent()' assign the parent thread's UI to this new child thread.
                while (!command.done) { // repeat until it is finally done.
                    try {
                        ui.access(command).get(); // and use the UI instance in a child thread, block this thread until the UI thread tried another time.
                        // network usage: make sure that there is no polling set so that we only piggy-back upon the client-server communications that take
                        // place after some user action such as a click.
                        // Using Vaadin Push, we would not need such a piggyback: one could push UI changes out to the client using UI.push().
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start(); // one may consider instead using a thread pool, see ExecutorService for example.

            // functional identity mapping with a little side effect
            future.thenApply(value -> {
                dialog.close();
                return value;
            });
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
