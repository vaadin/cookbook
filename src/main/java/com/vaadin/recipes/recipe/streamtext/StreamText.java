package com.vaadin.recipes.recipe.streamtext;

import java.time.Duration;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Route("stream-text")
@Metadata(howdoI = "stream text content into a component", description = "Incrementally add more text to a component in an efficient way", tags = {
        Tag.FLOW, Tag.PUSH })
public class StreamText extends Recipe {
    private Disposable subscription;

    public StreamText() {
        Component target = new Paragraph("Content is streamed here.");

        Consumer<String> receiver = createStreamer(target);

        Flux<String> updates = Flux
                .<String> generate(sink -> sink.next(" Here's an update."))
                .delayElements(Duration.ofMillis(500));

        Checkbox toggle = new Checkbox("Streaming active", event -> {
            if (event.getValue()) {
                subscription = updates.subscribe(receiver);
            } else {
                subscription.dispose();
                subscription = null;
            }
        });

        add(toggle, target);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
        }
    }

    private Consumer<String> createStreamer(Component target) {
        UI ui = UI.getCurrent();

        return text -> {
            ui.access(() -> {
                target.getElement().executeJs("this.textContent += $0", text);
            });
        };
    }
}