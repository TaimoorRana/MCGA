package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class StudentSpot extends POI {
    private RATING rating;
    private String description;

    public enum RATING {
        NO_STARS    (0.0),
        ONE_STAR    (1.0),
        TWO_STARS   (2.0),
        THREE_STARS (3.0),
        FOUR_STARS  (4.0),
        FIVE_STARS  (5.0);

        private double ratingVal;

        RATING(double ratingVal) {
            this.ratingVal = ratingVal;
        }

        public double getRatingVal() {
            return this.ratingVal;
        }
    }

    public StudentSpot(LatLng mapCoordinates, String name, RATING rating, String description) {
        super(mapCoordinates, name);
        this.rating = rating;
        this.description = description;
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
}
