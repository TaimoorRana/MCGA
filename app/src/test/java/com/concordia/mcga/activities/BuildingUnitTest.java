package com.concordia.mcga.activities;

import static junit.framework.Assert.assertEquals;

import com.concordia.mcga.models.Building;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
        Building testBuildingCopy = testBuilding.addEdgeCoordinate(new LatLng(45.495656, -73.574290));
        assertEquals(testBuilding, testBuildingCopy);
    }

    @Test
    public void getPolygonOverlayOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        testBuilding.addEdgeCoordinate(new LatLng(45.495656, -73.574290));
        PolygonOptions polygonOptions = testBuilding.getPolygonOverlayOptions();
        assertEquals(0x996d171f, polygonOptions.getFillColor());
    }
}
