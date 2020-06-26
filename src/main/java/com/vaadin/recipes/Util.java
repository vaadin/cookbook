package com.vaadin.recipes;

import java.io.File;

public class Util {

    public static String getSourceFile(Class<?> javaClass) {
        return javaClass.getName().replace(".", "/") + ".java";
    }

    public static String getSimpleName(String fullName) {
        return new File(fullName).getName();

    }
}
