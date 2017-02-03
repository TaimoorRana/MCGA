package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Location {
    private LatLng mapCoordinates;


    public LatLng getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(LatLng mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        return new EqualsBuilder()
            .append(mapCoordinates, location.mapCoordinates)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(mapCoordinates)
            .toHashCode();
    }

}
