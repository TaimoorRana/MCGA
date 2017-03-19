package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.IndoorMapTile;
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
        IndoorMapTile start = new IndoorMapTile(1, 1);
        IndoorMapTile dest = new IndoorMapTile(4,4);
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(new IndoorMapTile(i, j));
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
        IndoorMapTile start = new IndoorMapTile(1, 1);
        IndoorMapTile dest = new IndoorMapTile(4, 4);
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            if (i == 2) {
                continue;
            }
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(new IndoorMapTile(i, j));
            }
        }
        SingleMapPathFinder finder = new SingleMapPathFinder(map);

        // Execute
        List<IndoorMapTile> tiles = finder.shortestPath(start, dest);
    }

    @Test
    public void testShortestPath_validUPath() throws MCGAPathFindingException {
        // Test Data
        IndoorMapTile start = new IndoorMapTile(1, 1);
        IndoorMapTile dest = new IndoorMapTile(4, 4);
        TiledMap map = new TiledMap(10, 10);
        for (int i = 0; i < 10; i++) {
            if (i == 2) {
                continue;
            }
            for (int j = 0; j < 10; j++) {
                map.makeWalkable(new IndoorMapTile(i, j));
            }
        }
        map.makeWalkable(new IndoorMapTile(2,8));
        SingleMapPathFinder finder = new SingleMapPathFinder(map);

        // Execute
        List<IndoorMapTile> tiles = finder.shortestPath(start, dest);

        // Validate
        Assert.assertEquals(1, tiles.get(0).getCoordinateY());
        Assert.assertEquals(2, tiles.get(1).getCoordinateY());
        Assert.assertEquals(3, tiles.get(2).getCoordinateY());
        Assert.assertEquals(4, tiles.get(3).getCoordinateY());
        Assert.assertEquals(5, tiles.get(4).getCoordinateY());
        Assert.assertEquals(6, tiles.get(5).getCoordinateY());
        Assert.assertEquals(7, tiles.get(6).getCoordinateY());
        Assert.assertEquals(8, tiles.get(7).getCoordinateY());
        Assert.assertEquals(8, tiles.get(8).getCoordinateY());
        Assert.assertEquals(8, tiles.get(9).getCoordinateY());
        Assert.assertEquals(7, tiles.get(10).getCoordinateY());
        Assert.assertEquals(6, tiles.get(11).getCoordinateY());
        Assert.assertEquals(5, tiles.get(12).getCoordinateY());
        Assert.assertEquals(4, tiles.get(13).getCoordinateY());
        Assert.assertEquals(4, tiles.get(14).getCoordinateY());

        Assert.assertEquals(1, tiles.get(0).getCoordinateX());
        Assert.assertEquals(1, tiles.get(1).getCoordinateX());
        Assert.assertEquals(1, tiles.get(2).getCoordinateX());
        Assert.assertEquals(1, tiles.get(3).getCoordinateX());
        Assert.assertEquals(1, tiles.get(4).getCoordinateX());
        Assert.assertEquals(1, tiles.get(5).getCoordinateX());
        Assert.assertEquals(1, tiles.get(6).getCoordinateX());
        Assert.assertEquals(1, tiles.get(7).getCoordinateX());
        Assert.assertEquals(2, tiles.get(8).getCoordinateX());
        Assert.assertEquals(3, tiles.get(9).getCoordinateX());
        Assert.assertEquals(3, tiles.get(10).getCoordinateX());
        Assert.assertEquals(3, tiles.get(11).getCoordinateX());
        Assert.assertEquals(3, tiles.get(12).getCoordinateX());
        Assert.assertEquals(3, tiles.get(13).getCoordinateX());
        Assert.assertEquals(4, tiles.get(14).getCoordinateX());
    }
}
