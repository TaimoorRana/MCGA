package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class IndoorPOI extends POI {
    private int floorNumber;
    private int indoorCoordinateX;
    private int indoorCoordinateY;

    public IndoorPOI(LatLng mapCoordinates, String name,
        int floorNumber, int indoorCoordinateX, int indoorCoordinateY) {
        super(mapCoordinates, name);
        this.floorNumber = floorNumber;
        this.indoorCoordinateX = indoorCoordinateX;
        this.indoorCoordinateY = indoorCoordinateY;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getIndoorCoordinateX() {
        return indoorCoordinateX;
    }

    public void setIndoorCoordinateX(int indoorCoordinateX) {
        this.indoorCoordinateX = indoorCoordinateX;
    }

    public int getIndoorCoordinateY() {
        return indoorCoordinateY;
    }

    public void setIndoorCoordinateY(int indoorCoordinateY) {
        this.indoorCoordinateY = indoorCoordinateY;
    }
}
