package com.vaadin.recipes.recipe.largeuploadarea;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.io.IOException;
import java.io.InputStream;

@Route("large-upload-area")
@CssImport(value = "./recipe/large-upload-area/large-upload-area.css", themeFor = "vaadin-upload")
@Metadata(
    howdoI = "Create a large upload drop area",
    sourceFiles = { "recipe/large-upload-area/large-upload-area.css" },
    description = "Let users drag and drop files from the desktop on a large area in the application."
)
public class LargeUploadArea extends Recipe {

    public LargeUploadArea() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.getElement().getThemeList().add("custom-upload");
        upload.setWidth("100%");
        upload.setHeight("500px");
        upload.setAcceptedFileTypes("text/*");
        upload.setMaxFileSize(100000);
        upload.setMaxFiles(20);

        TextArea textArea = new TextArea();
        textArea.setValue("You can type or drop small text files here!");
        textArea.setSizeFull();

        upload.addSucceededListener(
            event -> {
                String value = textArea.getValue();
                if (value == null || value.isEmpty()) {
                    value = "";
                } else {
                    value += "\n";
                }
                InputStream inputStream = buffer.getInputStream();
                try {
                    value += new String(inputStream.readAllBytes());
                } catch (IOException e) {
                    value += e.getMessage();
                }
                textArea.setValue(value);
            }
        );
        upload.getElement().appendChild(textArea.getElement());

        add(upload);
    }
}
