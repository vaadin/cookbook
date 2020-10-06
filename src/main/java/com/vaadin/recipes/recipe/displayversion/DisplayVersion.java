package com.vaadin.recipes.recipe.displayversion;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;


@Route("display-version")
@Metadata(
        howdoI = "Show application and Vaadin versions in UI",
        description = "150-160 character description that is shown on the listing page and Google search results.",
        sourceFiles = "AppVersions.java"

)
public class DisplayVersion extends Recipe {

    /** Use Spring Autowired configuration to instantiate AppVersions.
     *
     *
     * @see AppVersions
     */
    public DisplayVersion(@Autowired AppVersions versions) {

        add(new Text("Cookbook "+ versions.getVersion()+". " +
                "Built with Vaadin "+ versions.getVaadinVersion()+
                " and Java " +versions.getJavaVersion() +
                " at "+versions.getBuildTime()));
    }


}
