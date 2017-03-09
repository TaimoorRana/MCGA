package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.IndoorPOI;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SingleMapPathFinderUnitTest {
    @Test
    public void testShortestPath_validPath() throws MCGAPathFindingException {
        // Test Data
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "TEST", 1, 1, 1);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "TEST", 1, 4, 4);
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(i, j);
            }
        }
        SingleMapPathFinder finder = new SingleMapPathFinder(map);

        // Execute
        List<IndoorMapTile> tiles = finder.shortestPath(start, dest);

        // Verify
        Assert.assertEquals(1, tiles.get(0).getCoordinateY());
        Assert.assertEquals(1, tiles.get(0).getCoordinateX());
        Assert.assertEquals(2, tiles.get(1).getCoordinateY());
        Assert.assertEquals(1, tiles.get(1).getCoordinateX());
        Assert.assertEquals(3, tiles.get(2).getCoordinateY());
        Assert.assertEquals(1, tiles.get(2).getCoordinateX());
        Assert.assertEquals(4, tiles.get(3).getCoordinateY());
        Assert.assertEquals(1, tiles.get(3).getCoordinateX());
        Assert.assertEquals(4, tiles.get(4).getCoordinateY());
        Assert.assertEquals(2, tiles.get(4).getCoordinateX());
        Assert.assertEquals(4, tiles.get(5).getCoordinateY());
        Assert.assertEquals(3, tiles.get(5).getCoordinateX());
        Assert.assertEquals(4, tiles.get(6).getCoordinateY());
        Assert.assertEquals(4, tiles.get(6).getCoordinateX());
    }

    @Test(expected = MCGAPathFindingException.class)
    public void testShortestPath_invalidPath() throws MCGAPathFindingException {
        // Test Data
        IndoorPOI start = new IndoorPOI(new LatLng(0,0), "TEST", 1, 1, 1);
        IndoorPOI dest = new IndoorPOI(new LatLng(0,0), "TEST", 1, 4, 4);
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            if (i == 2) {
                continue;
            }
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(i, j);
            }
        }
        SingleMapPathFinder finder = new SingleMapPathFinder(map);

        // Execute
        List<IndoorMapTile> tiles = finder.shortestPath(start, dest);
    }
}
