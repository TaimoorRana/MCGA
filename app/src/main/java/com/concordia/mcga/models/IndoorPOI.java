package com.concordia.mcga.models;

import com.concordia.mcga.exceptions.MCGADifferentFloorException;
import com.google.android.gms.maps.model.LatLng;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class IndoorPOI extends POI {
    private IndoorMap indoorMap;
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

    public IndoorMap getIndoorMap() {
        return indoorMap;
    }

    public void setIndoorMap(IndoorMap indoorMap) {
        this.indoorMap = indoorMap;
    }

    public int calculateDistanceTo(IndoorPOI poi) throws MCGADifferentFloorException{
        if (!poi.getIndoorMap().equals(indoorMap)){
            throw new MCGADifferentFloorException("POIs are on different floors. Cannot calculate the distance.");
        }
        return Math.abs(indoorCoordinateX - poi.indoorCoordinateX) + Math.abs(indoorCoordinateY - poi.indoorCoordinateY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndoorPOI indoorPOI = (IndoorPOI) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(floorNumber, indoorPOI.floorNumber)
            .append(indoorCoordinateX, indoorPOI.indoorCoordinateX)
            .append(indoorCoordinateY, indoorPOI.indoorCoordinateY)
            .append(indoorMap, indoorPOI.indoorMap)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(indoorMap)
            .append(floorNumber)
            .append(indoorCoordinateX)
            .append(indoorCoordinateY)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("floorNumber", floorNumber)
            .append("indoorCoordinateX", indoorCoordinateX)
            .append("indoorCoordinateY", indoorCoordinateY)
            .toString();
    }
}
