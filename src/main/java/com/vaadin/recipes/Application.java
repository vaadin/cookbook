package com.vaadin.recipes;

import com.vaadin.recipes.data.AllRecipes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@EnableAsync
public class Application extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AllRecipes.scan();
    }
}
