package com.vaadin.recipes.recipe.undoablesend;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Route("undoable-send")
@Metadata(
    howdoI = "Send with undo button",
    description = "Don't regret, undo. Learn how to create actions that can be undone in Vaadin apps.",
    tags = { Tag.PUSH }
)
public class UndoableSend extends Recipe {
    private static final Executor executor = CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS);

    public UndoableSend() {
        TextField messageField = new TextField("Message to send");
        Button sendButton = new Button("Send", e -> sendMessage(messageField.getValue()));
        add(messageField, sendButton);
    }

    private void sendMessage(String message) {
        UI ui = UI.getCurrent();

        Runnable sendMessage = () -> ui.access(() -> showSentNotification(message));
        CompletableFuture<Void> future = CompletableFuture.runAsync(sendMessage, executor);

        Button undoButton = new Button("Undo", e -> future.cancel(false));
        Notification undoNotification = new Notification(new Text("Message sent "), undoButton);
        undoNotification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        undoNotification.open();

        future.whenComplete((value, error) -> ui.access(undoNotification::close));
    }

    private void showSentNotification(String message) {
        Notification.show("Message has actually been sent: " + message, 5000, Notification.Position.MIDDLE);
    }
}
