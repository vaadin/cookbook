package com.vaadin.recipes.data;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.router.Route;
import com.vaadin.recipes.Application;
import com.vaadin.recipes.Util;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.SourceFiles;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class AllRecipes {

    private static List<RecipeInfo> allRecipes = new ArrayList<>();

    private AllRecipes() {
        // Static use only
    }

    public static void addRecipe(Class<? extends Recipe> recipeClass) {
        Metadata recipe = recipeClass.getAnnotation(Metadata.class);
        Route route = recipeClass.getAnnotation(Route.class);
        SourceFiles additionalFiles = recipeClass.getAnnotation(SourceFiles.class);
        if (route == null || route.value().equals(Route.NAMING_CONVENTION)) {
            throw new IllegalArgumentException(
                    "The class " + recipeClass.getName() + " must have a @Route annotation with a value");
        }
        List<String> sourceFiles = new ArrayList<>();
        sourceFiles.add(Util.getSourceFile(recipeClass));
        if (additionalFiles != null) {
            for (String additionalFile : additionalFiles.value()) {
                if (additionalFile.endsWith(".java")) {
                    sourceFiles.add(Util.getSourceFile(recipeClass, additionalFile));
                } else {
                    sourceFiles.add(Util.getFrontendFile(additionalFile));
                }
            }
        }

        RecipeInfo recipeInfo = new RecipeInfo(recipeClass, route.value(), recipe.howdoI(), sourceFiles);
        allRecipes.add(recipeInfo);
    }

    public static List<RecipeInfo> getRecipes() {
        return allRecipes;
    }

    public static RecipeInfo getRecipeInfo(Class<? extends Recipe> recipeClass) {
        return allRecipes.stream().filter(recipe -> recipe.getRecipeClass() == recipeClass).findFirst().orElse(null);
    }

    public static void scan() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Metadata.class));

        for (BeanDefinition beanDef : provider.findCandidateComponents(Application.class.getPackage().getName())) {
            String beanName = beanDef.getBeanClassName();
            try {
                addRecipe((Class<? extends Recipe>) Class.forName(beanName));
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}