package com.vaadin.recipes.recipe.uploadimagetofile;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("upload-image-to-file")
@Metadata(
        howdoI = "Upload an image to file on server",
        description = "Demonstrates how to upload an image to a temporary file on file system and show it after succesful upload."
)
public class UploadImageToFile extends Recipe {

    UploadImageToFile() {
        var output = new Div(new Text("(no image file uploaded yet)"));

        // Use UploadHandler.toFile(..) instead if you want it permanently saved.
        var handler = UploadHandler.toTempFile((metadata, data) -> {
            data.deleteOnExit(); // Delete the temporary file once Java exists.
            output.removeAll();
            output.add(new Text("Uploaded: " + metadata.fileName() + " to "+ data.getAbsolutePath() + " Type: " + metadata.contentType()));
            output.add(new Image(DownloadHandler.forFile(data),"Uploaded image"));
        }).whenComplete(((transferContext, success) -> {
            if (!success) {
                output.removeAll();
                output.add(new Text("Upload failed: " + transferContext.exception().getMessage()));
            }
        }));

        var upload = new Upload(handler);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        add(upload, output);
    }
}
