package com.vaadin.recipes.recipe.camera;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.io.ByteArrayInputStream;

@Route("camera")
@Metadata(
    howdoI = "Take a photo with my phone",
    description = "Let users capture and upload pictures with the camera on their mobile device. Customize the Vaadin upload component to capture photos."
)
public class CameraView extends Recipe {
    private Component previousPhoto;
    private Paragraph photoName;

    public CameraView() {
        var output = new Div();

        var uploadHandler = UploadHandler.inMemory(((metadata, data) -> {
            var component = createComponent(metadata.contentType(), metadata.fileName(), data);
            showOutput(metadata.fileName(), component, output);
        }));

        var upload = new Upload(uploadHandler);
        upload.setAcceptedFileTypes("image/*");
        // You can use the capture html5 attribute
        // https://caniuse.com/html-media-capture
        upload.getElement().setAttribute("capture", "environment");
        // If you don't compress the image, don't forget to increase the upload limit
        // and request size or you will have an error
        // For a spring boot application the default request size is 10MB
        // and the default upload size is 1MB
        // you can set it in application.properties:
        // spring.servlet.multipart.max-request-size=30MB
        // spring.servlet.multipart.max-file-size=30MB

        add(upload, output);
    }

    private Component createComponent(String contentType, String fileName, byte[] data) {
        if (contentType.startsWith("image")) {
            return new Image(DownloadHandler.fromInputStream(downloadEvent ->
                    new DownloadResponse(new ByteArrayInputStream(data), fileName, contentType, -1)), "Camera picture");
        }
        var content = new Div();
        var text = String.format("Content type: '%s'", contentType);
        content.setText(text);
        return content;
    }

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        if (photoName != null) {
            outputContainer.remove(photoName);
        }
        if (previousPhoto != null) {
            outputContainer.remove(previousPhoto);
        }
        photoName = new Paragraph(text);
        outputContainer.add(photoName);
        previousPhoto = content;
        outputContainer.add(previousPhoto);
    }
}
