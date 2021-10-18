package com.vaadin.recipes.recipe.responsivegrid;

import java.time.LocalDate;

public class Person {
    private String name;
    private String title;
    private String email;
    private LocalDate date;

    public Person(String name, String title, String email, LocalDate date) {
            super();
            this.name = name;
            this.title = title;
            this.email = email;
            this.date = date;
    }

    public Person() {

    }

    public String getName() {
            return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public String getTitle() {
            return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    public String getEmail() {
            return email;
    }

    public void setEmail(String email) {
            this.email = email;
    }

    public LocalDate getDate() {
            return date;
    }

    public void setDate(LocalDate date) {
            this.date = date;
    }
}
