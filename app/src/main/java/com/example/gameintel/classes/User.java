package com.example.gameintel.classes;

import java.util.Date;

public class User {
    private String image;
    private String username;
    private String name;
    private String email;
    private int age;
    private String birthdateYear;


    public User() {
    }

    public User(String username,String name, String email, int age, String birthdateYear,String image) {
        this.username=username;
        this.name = name;
        this.email = email;
        this.age = age;
        this.birthdateYear = birthdateYear;
        this.image=image;
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

    public String getImage() {
        return image; }
}
