package com.concordia.mcga.models;

import com.concordia.mcga.activities.BuildConfig;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;


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
    
    @Before
    public void setUp() {
        LatLng coord = new LatLng(5,10);
        polygonCoords.add(coord);
        room = new Room(latLng, name, mapTile, roomNumber, floorNumber, polygonCoords, roomIcon);

        floor = new Floor(null, floorNumber);
        room.setFloor(floor);
    }

    @Test
    public void testRoom_toJson() {
        JSONObject json = room.toJson();
        try {
            assertNotNull(json.get("roomName"));
            assertNotNull(json.get("xCoordinate"));
            assertNotNull(json.get("yCoordinate"));
            assertNotNull(json.get("floorNumber"));
            assertNotNull(json.get("polygonCoords"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
