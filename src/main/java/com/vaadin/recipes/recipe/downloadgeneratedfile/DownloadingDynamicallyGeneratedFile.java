package com.vaadin.recipes.recipe.downloadgeneratedfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Route("dynamic-download")
@Metadata(
        howdoI = "Offer a dynamic / generated file for download",
        description = "Shows how to allow the user to download a file generated on the server.",
        tags = { Tag.DOWNLOAD }
)
public class DownloadingDynamicallyGeneratedFile extends Recipe {

    private TextField name = new TextField("Name");
    private IntegerField yearOfBirth = new IntegerField("Year of Birth");

    public DownloadingDynamicallyGeneratedFile() {
        // A tiny form editing a DTO that will be dynamically
        // converted to a JSON file when downloaded
        add(name, yearOfBirth);
        var binder = new Binder<>(PersonDto.class);
        binder.bindInstanceFields(this);
        var person = new PersonDto();
        binder.setBean(person);

        var downloadHandler = DownloadHandler.fromInputStream(downloadEvent -> {
            var outputValue = binder.getBean();
            var outputStream = new ByteArrayOutputStream();
            // Using Jackson here to generate JSON to the output stream,
            // but could be just as well be XML, image or PDF
            var objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, outputValue);

            // Convert output stream to input stream
            var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return new DownloadResponse(inputStream,
                    name.getValue().replaceAll(" ", "_") + ".json", // dynamic file name
                    "application/json", // content type
                    -1); // content length (-1 = unknown)
        });

        var link = new Anchor(downloadHandler, "Download as JSON");
        add(link);
    }

    public static class PersonDto {
        private String name = "Carl Gustav";
        private int yearOfBirth = 1969;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getYearOfBirth() {
            return yearOfBirth;
        }

        public void setYearOfBirth(int yearOfBirth) {
            this.yearOfBirth = yearOfBirth;
        }
    }

}