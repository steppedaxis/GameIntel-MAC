package com.example.gameintel.classes;

import java.util.List;

public class Game {
    private String name;
    private String search_text;
    private List<String> genre;
    private String developer;
    private String publisher;
    private String description;
    private String releaseDate;
    private String series;
    private List<String> platforms;
    private String image;
    private int age;

    public Game(){
        //empty constructor needed
    }

    public Game(String name,String search_text ,List<String> genre, String developer, String publisher, String description, String releaseDate , String series, List<String> platforms, String image,int age) {
        this.name = name;
        this.search_text=search_text;
        this.genre=genre;
        this.developer = developer;
        this.publisher = publisher;
        this.description = description;
        this.releaseDate = releaseDate;
        this.series = series;
        this.platforms = platforms;
        this.image = image;
        this.age=age;
    }

    public String getName() {
        return name;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getImage(){
        return image;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSeries() {
        return series;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public String getSearch_text(){
        return search_text;
    }

    public int getAge(){
        return age;
    }


}
