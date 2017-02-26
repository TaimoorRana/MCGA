package com.concordia.mcga.utilities.pathfinding;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class PathFinderUnitTest {
    @Test
    public void testShortestPath_validPath() throws Exception {
        // Test Data
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(i, j);
            }
        }
        PathFinder finder = new PathFinder(map);

        // Execute
        List<PathFinderTile> tiles = finder.shortestPath(1, 1, 4, 4);

        // Verify
        Assert.assertEquals(1, tiles.get(0).getCoordinateX());
        Assert.assertEquals(1, tiles.get(0).getCoordinateY());
        Assert.assertEquals(2, tiles.get(1).getCoordinateX());
        Assert.assertEquals(1, tiles.get(1).getCoordinateY());
        Assert.assertEquals(3, tiles.get(2).getCoordinateX());
        Assert.assertEquals(1, tiles.get(2).getCoordinateY());
        Assert.assertEquals(4, tiles.get(3).getCoordinateX());
        Assert.assertEquals(1, tiles.get(3).getCoordinateY());
        Assert.assertEquals(4, tiles.get(4).getCoordinateX());
        Assert.assertEquals(2, tiles.get(4).getCoordinateY());
        Assert.assertEquals(4, tiles.get(5).getCoordinateX());
        Assert.assertEquals(3, tiles.get(5).getCoordinateY());
        Assert.assertEquals(4, tiles.get(6).getCoordinateX());
        Assert.assertEquals(4, tiles.get(6).getCoordinateY());
    }

    @Test(expected = Exception.class)
    public void testShortestPath_invalidPath() throws Exception {
        // Test Data
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            if (i == 2) {
                continue;
            }
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(i, j);
            }
        }
        PathFinder finder = new PathFinder(map);

        // Execute
        List<PathFinderTile> tiles = finder.shortestPath(1, 1, 4, 4);
    }
}
