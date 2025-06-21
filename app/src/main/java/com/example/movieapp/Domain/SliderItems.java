package com.example.movieapp.Domain;

public class SliderItems {

    private int image;
    private String title;
    private String genre;

    private String VideoUrl;

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public SliderItems(int image, String title, String genre, String videoUrl) {
        this.image = image;
        this.title= title;
        this.genre=genre;
        this.VideoUrl=videoUrl;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
