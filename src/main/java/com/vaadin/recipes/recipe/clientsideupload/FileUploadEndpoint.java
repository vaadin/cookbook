package com.vaadin.recipes.recipe.clientsideupload;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileUploadEndpoint {

    @PostMapping("/api/fileupload")
    public void handleFileUpload(@RequestParam("file") MultipartFile uploadedFile) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/uploadedFile");
        uploadedFile.transferTo(file);
    }
}
