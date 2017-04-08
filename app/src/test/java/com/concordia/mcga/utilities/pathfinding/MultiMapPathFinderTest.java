package com.concordia.mcga.utilities.pathfinding;


import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.ConnectedPOI;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultiMapPathFinderTest {

    @Spy
    private MultiMapPathFinder spyPathFinder;

    @Test
    public void testShortestPath_sameFloorPOIs() throws MCGAPathFindingException, MCGADatabaseException {
        // Test Data
        IndoorMapTile startTile = new IndoorMapTile(1, 2);
        IndoorMapTile destTile = new IndoorMapTile(1, 2);
        Floor floor = new Floor();
        IndoorPOI start = new IndoorPOI(new LatLng(0, 0), "START", startTile);
        start.setFloor(floor);
        IndoorPOI dest = new IndoorPOI(new LatLng(0, 0), "DEST", destTile);
        dest.setFloor(floor);
        floor.setBuilding(new Building(new LatLng(0, 0), "BLG", "blg", new MarkerOptions()));
        Map<Floor, List<IndoorMapTile>> expectedMap = new LinkedHashMap<>();

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder)
            .sameFloorNavigation(floor, startTile, destTile);

        // Execute
        Map<Floor, List<IndoorMapTile>> result = spyPathFinder
            .shortestPath(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).sameFloorNavigation(floor, startTile, destTile);
    }

    @Test
    public void testShortestPath_sameBuildingPOIs() throws MCGAPathFindingException, MCGADatabaseException {
        // Test Data
        IndoorMapTile startTile = new IndoorMapTile(1, 1);
        IndoorMapTile destTile = new IndoorMapTile(3, 3);
        IndoorPOI start = new IndoorPOI(new LatLng(0, 0), "START", startTile);
        IndoorPOI dest = new IndoorPOI(new LatLng(0, 0), "DEST", destTile);
        Building building = new Building(new LatLng(0, 0), "TEST", "TEST", new MarkerOptions());
        Floor map1 = new Floor(building, 4);
        Floor map2 = new Floor(building, 5);
        start.setFloor(map1);
        dest.setFloor(map2);

        Map<Floor, List<IndoorMapTile>> expectedMap = new LinkedHashMap<>();

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder)
            .sameBuildingNavigation(map1, startTile, map2, destTile);

        // Execute
        Map<Floor, List<IndoorMapTile>> result = spyPathFinder
            .shortestPath(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).sameBuildingNavigation(map1, startTile, map2, destTile);
    }

    @Test
    public void testShortestPath_differentBuildingPOIs() throws MCGAPathFindingException, MCGADatabaseException {
        // Test Data
        IndoorMapTile startTile = new IndoorMapTile(1, 1);
        IndoorMapTile destTile = new IndoorMapTile(3, 3);
        IndoorPOI start = new IndoorPOI(new LatLng(0, 0), "START", startTile);
        IndoorPOI dest = new IndoorPOI(new LatLng(0, 0), "DEST", destTile);
        Building building1 = new Building(new LatLng(0, 0), "TEST", "TEST", new MarkerOptions());
        Building building2 = new Building(new LatLng(1, 0), "TEST", "TEST", new MarkerOptions());
        Floor map1 = new Floor(building1, 4);
        Floor map2 = new Floor(building2, 5);
        start.setFloor(map1);
        dest.setFloor(map2);

        Map<Floor, List<IndoorMapTile>> expectedMap = new LinkedHashMap<>();

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder).differentBuildingNavigation(start, dest);

        // Execute
        Map<Floor, List<IndoorMapTile>> result = spyPathFinder
            .shortestPath(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).differentBuildingNavigation(start, dest);
    }
}
