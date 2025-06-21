package com.example.movieapp.Models;

import java.util.ArrayList;




public class Movie {
    public String id;
    public String title;
    public String description;
    public int year;

    public int age;
    public ArrayList<String> genres;
    public double rating;
    public String imageUrl;


    public String videoUrl;


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public int getAge() {
        return age;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Movie() {}

    public ArrayList<String> getGenres() {
        return genres;
    }

    public Movie(String id, String title, String description, int year, int age, ArrayList<String> genres, double rating, String imageUrl, String videoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.year = year;
        this.age = age;
        this.genres = genres;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
