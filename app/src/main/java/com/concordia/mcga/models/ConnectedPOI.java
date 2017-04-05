package com.concordia.mcga.models;

import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConnectedPOI {
    private Map<Integer, IndoorPOI> indoorPOIs;
    private int floorNumber;

    public ConnectedPOI(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public IndoorPOI getFloorPOI(int floorNumber) {
        return indoorPOIs.get(floorNumber);
    }

    public void setIndoorPOIs(Map<Integer, IndoorPOI> indoorPOIs) {
        this.indoorPOIs = indoorPOIs;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("indoorPOIs", indoorPOIs)
            .toString();
    }
}
