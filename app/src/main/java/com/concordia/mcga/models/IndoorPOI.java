package com.concordia.mcga.models;

import com.concordia.mcga.utilities.pathfinding.IndoorMapTile;
import com.google.android.gms.maps.model.LatLng;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class IndoorPOI extends POI {
    private Floor floor;
    private IndoorMapTile tile;

    public IndoorPOI(LatLng mapCoordinates, String name, IndoorMapTile tile) {
        super(mapCoordinates, name);
        this.tile = tile;
    }

    public IndoorMapTile getTile() {
        return tile;
    }

    public void setTile(IndoorMapTile tile) {
        this.tile = tile;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
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
            .append(floor, indoorPOI.floor)
            .append(tile, indoorPOI.tile)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(floor)
            .append(tile)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("floor", floor)
            .append("tile", tile)
            .toString();
    }

}
