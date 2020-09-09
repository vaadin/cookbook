package com.vaadin.recipes.recipe.notifychanges;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("notify-users")
@Metadata(howdoI = "Show a notification to active users", description = "Learn how to send a notification to all active users in your Vaadin application. Includes subscription handling code. ", tags = {
    Tag.PUSH })
public class NotifyUsers extends Recipe {
  private static final Set<SerializableConsumer<String>> subscribers = new HashSet<>();

  /*
   * Should preferably set up a proper container-managed worker, but this is a
   * simple approximation
   */
  private static final ExecutorService notifierThread = Executors.newSingleThreadExecutor();

  private SerializableConsumer<String> subscriber;
  private final Checkbox notifyMe = new Checkbox("Subscribe to notifications");

  public NotifyUsers() {
    notifyMe.addValueChangeListener(event -> updateSubscription());
    addAttachListener(event -> updateSubscription());
    addDetachListener(event -> updateSubscription());

    Button makeChanges = new Button("Notify subscribers",
        clickEvent -> notifySubscribers("This is a notification triggerd by the button"));

    add(notifyMe, makeChanges);
  }

  private void updateSubscription() {
    UI ui = getUI().orElse(null);

    // Subscribe if checkbox is checked and view is attached
    if (notifyMe.getValue() && ui != null) {
      if (subscriber != null) {
        // Already subscribed
        return;
      }

      subscriber = message -> ui.access(() -> Notification.show(message));
      synchronized (subscribers) {
        subscribers.add(subscriber);
      }
    } else {
      if (subscriber == null) {
        // Already unsubscribed
        return;
      }

      synchronized (subscribers) {
        subscribers.remove(subscriber);
      }
      subscriber = null;
    }
  }

  private static void notifySubscribers(String message) {
    Set<SerializableConsumer<String>> subscribersSnapshot;
    synchronized (subscribers) {
      subscribersSnapshot = new HashSet<>(subscribers);
    }

    for (SerializableConsumer<String> subscriber : subscribersSnapshot) {
      notifierThread.execute(() -> {
        try {
          subscriber.accept(message);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
  }
}
