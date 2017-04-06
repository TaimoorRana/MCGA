package com.concordia.mcga.factories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.utilities.pathfinding.TiledMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseConnector.class})
public class FloorFactoryTest {

    private IndoorMapFactory instance;
    private String name;
    private String shortName;
    private Building testBuilding;

    private SQLiteDatabase database;
    private DatabaseConnector databaseConnector;

    @Before
    public void setUp() throws MCGADatabaseException {
        instance = new IndoorMapFactory();
        name = "TEST_NAME";
        shortName = "SHORT_NAME";
        testBuilding = new Building(new LatLng(0, 0), name, shortName, new MarkerOptions());

        // Setup mock
        databaseConnector = Mockito.mock(DatabaseConnector.class);
        PowerMockito.mockStatic(DatabaseConnector.class);
        Mockito.when(DatabaseConnector.getInstance()).thenReturn(databaseConnector);
        DatabaseConnector.setDatabaseConnector(databaseConnector);
        Mockito.when(databaseConnector.getDb()).thenReturn(database);

        database = Mockito.mock(SQLiteDatabase.class);
    }

    @Test
    public void testGetInstance_noInstance() throws Exception {
        // Test Data
        IndoorMapFactory.setInstance(null);

        // Execute & Verify
        Assert.assertNotNull(IndoorMapFactory.getInstance());
    }

    @Test
    public void testGetInstance_existingInstance() throws Exception {
        // Test Data
        IndoorMapFactory expectedInstance = new IndoorMapFactory();
        IndoorMapFactory.setInstance(expectedInstance);

        // Execute
        IndoorMapFactory result = IndoorMapFactory.getInstance();

        // Verify
        Assert.assertEquals(expectedInstance, result);
    }

    @Test
    public void testCreateWalkablePaths_pathsDoNotExist() throws MCGADatabaseException {
        // Mocking
        TiledMap mockMap = Mockito.mock(TiledMap.class);
        Cursor expectedCursor = Mockito.mock(Cursor.class);

        int expectedX = 1;
        int expectedY = 2;
        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(expectedCursor.moveToNext()).thenReturn(false);
        Mockito.when(expectedCursor.getInt(Mockito.anyInt())).thenReturn(expectedX, expectedY);
        Mockito.when(database.rawQuery(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(expectedCursor);

        // Execute
        instance.insertWalkablePaths(testBuilding, 4, mockMap);

        // Verify
        Mockito.verify(mockMap, Mockito.times(0)).makeWalkable(new IndoorMapTile(expectedX, expectedY));
        Mockito.verify(expectedCursor).close();
    }

    @Test
    public void testCreateWalkablePaths_pathsExist() throws MCGADatabaseException {
        // Mocking
        TiledMap mockMap = Mockito.mock(TiledMap.class);
        Cursor expectedCursor = Mockito.mock(Cursor.class);

        int expectedX = 1;
        int expectedY = 2;
        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(expectedCursor.moveToNext()).thenReturn(true, false);
        Mockito.when(expectedCursor.getInt(Mockito.anyInt())).thenReturn(expectedX, expectedY);
        Mockito.when(database.rawQuery(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(expectedCursor);

        // Execute
        instance.insertWalkablePaths(testBuilding, 4, mockMap);

        // Verify
        Mockito.verify(mockMap).makeWalkable(new IndoorMapTile(expectedX, expectedY));
        Mockito.verify(expectedCursor).close();
    }

    @Test
    public void testGetTiledMap_mapExists() throws MCGADatabaseException {
        // Mocking
        TiledMap mockMap = Mockito.mock(TiledMap.class);
        Cursor expectedCursor = Mockito.mock(Cursor.class);


        int expectedX = 1;
        int expectedY = 2;
        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(expectedCursor.moveToNext()).thenReturn(true);
        Mockito.when(expectedCursor.getInt(Mockito.anyInt())).thenReturn(expectedX, expectedY);
        Mockito.when(database.rawQuery(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(expectedCursor);

        // Execute
        TiledMap expectedMap = instance.createTiledMap(testBuilding, 4);

        // Verify
        Mockito.verify(expectedCursor, Mockito.times(2)).getInt(Mockito.anyInt());
        Mockito.verify(expectedCursor).close();
    }

    @Test
    public void testGetTiledMap_mapDoesNotExist() {
        // Mocking
        SQLiteDatabase mockDB = Mockito.mock(SQLiteDatabase.class);
        Cursor expectedCursor = Mockito.mock(Cursor.class);

        Mockito.when(databaseConnector.getDb()).thenReturn(database);
        Mockito.when(expectedCursor.moveToNext()).thenReturn(false);
        Mockito.when(database.rawQuery(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(expectedCursor);

        //Execute
        try {
            instance.createTiledMap(testBuilding, 4);
            Assert.fail("TEST SHOULD NOT REACH HERE");
        } catch (MCGADatabaseException e) {
        }

        // Verify
        Mockito.verify(expectedCursor).close();
    }
}
