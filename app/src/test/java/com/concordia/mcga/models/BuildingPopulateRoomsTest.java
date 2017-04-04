package com.concordia.mcga.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.concordia.mcga.factories.RoomFactory;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Room.RoomIcon;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseConnector.class, RoomFactory.class})
public class BuildingPopulateRoomsTest {
    private Building building;
    private Cursor cursor;
    private SQLiteDatabase database;
    private Room room = new Room(new LatLng(0, 0), "TEST", new IndoorMapTile(0, 0), "A", 1,
        new ArrayList<LatLng>(), RoomIcon.NONE);
    private DatabaseConnector databaseConnector;
    @Before
    public void setUp() throws Exception{
        // Setup mock
        databaseConnector = Mockito.mock(DatabaseConnector.class);
        PowerMockito.mockStatic(DatabaseConnector.class);
        Mockito.when(DatabaseConnector.getInstance()).thenReturn(databaseConnector);
        DatabaseConnector.setDatabaseConnector(databaseConnector);
        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        cursor = Mockito.mock(Cursor.class);
        database = Mockito.mock(SQLiteDatabase.class);
        Mockito.when(database.rawQuery("select * from room", null)).thenReturn(cursor, cursor);
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
        Mockito.when(RoomFactory.createRoom(cursor)).thenReturn(room);
        Mockito.when(cursor.moveToNext()).thenReturn(true,true,false);
        Mockito.when(cursor.getString(Mockito.anyInt())).thenReturn("SHORT");
        Mockito.when(databaseConnector.getDb()).thenReturn(database);

        // Execute
        building.populateRooms();

        // Verify
        Mockito.verify(database, Mockito.times(1)).rawQuery("select * from room", null);
        Mockito.verify(cursor, Mockito.times(3)).moveToNext();
        PowerMockito.verifyStatic(Mockito.times(2));
        RoomFactory.createRoom(cursor);
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
        Mockito.verify(cursor, Mockito.times(0)).moveToNext();
        Mockito.verify(database, Mockito.times(0)).rawQuery("select * from room", null);

    }
}
