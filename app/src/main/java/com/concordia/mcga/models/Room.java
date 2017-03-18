package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Room extends IndoorPOI {
    private int roomNumber;
    private int floorNumber;

    public Room(LatLng mapCoordinates, String name, IndoorMapTile tile, int roomNumber, int floorNumber) {
        super(mapCoordinates, name, tile);
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
    }

    public int getRoomNumber() {

        return roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Room room = (Room) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(roomNumber, room.roomNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(roomNumber)
                .toHashCode();
    }
}
