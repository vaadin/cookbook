package com.vaadin.recipes.recipe.largeuploadarea;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.IOUtils;

@Route("large-upload-area")
@Metadata(
    howdoI = "Create a large upload drop area",
    description = "Let users drag and drop files from the desktop on a large area in the application."
)
public class LargeUploadArea extends Recipe {

    public LargeUploadArea() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setWidth("100%");
        upload.setHeight("500px");
        upload.setDropLabel(new Span("Drop files here"));
        upload.addSucceededListener(
            event -> {
                Notification.show("Received " + event.getFileName() + " successfully!");
            }
        );

        add(upload);
    }
}
