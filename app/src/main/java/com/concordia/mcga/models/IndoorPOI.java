package com.concordia.mcga.models;

public class IndoorPOI extends POI {
    private int floorNumber;
    private int indoorCoordinateX;
    private int indoorCoordinateY;

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
