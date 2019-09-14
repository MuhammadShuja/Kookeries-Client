package com.kookeries.shop.models;

public class Slide {
    private static final String TAG = "Model/Slide";

    private String image;
    private String color;
    private String action;

    public Slide(String image, String color, String action) {
        this.image = image;
        this.color = color;
        this.action = action;
    }

    public String getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }

    public String getAction() {
        return action;
    }
}
