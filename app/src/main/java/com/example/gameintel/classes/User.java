package com.example.gameintel.classes;

import java.util.Date;
import java.util.List;

public class User {
    private String image;
    private String username;
    private String name;
    private String email;
    private int age;
    private String birthdateYear;
    private List<String> favoriteGames;


    public User() {
    }

    public User(String username,String name, String email, int age, String birthdateYear,String image,List<String> favoriteGames) {
        this.username=username;
        this.name = name;
        this.email = email;
        this.age = age;
        this.birthdateYear = birthdateYear;
        this.image=image;
        this.favoriteGames=favoriteGames;
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
        return image;
    }

    public List<String> getFavoriteGames() {
        return favoriteGames;
    }
}
