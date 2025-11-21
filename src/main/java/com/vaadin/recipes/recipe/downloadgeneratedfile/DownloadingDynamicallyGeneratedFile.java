package com.vaadin.recipes.recipe.downloadgeneratedfile;

import tools.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import org.vaadin.firitin.components.DynamicFileDownloader;

@Route("dynamic-download")
@Metadata(
        howdoI = "Offer a dynamic / generated file for download",
        description = "Shows how to allow the user to download a file generated on the server.",
        tags = { Tag.DOWNLOAD }, addons = "Viritin;https://vaadin.com/directory/component/flow-viritin"
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

        // The actual download component and file generation
        add(new DynamicFileDownloader("Download as JSON", outputStream -> {
            // Using Jackson here to generate JSON to the output stream,
            // but could be just as well be XML, image or PDF
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, person);
        }).withFileNameGenerator(r -> {
            // Using a FileNameGenerator, you can customize the
            // name of the downloaded file
            return person.getName().replaceAll(" ", "_") + ".json";
        }));
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