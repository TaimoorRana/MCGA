package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class Campus extends POI {
    public static final Campus LOYOLA = new Campus(new LatLng(45.458563, -73.640156), "Loyola Campus", "LOY");
    public static final Campus SGW = new Campus(new LatLng(45.497100, -73.579077), "SGW Campus", "SGW");

    private String shortName;

    public Campus(LatLng mapCoordinates, String name, String shortName) {
        super(mapCoordinates, name);
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
