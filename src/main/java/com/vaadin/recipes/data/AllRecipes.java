package com.vaadin.recipes.data;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.router.Route;
import com.vaadin.recipes.Application;
import com.vaadin.recipes.Util;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.SourceFiles;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

@Component
public class AllRecipes implements ApplicationListener<ContextRefreshedEvent> {

    private List<RecipeInfo> allRecipes = new ArrayList<>();

    public void addRecipe(Class<? extends com.vaadin.flow.component.Component> recipeClass) {
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
                    throw new IllegalArgumentException(
                            "Currently only java classes in the same package as the recipe are supported. Create an issue at https://github.com/vaadin/recipes/issues");
                }
            }
        }
        RecipeInfo recipeInfo = new RecipeInfo(route.value(), recipe.howdoI(), sourceFiles);
        allRecipes.add(recipeInfo);
    }

    public List<RecipeInfo> getRecipes() {
        return allRecipes;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Metadata.class));

        for (BeanDefinition beanDef : provider.findCandidateComponents(Application.class.getPackage().getName())) {
            String beanName = beanDef.getBeanClassName();
            try {
                addRecipe((Class<? extends com.vaadin.flow.component.Component>) Class.forName(beanName));
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}