package com.vaadin.recipes.data;

import java.util.List;

public class RecipeInfo {

    private String howDoI;
    private String url;
    private List<String> sourceFiles;

    public RecipeInfo() {

    }

    public RecipeInfo(String url, String howDoI, List<String> sourceFiles) {
        this.url = url;
        this.howDoI = howDoI;
        this.sourceFiles = sourceFiles;
    }

    public String getHowDoI() {
        return howDoI;
    }

    public void setHowDoI(String howDoI) {
        this.howDoI = howDoI;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<String> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }
}