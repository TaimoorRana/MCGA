package com.concordia.mcga.utilities.pathfinding;


import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.ConnectedPOI;
import com.concordia.mcga.models.IndoorMap;
import com.concordia.mcga.models.IndoorPOI;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class MultiMapPathFinderTest {
    @Spy
    private MultiMapPathFinder spyPathFinder;

    @Test
    public void testShortestPath_sameFloorPOIs() throws MCGAPathFindingException {
        // Test Data
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "START", 4, 1, 1);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "DEST", 4, 3, 3);
        IndoorMap map = new IndoorMap();
        start.setIndoorMap(map);
        dest.setIndoorMap(map);

        Map<IndoorMap, List<PathFinderTile>> expectedMap = new LinkedHashMap<>();

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder).sameFloorNavigation(start, dest);

        // Execute
        Map<IndoorMap, List<PathFinderTile>> result = spyPathFinder
            .shortestPath(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).sameFloorNavigation(start, dest);
    }

    @Test
    public void testShortestPath_sameBuildingPOIs() throws MCGAPathFindingException {
        // Test Data
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "START", 5, 1, 1);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "DEST", 4, 3, 3);
        Building building = new Building(new LatLng(0,0), "TEST", "TEST", new MarkerOptions());
        IndoorMap map1 = new IndoorMap(building, 4);
        IndoorMap map2 = new IndoorMap(building, 5);
        start.setIndoorMap(map1);
        dest.setIndoorMap(map2);

        Map<IndoorMap, List<PathFinderTile>> expectedMap = new LinkedHashMap<>();

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder).sameBuildingNavigation(start, dest);

        // Execute
        Map<IndoorMap, List<PathFinderTile>> result = spyPathFinder
            .shortestPath(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).sameBuildingNavigation(start, dest);
    }

    @Test
    public void testShortestPath_differentBuildingPOIs() throws MCGAPathFindingException {
        // Test Data
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "START", 5, 1, 1);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "DEST", 4, 3, 3);
        Building building1 = new Building(new LatLng(0,0), "TEST", "TEST", new MarkerOptions());
        Building building2 = new Building(new LatLng(1,0), "TEST", "TEST", new MarkerOptions());
        IndoorMap map1 = new IndoorMap(building1, 4);
        IndoorMap map2 = new IndoorMap(building2, 5);
        start.setIndoorMap(map1);
        dest.setIndoorMap(map2);

        Map<IndoorMap, List<PathFinderTile>> expectedMap = new LinkedHashMap<>();

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder).differentBuildingNavigation(start, dest);

        // Execute
        Map<IndoorMap, List<PathFinderTile>> result = spyPathFinder
            .shortestPath(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).differentBuildingNavigation(start, dest);
    }

    @Test
    public void testSameBuildingNavigation() throws MCGAPathFindingException {
        // Test data
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "TEST", 1, 2, 2);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "DEST", 2, 3, 3);

        Map<IndoorMap, List<PathFinderTile>> expectedMap = new LinkedHashMap<>();
        ConnectedPOI connectedPOI = Mockito.mock(ConnectedPOI.class);

        // Mock
        Mockito.doReturn(expectedMap).when(spyPathFinder).createPath(start, dest, connectedPOI);
        Mockito.doReturn(connectedPOI).when(spyPathFinder).getClosestConnectedPOI(start,dest);

        // Execute
        Map<IndoorMap, List<PathFinderTile>> result = spyPathFinder
            .sameBuildingNavigation(start, dest);

        // Verify
        Assert.assertEquals(expectedMap, result);
        Mockito.verify(spyPathFinder).createPath(start, dest, connectedPOI);
        Mockito.verify(spyPathFinder).getClosestConnectedPOI(start,dest);
    }

    @Test
    public void testCreatePath() throws MCGAPathFindingException {
        // Test Data
        ConnectedPOI connectedPOI = Mockito.mock(ConnectedPOI.class);
        IndoorPOI intermediatePOI = new IndoorPOI(new LatLng(0,0), "INTERMEDIATE", 2, 3, 3);
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "START", 2, 2, 2);
        IndoorMap startMap = new IndoorMap();
        startMap.setFloorNumber(3);
        start.setIndoorMap(startMap);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "DEST", 2, 1, 1);
        IndoorMap destMap = new IndoorMap();
        destMap.setFloorNumber(4);
        dest.setIndoorMap(destMap);

        List<PathFinderTile> expectedStartList = new ArrayList<>();
        expectedStartList.add(new PathFinderTile(1,1));
        List<PathFinderTile> expectedDestList = new ArrayList<>();
        expectedDestList.add(new PathFinderTile(1,2));

        // Mock
        Mockito.doReturn(expectedStartList).when(spyPathFinder).getDirectionList(start, intermediatePOI);
        Mockito.doReturn(expectedDestList).when(spyPathFinder).getDirectionList(intermediatePOI, dest);
        Mockito.when(connectedPOI.getFloorPOI(Mockito.anyInt())).thenReturn(intermediatePOI);

        // Execute
        Map<IndoorMap, List<PathFinderTile>> result = spyPathFinder.createPath(start, dest, connectedPOI);

        // Verify
        Mockito.verify(spyPathFinder).getDirectionList(start, intermediatePOI);
        Mockito.verify(spyPathFinder).getDirectionList(intermediatePOI, dest);
        Assert.assertEquals(2, result.size());
        Iterator<Map.Entry<IndoorMap, List<PathFinderTile>>> iterator = result.entrySet().iterator();
        Assert.assertEquals(expectedStartList, iterator.next().getValue());
        Assert.assertEquals(expectedDestList, iterator.next().getValue());
    }
}
