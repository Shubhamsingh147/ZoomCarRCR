package com.example.shubham.zoomcar.model;

/**
 * Created by shubham on 13/9/15.
 */


import java.util.ArrayList;

public class Car {
    private String title, thumbnailUrl;
    private int AC;
    private String rating;
    private int price;
    private int seater;
    private float latitude;
    private float longitude;
    public Car() {
    }

    public Car(String name, String thumbnailUrl, int AC, String rating,
                 int price, int seater, float latitude, float longitude) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.AC = AC;
        this.rating = rating;
        this.price = price;
        this.seater = seater;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getAC() {
        return AC;
    }

    public void setAC(int AC) {
        this.AC = AC;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeater() { return seater; }

    public void setSeater(int seater) { this.seater = seater;}

    public void setLatitude(float latitude) { this.latitude = latitude; }

    public float getLatitude() { return latitude;}

    public void setLongitude(float longitude) { this.longitude = longitude; }

    public float getLongitude() { return longitude; }

}
