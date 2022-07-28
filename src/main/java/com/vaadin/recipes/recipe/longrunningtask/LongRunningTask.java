package com.vaadin.recipes.recipe.longrunningtask;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Route("long-running-task")
@Metadata(
    howdoI = "Show a progress bar while a long task is running",
    description = "Learn how to display a progress bar while a long-running background process and update the UI asynchronously when it completes.",
    sourceFiles = { "BackendService.java" },
    tags = { Tag.PUSH, Tag.USABILITY }
)
public class LongRunningTask extends Recipe {
    
    // Make sure to add @Push to your AppShellConfigurator implementation.
    // For more details, see https://vaadin.com/docs/latest/advanced/server-push/.
    
    private final BackendService backendService;

    private Button button = new Button("Start long-running task");
    private ProgressBar progressBar = new ProgressBar();
    private Paragraph message = new Paragraph();

    public LongRunningTask(BackendService backendService) {
        this.backendService = backendService;

        button.setWidth("15em");
        progressBar.setWidth("15em");

        add(new HorizontalLayout(button, progressBar), message);
        button.addClickListener(event -> startLongRunningTask());

        // initial setup
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
    }

    private void startLongRunningTask() {
        // setup for starting the task
        button.setEnabled(false);
        progressBar.setVisible(true);
        message.setText("Please wait...");
        UI ui = UI.getCurrent(); // get the instance before running a new thread

        backendService
            .longRunningTask()
            .addCallback(
                new ListenableFutureCallback<>() {

                    @Override
                    public void onFailure(Throwable ex) {
                        ui.access(
                            () -> {
                                // setup for error
                                button.setEnabled(true);
                                progressBar.setVisible(false);
                                message.setText("Error.");
                            }
                        );
                    }

                    @Override
                    public void onSuccess(Void result) {
                        ui.access(
                            () -> {
                                // setup for task completed
                                button.setEnabled(true);
                                progressBar.setVisible(false);
                                message.setText("Task completed.");
                            }
                        );
                    }
                }
            );
    }
}
