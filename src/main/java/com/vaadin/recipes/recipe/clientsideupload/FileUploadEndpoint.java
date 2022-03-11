package com.vaadin.recipes.recipe.clientsideupload;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadEndpoint {

    @PostMapping("/api/fileupload")
    public void handleFileUpload(@RequestParam("file") MultipartFile uploadedFile) {
        /* Here you can do something with the uploaded file. For example:
        File file = new File(System.getProperty("user.dir") + "/uploadedFile");
        uploadedFile.transferTo(file);
         */
    }
}
