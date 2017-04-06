package com.concordia.mcga.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.factories.ConnectedPOIFactory;
import com.concordia.mcga.factories.RoomFactory;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Room.RoomIcon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseConnector.class, RoomFactory.class, ConnectedPOIFactory.class})
public class BuildingPopulateTest {
    private Building building;
    private Cursor cursorRoom;
    private Cursor cursorConnectedPOI;
    private SQLiteDatabase database;
    private Room room = new Room(new LatLng(0, 0), "TEST", new IndoorMapTile(0, 0), "A", 1,
        new ArrayList<LatLng>(), RoomIcon.NONE);
    private Elevator elevator = new Elevator(1);
    private DatabaseConnector databaseConnector;

    @Before
    public void setUp() throws Exception{
        // Setup mock
        databaseConnector = Mockito.mock(DatabaseConnector.class);
        PowerMockito.mockStatic(DatabaseConnector.class);
        Mockito.when(DatabaseConnector.getInstance()).thenReturn(databaseConnector);
        DatabaseConnector.setDatabaseConnector(databaseConnector);
        Mockito.when(databaseConnector.getDb()).thenReturn(database);

        cursorRoom = Mockito.mock(Cursor.class);
        cursorConnectedPOI = Mockito.mock(Cursor.class);
        database = Mockito.mock(SQLiteDatabase.class);

        Mockito.when(database.rawQuery("select * from room", null)).thenReturn(cursorRoom, cursorRoom);
        Mockito.when(database.rawQuery("select * from connected_poi", null)).thenReturn(cursorConnectedPOI, cursorConnectedPOI);

        building = Mockito.spy(Building.class);
    }

    @Test
    public void testPopulateRooms_emptyRooms() throws Exception  {
        // Test data
        List<Room> rooms = new ArrayList<>();
        building.setRooms(rooms);
        building.setShortName("SHORT");
        // Mock
        PowerMockito.mockStatic(RoomFactory.class);
        Mockito.when(RoomFactory.createRoom(cursorRoom)).thenReturn(room);
        Mockito.when(cursorRoom.moveToNext()).thenReturn(true,true,false);
        Mockito.when(cursorRoom.getString(Mockito.anyInt())).thenReturn("SHORT");
        Mockito.when(databaseConnector.getDb()).thenReturn(database);

        // Execute
        building.populateRooms();

        // Verify
        Mockito.verify(database, Mockito.times(1)).rawQuery("select * from room", null);
        Mockito.verify(cursorRoom, Mockito.times(3)).moveToNext();
        PowerMockito.verifyStatic(Mockito.times(2));
        RoomFactory.createRoom(cursorRoom);
    }

    @Test
    public void testPopulateRooms_fullRooms() throws Exception  {
        // Test data
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        building.setRooms(rooms);

        // Execute
        building.populateRooms();

        // Verify
        Mockito.verify(cursorRoom, Mockito.times(0)).moveToNext();
        Mockito.verify(database, Mockito.times(0)).rawQuery("select * from room", null);

    }

    @Test
    public void testPopulateConnectedPOIs_empty() throws Exception  {
        // Test data
        Map<Integer, Floor> floorMaps = new HashMap<>();
        List<Room> rooms = new ArrayList<>();

        building.setRooms(rooms);
        building.setFloorMaps(floorMaps);

        building.getRooms().add(room);
        Floor floor = building.getFloorMap(1);
        building.setShortName("SHORT");

        // Mock
        PowerMockito.mockStatic(ConnectedPOIFactory.class);
        Mockito.when(ConnectedPOIFactory.createConnectedPOI(cursorConnectedPOI, building)).thenReturn(elevator);
        Mockito.when(cursorConnectedPOI.moveToNext()).thenReturn(true,true,false);
        Mockito.when(cursorConnectedPOI.getString(Mockito.anyInt())).thenReturn("SHORT");
        Mockito.when(databaseConnector.getDb()).thenReturn(database);

        // Execute
        building.populateConnectedPOIs();

        // Verify
        Mockito.verify(database, Mockito.times(1)).rawQuery("select * from connected_poi", null);
        Mockito.verify(cursorConnectedPOI, Mockito.times(3)).moveToNext();
        PowerMockito.verifyStatic(Mockito.times(2));
        ConnectedPOIFactory.createConnectedPOI(cursorConnectedPOI, building);
    }

    @Test
    public void testPopulateConnectedPOIs_full() throws Exception  {
        // Test data
        Map<Integer, Floor> floorMaps = new HashMap<>();
        List<Room> rooms = new ArrayList<>();

        building.setConnectedPoiRetrieved(true);

        // Execute
        building.populateConnectedPOIs();

        // Verify
        Mockito.verify(cursorConnectedPOI, Mockito.times(0)).moveToNext();
        Mockito.verify(database, Mockito.times(0)).rawQuery("select * from connected_poi", null);

    }

    @Test
    public void testPopulateFloors() throws Exception  {
        // Test data
        Building testBuilding = new Building(new LatLng(0,0), "Test", "T", new MarkerOptions());

        Room room1 = new Room(new LatLng(0,0), "Room1", new IndoorMapTile(0,0), "1", 1, new ArrayList<LatLng>(), RoomIcon.NONE);
        Room room2 = new Room(new LatLng(0,0), "Room2", new IndoorMapTile(0,0), "2", 1, new ArrayList<LatLng>(), RoomIcon.NONE);
        Room room3 = new Room(new LatLng(0,0), "Room3", new IndoorMapTile(0,0), "3", 2, new ArrayList<LatLng>(), RoomIcon.NONE);

        testBuilding.getRooms().add(room1);
        testBuilding.getRooms().add(room2);
        testBuilding.getRooms().add(room3);

        // Execute
        testBuilding.populateFloors();

        // Verify
        assertTrue(testBuilding.getFloorMap(1).getIndoorPOIs().contains(room1));
        assertTrue(testBuilding.getFloorMap(1).getIndoorPOIs().contains(room2));
        assertTrue(testBuilding.getFloorMap(2).getIndoorPOIs().contains(room3));
    }

    @Test
    public void testAddConnectedPoi() {
        // Test data
        Building testBuilding = new Building(new LatLng(0,0), "Test", "T", new MarkerOptions());

        Room room1 = new Room(new LatLng(0,0), "Room1", new IndoorMapTile(0,0), "1", 1, new ArrayList<LatLng>(), RoomIcon.NONE);
        Room room2 = new Room(new LatLng(0,0), "Room2", new IndoorMapTile(0,0), "2", 2, new ArrayList<LatLng>(), RoomIcon.NONE);

        Escalator escalator = new Escalator(1);
        Elevator elevator = new Elevator(1);
        Staircase staircase = new Staircase(2);

        //Populate
        testBuilding.getRooms().add(room1);
        testBuilding.getRooms().add(room2);

        testBuilding.populateFloors();

        testBuilding.addConnectedPOI(escalator);
        testBuilding.addConnectedPOI(elevator);
        testBuilding.addConnectedPOI(staircase);

        //Assert
        assertTrue(testBuilding.getFloorMap(1).getEscalators().contains(escalator));
        assertTrue(testBuilding.getFloorMap(1).getElevators().contains(elevator));
        assertTrue(testBuilding.getFloorMap(2).getStaircases().contains(staircase));
    }
}
