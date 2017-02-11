package com.concordia.mcga.utilities.pathfinding;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PathFinderUnitTest {
    @Test
    public void testShortestPath_validPath() throws Exception {
        // Test Data
        PathFinder finder = new PathFinder();

        // Execute
        List<PathFinderTile> tiles = finder.shortestPath(1,1,4,4);

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
        PathFinder finder = new PathFinder();

        finder.map.setTile(2,0, null);
        finder.map.setTile(2,1, null);
        finder.map.setTile(2,2, null);
        finder.map.setTile(2,3, null);
        finder.map.setTile(2,4, null);
        finder.map.setTile(2,5, null);
        finder.map.setTile(2,6, null);
        finder.map.setTile(2,7, null);
        finder.map.setTile(2,8, null);
        finder.map.setTile(2,9, null);

        // Execute
        List<PathFinderTile> tiles = finder.shortestPath(1,1,4,4);
    }
}
