package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class StudentSpot extends POI {
    private float rating;
    private String description;
    private String address;
    private int resId;
    private double lastKnownDistance;

    /**
     * Each student spot comprises a coordinate, name, rating, description, address, and image
     * @param mapCoordinates Location of the desired student spot
     * @param name Name of the spot, eg. "Arby's"
     * @param rating Rating of the spot from 0.0 to 5.0
     * @param description Description of what type of spot this is
     * @param address Textual address of the spot
     * @param resId Image resource displaying the spot
     */
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

    public double getLastKnownDistance() {
        return lastKnownDistance;
    }

    public void setLastKnownDistance(double lastKnownDistance) {
        this.lastKnownDistance = lastKnownDistance;
    }
}
