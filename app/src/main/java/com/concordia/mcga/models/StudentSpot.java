package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class StudentSpot extends POI {
    private float rating;
    private String description;
    private String address;
    private int resId;

    public StudentSpot(LatLng mapCoordinates, String name, float rating, String description,
                       String address, int resId) {
        super(mapCoordinates, name);
        this.rating = rating;
        this.description = description;
        this.address = address;
        this.resId = resId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
