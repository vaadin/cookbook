package com.vaadin.recipes.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.recipes.recipe.Recipe;

public class RecipeInfo {

    @JsonIgnore
    private Class<? extends Recipe> recipeClass;
    private String howDoI;
    private String url;
    private List<String> sourceFiles;

    public RecipeInfo() {

    }

    public RecipeInfo(Class<? extends Recipe> recipeClass, String url, String howDoI, List<String> sourceFiles) {
        this.url = url;
        this.howDoI = howDoI;
        this.sourceFiles = sourceFiles;
        this.recipeClass = recipeClass;
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

    public Class<? extends Recipe> getRecipeClass() {
        return recipeClass;
    }

    @Override
    public String toString() {
        return "RecipeInfo [howDoI=" + howDoI + ", recipeClass=" + recipeClass + ", sourceFiles=" + sourceFiles
                + ", url=" + url + "]";
    }
}