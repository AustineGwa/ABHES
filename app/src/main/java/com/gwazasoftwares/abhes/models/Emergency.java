package com.gwazasoftwares.abhes.models;

public class Emergency {
    private int Image;
    private String title;
    private String description;

    public Emergency() {
    }

    public Emergency(int image, String title, String description) {
        Image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
