package com.vaadin.recipes.data;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

public class RecipeInfo {
    @JsonIgnore
    private Class<? extends Recipe> recipeClass;

    @NonNull
    private String howDoI;

    @NotBlank
    private String description;

    private String url;

    @Nullable
    private List<String> sourceFiles;

    @Nullable
    private List<@NonNull Tag> tags;

    public RecipeInfo() {}

    public RecipeInfo(
        Class<? extends Recipe> recipeClass,
        String url,
        String howDoI,
        String description,
        List<String> sourceFiles,
        Tag[] tags
    ) {
        this.url = url;
        if (howDoI.toLowerCase().startsWith("how do i ")) {
            howDoI = howDoI.substring("how do i ".length());
        }
        this.howDoI = firstToLower(howDoI);
        this.description = description;
        this.sourceFiles = sourceFiles;
        this.recipeClass = recipeClass;
        this.tags = new ArrayList<>();
        Set<Tag> uniqueTags = new LinkedHashSet<>();
        uniqueTags.add(Tag.JAVA);
        uniqueTags.add(Tag.FLOW);
        uniqueTags.addAll(Arrays.asList(tags));
        this.tags.addAll(uniqueTags);
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
        return (
            "RecipeInfo [description=" +
            description +
            ", howDoI=" +
            howDoI +
            ", recipeClass=" +
            recipeClass +
            ", sourceFiles=" +
            sourceFiles +
            ", tags=" +
            tags +
            ", url=" +
            url +
            "]"
        );
    }
}
