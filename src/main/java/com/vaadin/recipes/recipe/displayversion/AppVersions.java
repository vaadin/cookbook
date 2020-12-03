package com.vaadin.recipes.recipe.displayversion;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/** Load properties from application.properties.
 *
 * Properties are automatically filled in Maven build using resource filtering.
 *
 * In <code>pom.xml</code>
 * <pre>
 *     <build>
 *         <resources>
 *             <resource>
 *                 <directory>src/main/resources</directory>
 *                 <filtering>true</filtering>
 *             </resource>
 * </pre>
 *
 * and in <code>application.properties</code>
 * <pre>
 * app.version=@project.version@
 * app.build-time=@maven.build.timestamp@
 * app.vaadin.version=@vaadin.version@
 * app.java.version=@java.version@
 * </pre>
 *
 * @link https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/reference/html/howto-properties-and-configuration.html
 */
@PropertySource(value = {"classpath:application.properties"})
@Component
public class AppVersions {

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.buildtime}")
    private String appBuildTime;

    @Value("${app.vaadin.version}")
    private String appVaadinVersion;

    @Value("${app.java.version}")
    private String appJavaVersion;

    public String getVersion() {
        return appVersion;
    }

    public String getBuildTime() {
        return appBuildTime;
    }

    public String getVaadinVersion() {
        return appVaadinVersion;
    }

    public String getJavaVersion() {
        return appJavaVersion;
    }

}
