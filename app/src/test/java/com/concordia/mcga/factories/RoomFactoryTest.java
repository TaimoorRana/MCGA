package com.concordia.mcga.factories;

import static com.concordia.mcga.factories.RoomFactory.ROOM_ICON_COLUMN_INDEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.database.Cursor;

import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.models.Room.RoomIcon;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(RoomFactory.class)
public class RoomFactoryTest {
    private Gson gson = new Gson();

    @Test
    public void createRoom() throws Exception {
        Cursor res = Mockito.mock(Cursor.class);

        LatLng latLng = new LatLng(0, 0);
        String name = "Testo";
        String roomNumber = "12";
        List<LatLng> polygonCoords = new ArrayList<>();
        IndoorMapTile mapTile = new IndoorMapTile(0,0);
        RoomIcon roomIcon = RoomIcon.NONE;
        int floorNumber = 1;


        Mockito.when(res.getString(RoomFactory.CENTER_COORDINATE_COLUMN_INDEX)).thenReturn(gson.toJson(latLng));
        Mockito.when(res.getString(RoomFactory.NAME_COLUMN_INDEX)).thenReturn(name);
        Mockito.when(res.getString(RoomFactory.ROOM_NUMBER_COLUMN_INDEX)).thenReturn(roomNumber);
        Mockito.when(res.getString(RoomFactory.INDOORMAPTILE_COLUMN_INDEX)).thenReturn(gson.toJson(mapTile));
        Mockito.when(res.getInt(RoomFactory.FLOOR_NUMBER_COLUMN_INDEX)).thenReturn(floorNumber);
        Mockito.when(res.getString(RoomFactory.POLYGON_COORDINATE_COLUMN_INDEX)).thenReturn(gson.toJson(polygonCoords));
        Mockito.when(res.getString(RoomFactory.ROOM_ICON_COLUMN_INDEX)).thenReturn(roomIcon.toString());

        Room result = RoomFactory.createRoom(res);

        assertNotNull(result);
        assertEquals(latLng, result.getMapCoordinates());
        assertEquals(name, result.getName());
        assertEquals(roomNumber, result.getRoomNumber());
        assertEquals(floorNumber, result.getFloorNumber());
        assertEquals(mapTile, result.getTile());
        assertEquals(polygonCoords, result.getPolygonCoordinates());
        assertEquals(roomIcon, result.getRoomIcon());
    }
}

