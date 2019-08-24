package com.example.gameintel.classes;

import com.google.firebase.database.PropertyName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private String name;
    private String genre;
    private String image;

    public Game(){
        //empty constructor needed
    }

    public Game(String name,String genre,String image) {
        this.name = name;
        this.genre = genre;
        this.image=image;
    }



    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getImage(){
        return image;
    }


}
