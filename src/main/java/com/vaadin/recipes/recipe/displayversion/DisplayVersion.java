package com.vaadin.recipes.recipe.displayversion;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;

@Route("display-version")
@Metadata(
    howdoI = "Show application Java and Vaadin versions in UI",
    description = "Using Spring Boot application.properties and Maven build to automatically expand project, Vaadin and Java versions and build time into properties and display in UI.",
    sourceFiles = { "AppVersions.java", "pom.xml", "application.properties" }
)
public class DisplayVersion extends Recipe {

    /**
     * Use Spring Autowired configuration to instantiate AppVersions.
     *
     *
     * @see AppVersions
     */
    public DisplayVersion(@Autowired AppVersions versions) {
         TextArea info = new TextArea(null,
                "Cookbook build info." +
                "\nVersion: " + versions.getVersion() +
                ". " +
                "\nVaadin: " +
                versions.getVaadinVersion() +
                "\nJava: " +
                versions.getJavaVersion() +
                "\nBuilt time: " +
                versions.getBuildTime(),
                    (String)null);
         info.setWidth("300px");
         info.setReadOnly(true);
         add(info);
    }
}
