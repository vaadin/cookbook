package com.vaadin.recipes.recipe.griddatabaseerror;

import java.time.LocalDate;

public class Person {

    private String name;
    private LocalDate birthday;
    private String email;
    private int age;

    public Person(String name, LocalDate birthday, String email, int age) {
        super();
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.age = age;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
