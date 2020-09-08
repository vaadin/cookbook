package com.vaadin.recipes.recipe.longrunningtask;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class BackendService {

    @Async // runs in a separate thread (enable with @EnableAsync in a config class)
    public ListenableFuture<Void> longRunningTask() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            return AsyncResult.forExecutionException(new RuntimeException("Error"));
        }

        return AsyncResult.forValue(null);
    }
}
