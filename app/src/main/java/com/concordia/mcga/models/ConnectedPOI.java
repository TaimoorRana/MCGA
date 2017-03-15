package com.concordia.mcga.models;

import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConnectedPOI {
    private Map<Integer, IndoorPOI> indoorPOIs;

    public IndoorPOI getFloorPOI(int floorNumber) {
        return indoorPOIs.get(floorNumber);
    }

    public void setIndoorPOIs(
        Map<Integer, IndoorPOI> indoorPOIs) {
        this.indoorPOIs = indoorPOIs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("indoorPOIs", indoorPOIs)
            .toString();
    }
}
