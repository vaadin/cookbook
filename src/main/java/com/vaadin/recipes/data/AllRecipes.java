package com.vaadin.recipes.data;

import com.vaadin.flow.router.Route;
import com.vaadin.recipes.Application;
import com.vaadin.recipes.Util;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.util.ArrayList;
import java.util.List;
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
        if (route == null || route.value().equals(Route.NAMING_CONVENTION)) {
            throw new IllegalArgumentException(
                "The class " + recipeClass.getName() + " must have a @Route annotation with a value"
            );
        }
        List<String> sourceFiles = new ArrayList<>();
        sourceFiles.add(Util.getSourceFile(recipeClass));
        for (String additionalFile : recipe.sourceFiles()) {
            if (recipeClass.getResource(additionalFile) != null) {
                sourceFiles.add(Util.getSourceOrResourceFile(recipeClass, additionalFile));
            } else {
                sourceFiles.add(Util.getFrontendFile(additionalFile));
            }
        }

        RecipeInfo recipeInfo = new RecipeInfo(
            recipeClass,
            route.value(),
            recipe.howdoI(),
            recipe.description(),
            sourceFiles,
            recipe.tags()
        );
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
                Class cls = Class.forName(beanName);
                if (!Recipe.class.isAssignableFrom(cls)) {
                    throw new IllegalStateException("Recipe " + cls.getName() + " must extend Recipe");
                }
                addRecipe((Class<? extends Recipe>) cls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTitle(RecipeInfo recipeInfo) {
        return getHowDoI(recipeInfo) + " - Vaadin Cookbook";
    }

    private static String getHowDoI(RecipeInfo recipeInfo) {
        return "How do I " + recipeInfo.getHowDoI();
    }

    public static String getDescription(RecipeInfo recipeInfo) {
        String description = recipeInfo.getDescription();
        return description != null && !description.isEmpty() ? description : getHowDoI(recipeInfo);
    }
}
