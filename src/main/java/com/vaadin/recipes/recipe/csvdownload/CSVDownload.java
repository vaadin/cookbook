package com.vaadin.recipes.recipe.csvdownload;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.recipes.recipe.gridcsvimport.GridCsvImport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.vaadin.olli.FileDownloadWrapper;

@Route("csv-download")
@Metadata(
    howdoI = "Download text area contents as a file",
    description = "Shows how to allow the user to download a file generated from the contents of a TextArea.",
    tags = { Tag.DOWNLOAD }, addons = {"FileDownloadWrapper;https://vaadin.com/directory/component/file-download-wrapper"}
)
public class CSVDownload extends Recipe {

    public CSVDownload() {
        add(new Anchor("/dynamic-download", "See this example if you want to create a download link to a dynamically generated file."));
        add(new Anchor("/grid-csv-export", "See this example if you want to export grid data as a CSV file."));

        String csvData;
        try {
            csvData = IOUtils.toString(GridCsvImport.class.getResource("input.csv"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            csvData = "Unable to load data";
        }
        TextArea text = new TextArea();
        text.setValue(csvData);
        text.setWidth("100%");
        text.setMaxHeight("500px");
        add(text);

        StreamResource resource = new StreamResource(
            "my.csv",
            () -> new ByteArrayInputStream(text.getValue().getBytes())
        );

        Anchor link = new Anchor(resource, "Download text area contents as a file using a link");

        Button downloadButton = new Button("Download text area contents as a file using a button");
        FileDownloadWrapper downloadButtonWrapper = new FileDownloadWrapper(resource);
        downloadButtonWrapper.wrapComponent(downloadButton);

        add(new Paragraph());
        add(link);
        add(new Paragraph());
        add(downloadButtonWrapper);
    }
}
