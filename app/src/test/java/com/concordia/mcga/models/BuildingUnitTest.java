package com.concordia.mcga.models;

import android.database.Cursor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.factories.IndoorMapFactory;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(JUnit4.class)
public class BuildingUnitTest {
    MarkerOptions markerOptions = new MarkerOptions();
    Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);

    @Test
    public void getNameTest() {
        assertEquals("Hall", testBuilding.getName());
    }

    @Test
    public void getShortNameTest() {
        assertEquals("H", testBuilding.getShortName());
    }

    @Test
    public void getMarkerOptionsTest() {
        assertEquals(markerOptions, testBuilding.getMarkerOptions());
    }

    @Test
    public void getCenterCoordinatesTest() {
        assertEquals(new LatLng(45.495656, -73.574290), testBuilding.getMapCoordinates());
    }

    @Test
    public void addEdgeCoordinateTest() {
        List<LatLng> list = new ArrayList<>();
        LatLng edge = new LatLng(45.495656, -73.574290);
        list.add(edge);
        testBuilding.addEdgeCoordinate(list);
        assertEquals(true, testBuilding.getEdgeCoordinateList().contains(edge));
    }

    @Test
    public void getPolygonOverlayOptions() {
        List<LatLng> list = new ArrayList<>();
        LatLng edge = new LatLng(45.495656, -73.574290);
        list.add(edge);
        testBuilding.addEdgeCoordinate(list);
        PolygonOptions polygonOptions = testBuilding.getPolygonOverlayOptions();
        assertEquals(0x996d171f, polygonOptions.getFillColor());
    }

    @Test
    public void testGetFloorMap_cached(){
        // Test data
        Map<Integer,Floor> maps = new HashMap<>();
        Floor expectedMap = new Floor();
        maps.put(1,expectedMap);
        Building testBuilding = new Building(new LatLng(0,0), "TEST", "TEST", new MarkerOptions());
        testBuilding.setFloorMaps(maps);

        // Execute
        Floor result = testBuilding.getFloorMap(1);

        // Verify
        assertEquals(expectedMap, result);
    }

    @Test
    public void testGetFloorMap_notCached(){
        // Test data
        Building testBuilding = new Building(new LatLng(0,0), "TEST", "TEST", new MarkerOptions());
        Floor expectedMap = new Floor();
        Map<Integer,Floor> maps = new HashMap<>();
        testBuilding.setFloorMaps(maps);
        // Mock
        IndoorMapFactory mockFactory = Mockito.mock(IndoorMapFactory.class);
        Mockito.when(mockFactory.createIndoorMap(testBuilding, 1)).thenReturn(expectedMap);
        IndoorMapFactory.setInstance(mockFactory);

        // Pretest asserts
        assertEquals(0, maps.size());

        // Execute
        Floor result = testBuilding.getFloorMap(1);

        // Verify
        assertEquals(expectedMap, result);
        assertEquals(1, maps.size());
        Mockito.verify(mockFactory).createIndoorMap(testBuilding, 1);
    }

    @Test
    public void setShortNameTest(){
       testBuilding.setShortName("test");
       assertEquals("test",testBuilding.getShortName());
  }
}