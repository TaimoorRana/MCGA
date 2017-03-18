package com.concordia.mcga.models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Room extends IndoorPOI {

    private int roomNumber;
    private int floorNumber;
    private List<LatLng> polygonCoordinates;

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", floorNumber=" + floorNumber +
                ", polygonCoordinates=" + polygonCoordinates +
                "} " + super.toString();
    }

    public Room(LatLng mapCoordinates, String name, IndoorMapTile tile, int roomNumber, int floorNumber, List<LatLng> polygonCoordinates) {
        super(mapCoordinates, name, tile);
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.polygonCoordinates = polygonCoordinates;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<LatLng> getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

   /* public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("roomName", this.getName());
            json.put("lat", this.coordinateY);
            json.put("lng", this.coordinateX);
        } catch (JSONException e) {
            Log.e("IndoorMapTile Error", Log.getStackTraceString(e));
        }
        return json;
            JSONArray pftArray = new JSONArray();
            for (IndoorMapTile pft : pathTilesJunctions) {
                pftArray.put(pft.toJSON());
            }
            return pftArray;
    }*/

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
