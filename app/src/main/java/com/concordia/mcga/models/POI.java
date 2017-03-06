package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class POI extends Location {
    private String name;


    public POI(LatLng mapCoordinates, String name) {
        super(mapCoordinates);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        POI poi = (POI) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(name, poi.name)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(name)
            .toHashCode();
    }
}
