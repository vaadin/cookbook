package com.vaadin.recipes.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

public class RecipeInfo {

    @JsonIgnore
    private Class<? extends Recipe> recipeClass;
    private String howDoI;
    @Nullable
    private String description;
    private String url;
    @Nullable
    private List<String> sourceFiles;
    @Nullable
    private List<Tag> tags;

    public RecipeInfo() {

    }

    public RecipeInfo(Class<? extends Recipe> recipeClass, String url, String howDoI, String description,
            List<String> sourceFiles) {
        this.url = url;
        this.howDoI = firstToLower(howDoI);
        this.description = description;
        this.sourceFiles = sourceFiles;
        this.recipeClass = recipeClass;
        tags = new ArrayList<>();
        tags.add(Tag.JAVA);
    }

    private static String firstToLower(String howDoI) {
        if (howDoI == null || howDoI.length() < 2) {
            return howDoI;
        }
        return howDoI.substring(0, 1).toLowerCase(Locale.ENGLISH) + howDoI.substring(1);
    }

    public String getHowDoI() {
        return howDoI;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    public Class<? extends Recipe> getRecipeClass() {
        return recipeClass;
    }

    public String getDescription() {
        return description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "RecipeInfo [description=" + description + ", howDoI=" + howDoI + ", recipeClass=" + recipeClass
                + ", sourceFiles=" + sourceFiles + ", tags=" + tags + ", url=" + url + "]";
    }

}