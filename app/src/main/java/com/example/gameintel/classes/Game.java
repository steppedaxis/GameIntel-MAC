package com.example.gameintel.classes;

public class Game {
    private String name;
    private String genre;
    private String developer;
    private String publisher;
    private String description;
    private String releaseDate;
    private String[] subGenres;
    private String series;
    private String[] platforms;
    private String image;

    public Game(){
        //empty constructor needed
    }

    public Game(String name, String genre, String developer, String publisher, String description, String releaseDate, String[] subGenres, String series, String[] platforms, String image) {
        this.name = name;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.description = description;
        this.releaseDate = releaseDate;
        this.subGenres = subGenres;
        this.series = series;
        this.platforms = platforms;
        this.image = image;
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

    public int getSubGenres() {
        return subGenres;
    }

    public String getSeries() {
        return series;
    }

    public String[] getPlatforms() {
        return platforms;
    }
}
