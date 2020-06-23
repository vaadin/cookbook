package com.vaadin.recipes.data;

public class RecipeInfo {

    private String title;
    private String url;

    public RecipeInfo() {

    }

    public RecipeInfo(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}