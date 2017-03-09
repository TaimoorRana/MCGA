package com.concordia.mcga.models;

import static junit.framework.Assert.assertEquals;

import com.concordia.mcga.factories.IndoorMapFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class BuildingUnitTest {
    @Test
    public void getNameTest() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        assertEquals("Hall", testBuilding.getName());
    }

    @Test
    public void getShortNameTest() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        assertEquals("H", testBuilding.getShortName());
    }

    @Test
    public void getMarkerOptionsTest() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        assertEquals(markerOptions, testBuilding.getMarkerOptions());
    }

    @Test
    public void getCenterCoordinatesTest() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        assertEquals(new LatLng(45.495656, -73.574290), testBuilding.getMapCoordinates());
    }

    @Test
    public void addEdgeCoordinateTest() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        List<LatLng> list = new ArrayList<>();
        LatLng edge = new LatLng(45.495656, -73.574290);
        list.add(edge);
        testBuilding.addEdgeCoordinate(list);
        assertEquals(true, testBuilding.getEdgeCoordinateList().contains(edge));
    }

    @Test
    public void getPolygonOverlayOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
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
}
