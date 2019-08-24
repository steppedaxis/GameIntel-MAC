package com.example.gameintel.classes;

import java.util.Date;

public class User {
    private String username;
    private String name;
    private String email;
    private int age;
    private String birthdateYear;


    public User() {
    }

    public User(String username,String name, String email, int age, String birthdateYear) {
        this.username=username;
        this.name = name;
        this.email = email;
        this.age = age;
        this.birthdateYear = birthdateYear;
    }

    public String getUserName()
    {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getBirthdateYear() {
        return birthdateYear;
    }
}
