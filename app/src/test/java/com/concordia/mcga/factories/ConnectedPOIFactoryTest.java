package com.concordia.mcga.factories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.ConnectedPOI;
import com.concordia.mcga.models.Elevator;
import com.concordia.mcga.models.Escalator;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.models.Room.RoomIcon;
import com.concordia.mcga.models.Staircase;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static com.concordia.mcga.factories.RoomFactory.ROOM_ICON_COLUMN_INDEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectedPOIFactory.class, DatabaseConnector.class, RoomFactory.class})
public class ConnectedPOIFactoryTest {
    private Gson gson = new Gson();
    private SQLiteDatabase database;
    private Cursor cursor;
    private DatabaseConnector databaseConnector;
    private Room room1;
    private Room room2;
    private Building building;

    @Before
    public void setUp() throws Exception {
        // Setup mock
        databaseConnector = Mockito.mock(DatabaseConnector.class);
        PowerMockito.mockStatic(DatabaseConnector.class);
        Mockito.when(DatabaseConnector.getInstance()).thenReturn(databaseConnector);
        DatabaseConnector.setDatabaseConnector(databaseConnector);
        Mockito.when(databaseConnector.getDb()).thenReturn(database);

        cursor = Mockito.mock(Cursor.class);
        database = Mockito.mock(SQLiteDatabase.class);
        PowerMockito.mockStatic(RoomFactory.class);

        building = new Building(new LatLng(0, 0), "Test", "T", new MarkerOptions());
        room1 = new Room(new LatLng(0, 0), "Test", new IndoorMapTile(0, 0), "1", 1, new ArrayList<LatLng>(), RoomIcon.NONE);
        room2 = new Room(new LatLng(0, 0), "Test", new IndoorMapTile(0, 0), "2", 2, new ArrayList<LatLng>(), RoomIcon.NONE);
    }

    @Test
    public void createConnectedPOI_Escalator() throws Exception {
        Cursor res = Mockito.mock(Cursor.class);

        LatLng latLng = new LatLng(0, 0);
        int id = 1;
        String name = "Testo";
        int floorNumber = 1;
        String discriminatorString = "ESCALATOR";

        //ConnectedPOI
        Mockito.when(res.getInt(ConnectedPOIFactory.ID_COLUMN_INDEX)).thenReturn(id);
        Mockito.when(res.getString(ConnectedPOIFactory.NAME_COLUMN_INDEX)).thenReturn(building.getShortName());
        Mockito.when(res.getInt(ConnectedPOIFactory.FLOOR_NUMBER_COLUMN_INDEX)).thenReturn(floorNumber);
        Mockito.when(res.getString(ConnectedPOIFactory.DISCRIMINATOR_COLUMN_INDEX)).thenReturn(discriminatorString);

        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(res.moveToNext()).thenReturn(true, true, false);
        Mockito.when(res.getInt(Mockito.anyInt())).thenReturn(1);
        Mockito.when(RoomFactory.createRoomById(Mockito.anyInt())).thenReturn(room1, room2);
        Mockito.when(database.rawQuery(Mockito.anyString(),  Mockito.any(String[].class))).thenReturn(res);


        ConnectedPOI result = ConnectedPOIFactory.createConnectedPOI(res, building);

        assertNotNull(result);
        assertTrue(result.getFloorNumber() == floorNumber);
        assertTrue(result.getFloorPOI(room1.getFloorNumber()).equals(room1));
        assertTrue(result.getFloorPOI(room2.getFloorNumber()).equals(room2));
        assertTrue(result instanceof Escalator);
    }

    @Test
    public void createConnectedPOI_Elevator() throws Exception {
        Cursor res = Mockito.mock(Cursor.class);

        LatLng latLng = new LatLng(0, 0);
        int id = 1;
        String name = "Testo";
        int floorNumber = 1;
        String discriminatorString = "ELEVATOR";

        //ConnectedPOI
        Mockito.when(res.getInt(ConnectedPOIFactory.ID_COLUMN_INDEX)).thenReturn(id);
        Mockito.when(res.getString(ConnectedPOIFactory.NAME_COLUMN_INDEX)).thenReturn(building.getShortName());
        Mockito.when(res.getInt(ConnectedPOIFactory.FLOOR_NUMBER_COLUMN_INDEX)).thenReturn(floorNumber);
        Mockito.when(res.getString(ConnectedPOIFactory.DISCRIMINATOR_COLUMN_INDEX)).thenReturn(discriminatorString);

        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(res.moveToNext()).thenReturn(true, true, false);
        Mockito.when(res.getInt(Mockito.anyInt())).thenReturn(1);
        Mockito.when(RoomFactory.createRoomById(Mockito.anyInt())).thenReturn(room1, room2);
        Mockito.when(database.rawQuery(Mockito.anyString(),  Mockito.any(String[].class))).thenReturn(res);


        ConnectedPOI result = ConnectedPOIFactory.createConnectedPOI(res, building);

        assertNotNull(result);
        assertTrue(result.getFloorNumber() == floorNumber);
        assertTrue(result.getFloorPOI(room1.getFloorNumber()).equals(room1));
        assertTrue(result.getFloorPOI(room2.getFloorNumber()).equals(room2));
        assertTrue(result instanceof Elevator);
    }

    @Test
    public void createConnectedPOI_Staircase() throws Exception {
        Cursor res = Mockito.mock(Cursor.class);

        LatLng latLng = new LatLng(0, 0);
        int id = 1;
        String name = "Testo";
        int floorNumber = 1;
        String discriminatorString = "STAIRCASE";

        //ConnectedPOI
        Mockito.when(res.getInt(ConnectedPOIFactory.ID_COLUMN_INDEX)).thenReturn(id);
        Mockito.when(res.getString(ConnectedPOIFactory.NAME_COLUMN_INDEX)).thenReturn(building.getShortName());
        Mockito.when(res.getInt(ConnectedPOIFactory.FLOOR_NUMBER_COLUMN_INDEX)).thenReturn(floorNumber);
        Mockito.when(res.getString(ConnectedPOIFactory.DISCRIMINATOR_COLUMN_INDEX)).thenReturn(discriminatorString);

        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(res.moveToNext()).thenReturn(true, true, false);
        Mockito.when(res.getInt(Mockito.anyInt())).thenReturn(1);
        Mockito.when(RoomFactory.createRoomById(Mockito.anyInt())).thenReturn(room1, room2);
        Mockito.when(database.rawQuery(Mockito.anyString(),  Mockito.any(String[].class))).thenReturn(res);


        ConnectedPOI result = ConnectedPOIFactory.createConnectedPOI(res, building);

        assertNotNull(result);
        assertTrue(result.getFloorNumber() == floorNumber);
        assertTrue(result.getFloorPOI(room1.getFloorNumber()).equals(room1));
        assertTrue(result.getFloorPOI(room2.getFloorNumber()).equals(room2));
        assertTrue(result instanceof Staircase);
    }

}

