package com.concordia.mcga.models;

import com.concordia.mcga.activities.BuildConfig;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RoomTest {

    private Room room;
    private Floor floor;

    private LatLng latLng = new LatLng(0, 0);
    private String name = "Testo";
    private String roomNumber = "12";
    private List<LatLng> polygonCoords = new ArrayList<>();
    private IndoorMapTile mapTile = new IndoorMapTile(0, 0);
    private Room.RoomIcon roomIcon = Room.RoomIcon.NONE;
    private int floorNumber = 1;

    private String expectedOutput = "{\"xCoordinate\":0,\"polygonCoords\":[{\"lng\":10,\"lat\":5}],\"roomIcon\":\"NONE\",\"yCoordinate\":0,\"floorNumber\":1,\"roomName\":\"Testo\"}";

    @Test
    public void testRoom_toJson() {
        LatLng coord = new LatLng(5,10);
        polygonCoords.add(coord);
        room = new Room(latLng, name, mapTile, roomNumber, floorNumber, polygonCoords, roomIcon);

        floor = new Floor(null, floorNumber);
        room.setFloor(floor);

        JSONObject json = room.toJson();
        assertEquals(json.toString(), expectedOutput);
    }
}
