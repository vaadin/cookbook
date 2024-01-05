package com.vaadin.recipes.recipe.uploadvalidator;

import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("upload-validation")
@Metadata(
        howdoI = "Validate the contents of an uploaded file",
        description = "This example shows how to validate the contents of an uploladed file while it's being uploaded file and in this simple example it just rejects the upload if the first byte is not the character a."
)
public class UploadValidation extends Recipe {
    public UploadValidation() {
        Upload upload = new Upload(this::receiveUpload);

        upload.addSucceededListener(event -> Notification
                .show("Upload succeeded: " + event.getFileName()));
        upload.addFailedListener(event -> Notification
                .show("Upload failed: " + event.getFileName()));
        add(upload);
    }

    public OutputStream receiveUpload(String originalFileName,
            String MIMEType) {
        return new OutputStream() {
            boolean firstReceived = false;

            @Override
            public void write(int b) throws IOException {
                throw new UnsupportedOperationException("Upload always writes chunks");
            }
            
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                if (!firstReceived) {
                    byte first = b[off];
                    if (first != 'a') {
                        // A real example might delete any temporary data here
                        throw new IOException("Invalid file content");
                    }
                    firstReceived = true;
                }

                // A real example would write the data to a sink here
            }
            
            @Override
            public void close() throws IOException {
                // A real example would close the downstream sink here
            }
        };
    }
}