package com.vaadin.recipes.recipe.longrunningtask;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class BackendService {

    @Async // runs in a separate thread (enable with @EnableAsync in a config class)
    public CompletableFuture<Void> longRunningTask() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(new RuntimeException("Error"));
        }

        return CompletableFuture.completedFuture(null);
    }
}
