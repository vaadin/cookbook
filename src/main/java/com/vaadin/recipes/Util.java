package com.vaadin.recipes;

import java.io.File;

public class Util {

    public static String getSourceFile(Class<?> javaClass) {
        return javaClass.getName().replace(".", "/") + ".java";
    }

    public static String getSourceFile(Class<?> recipeClass, String name) {
        return recipeClass.getPackage().getName().replace(".", "/") + "/" + name;
    }

    public static String getFrontendFile(String name) {
        return name;
    }

    public static String getSimpleName(String fullName) {
        return new File(fullName).getName();

    }
}
