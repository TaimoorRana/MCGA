package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class StudentSpot extends POI {
    private RATING rating;
    private String description;
    private String address;

    private int resId;

    public enum RATING {
        NO_STARS    (0.0f),
        ONE_STAR    (1.0f),
        TWO_STARS   (2.0f),
        THREE_STARS (3.0f),
        FOUR_STARS  (4.0f),
        FIVE_STARS  (5.0f);

        private float ratingVal;

        RATING(float ratingVal) {
            this.ratingVal = ratingVal;
        }

        public float getRatingVal() {
            return this.ratingVal;
        }
    }

    public StudentSpot(LatLng mapCoordinates, String name, RATING rating, String description,
                       String address, int resId) {
        super(mapCoordinates, name);
        this.rating = rating;
        this.description = description;
        this.address = address;
        this.resId = resId;
    }

    public RATING getRating() {
        return rating;
    }

    public void setRating(RATING rating) {
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
