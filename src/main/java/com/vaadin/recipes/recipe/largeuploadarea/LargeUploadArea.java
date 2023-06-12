package com.vaadin.recipes.recipe.largeuploadarea;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("large-upload-area")
@CssImport(value = "./recipe/large-upload-area/large-upload-area.css", themeFor = "vaadin-upload")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@CssImport(value = "./recipe/large-upload-area/file.css")
@Metadata(
    howdoI = "Create a large upload drop area",
    sourceFiles = { "recipe/large-upload-area/large-upload-area.css", "recipe/large-upload-area/file.css" },
    description = "Let users drag and drop files from the desktop on a large area in the application."
)
public class LargeUploadArea extends Recipe {

    public LargeUploadArea() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.getElement().getThemeList().add("custom-upload");
        upload.setWidth("100%");
        upload.setHeight("500px");
        upload.setMaxFileSize(100000);
        upload.setMaxFiles(20);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setClassName("drop-area");
        HorizontalLayout fileContainerLayout = new HorizontalLayout();
        verticalLayout.add(new Span("You drag and drop small files here!"), fileContainerLayout);

        verticalLayout.setSizeFull();

        upload.addSucceededListener(
            event -> {
                Div droppedFile = new Div();
                droppedFile.setClassName("file-div");
                Span deleteBadge = new Span("X");
                deleteBadge.setClassName("delete-button");
                deleteBadge.getElement().getThemeList().add("badge contrast pill");
                deleteBadge
                    .getElement()
                    .addEventListener(
                        "click",
                        e -> {
                            fileContainerLayout.remove(droppedFile);
                        }
                    );
                Span fileNameSpan = new Span();
                fileNameSpan.setText(event.getFileName());
                droppedFile.add(deleteBadge, fileNameSpan);
                fileContainerLayout.add(droppedFile);
            }
        );
        upload.addFileRejectedListener(
            event -> {
                Notification notification = new Notification();
                notification.setDuration(3000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setText("Upload failed: " + event.getErrorMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        );
        upload.getElement().appendChild(verticalLayout.getElement());

        add(upload);
    }
}
