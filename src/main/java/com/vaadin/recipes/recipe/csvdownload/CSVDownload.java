package com.vaadin.recipes.recipe.csvdownload;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.FieldSet;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.recipes.recipe.gridcsvimport.GridCsvImport;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Route("csv-download")
@Metadata(
    howdoI = "Download text area contents as a file",
    description = "Shows how to allow the user to download a file generated from the contents of a TextArea.",
    tags = { Tag.DOWNLOAD }
)
public class CSVDownload extends Recipe {

    public CSVDownload() {
        TextArea text = createTextArea(loadExampleTextFromFile());
        add(text);

        add(new Div("Download Link:"));
        add(createDownloadAnchor(text));
        add(new Div("Download Button:"));
        add(createWrappedDownloadButton(text));

        add(createOtherExamplesSection());
    }

    private TextArea createTextArea(String initialValue) {
        TextArea text = new TextArea("Content");
        text.setValue(initialValue);
        text.setWidth("100%");
        text.setMaxHeight("300px");
        return text;
    }

    private static Anchor createDownloadAnchor(TextArea text) {
        // Create a download handler that collects text from the text area, converts it to input stream, and provides a download response.
        DownloadHandler downloadHandlerAnchor = DownloadHandler.fromInputStream(event -> {
            InputStream inputStream = new ByteArrayInputStream(text.getValue().getBytes());
            return new DownloadResponse(inputStream, "output.txt", "text/plain", -1);
        });

        // Create a link with our download handler
        return  new Anchor(downloadHandlerAnchor, "Download");
    }

    private static Anchor createWrappedDownloadButton(TextArea text) {
        // Create a download handler that collects text from the text area, converts it to input stream, and provides a download response.
        DownloadHandler downloadHandlerButton = DownloadHandler.fromInputStream(event -> {
            InputStream inputStream = new ByteArrayInputStream(text.getValue().getBytes());
            return new DownloadResponse(inputStream, "output.txt", "text/plain", -1);
        });

        // Create a button which will trigger download
        Button downloadButton = new Button("Download", VaadinIcon.DOWNLOAD.create());
        // Create a link with our download handler (triggered on button click)
        Anchor wrappedDownloadButton = new Anchor(downloadHandlerButton, "");
        // Wrap the button in the link
        wrappedDownloadButton.add(downloadButton);
        return wrappedDownloadButton;
    }

    private String loadExampleTextFromFile() {
        try {
            return IOUtils.toString(GridCsvImport.class.getResource("input.csv"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Unable to load data";
        }
    }

    private FieldSet createOtherExamplesSection() {
        var fieldSet = new FieldSet("Related recipes:");
        fieldSet.getStyle().setBorder("1px solid var(--lumo-contrast-30pct)");
        fieldSet.getLegend().getStyle().setPaddingLeft("5px");

        var content = new VerticalLayout();
        content.add(new Anchor("/dynamic-download", " Offer a dynamic / generated file for download."));
        content.add(new Anchor("/grid-csv-export", "Export grid data as a CSV file."));
        fieldSet.add(content);
        return fieldSet;
    }
}
