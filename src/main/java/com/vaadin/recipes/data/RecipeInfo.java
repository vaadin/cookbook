package com.vaadin.recipes.data;

public class RecipeInfo {

    private String howDoI;
    private String url;

    public RecipeInfo() {

    }

    public RecipeInfo(String url, String howDoI) {
        this.url = url;
        this.howDoI = howDoI;
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
}