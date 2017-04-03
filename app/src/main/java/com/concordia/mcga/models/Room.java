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

    public enum RoomIcon {
        NONE,
        WASHROOM_MALE,
        WASHROOM_FEMALE
    }

    private String roomNumber;
    private int floorNumber;
    private List<LatLng> polygonCoordinates;
    private RoomIcon roomIcon;

    public Room(LatLng mapCoordinates, String name, IndoorMapTile tile, String roomNumber, int floorNumber, List<LatLng> polygonCoordinates, RoomIcon roomIcon) {
        super(mapCoordinates, name, tile);
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.polygonCoordinates = polygonCoordinates;
        this.roomIcon = roomIcon;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<LatLng> getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomIcon getRoomIcon() {
        return roomIcon;
    }

    public void setRoomIcon(RoomIcon roomIcon) {
        this.roomIcon = roomIcon;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray polygonCoordArray = new JSONArray();
        for (LatLng coord : polygonCoordinates) {
            JSONObject polygonCoord = new JSONObject();
            try {
                polygonCoord.put("lat", coord.latitude);
                polygonCoord.put("lng", coord.longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            polygonCoordArray.put(polygonCoord);
        }
        try {
            json.put("roomName", this.getName());
            json.put("xCoordinate", this.getTile().getCoordinateX());
            json.put("yCoordinate", this.getTile().getCoordinateY());
            json.put("floorNumber", this.getFloor().getFloorNumber());
            json.put("polygonCoords", polygonCoordArray);
            json.put("roomIcon", this.getRoomIcon().toString());
        } catch (JSONException e) {
            Log.e("IndoorMapTile Error", Log.getStackTraceString(e));
        }
        return json;
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

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", floorNumber=" + floorNumber +
                ", polygonCoordinates=" + polygonCoordinates +
                "} " + super.toString();
    }
}
