package com.concordia.mcga.activities;

import com.concordia.mcga.utilities.pathfinding.PathFinder;
import com.concordia.mcga.utilities.pathfinding.PathFinderTile;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PathFinderUnitTest {
    @Test
    public void testShortestPath() throws Exception {
        PathFinder finder = new PathFinder();

        List<PathFinderTile> tiles = finder.shortestPath(1,1,4,4);

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
}
