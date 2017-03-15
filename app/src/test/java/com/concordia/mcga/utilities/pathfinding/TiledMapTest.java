package com.concordia.mcga.utilities.pathfinding;


import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.utilities.pathfinding.PathFinderTile;
import com.concordia.mcga.utilities.pathfinding.TiledMap;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TiledMapTest {
    @Test
    public void testisWalkable_true(){
        // Test Data
        TiledMap map = new TiledMap(1,1);
        IndoorMapTile indoorMapTile = new IndoorMapTile(0, 0);
        PathFinderTile tile = new PathFinderTile(indoorMapTile);
        map.setTile(0,0, tile);

        // Execute
        boolean result = map.isWalkable(indoorMapTile);

        // Verify
        Assert.assertTrue(result);
    }
    @Test
    public void testisWalkable_false(){
        // Test Data
        TiledMap map = new TiledMap(1,1);
        IndoorMapTile indoorMapTile = new IndoorMapTile(0, 0);

        // Execute
        boolean result = map.isWalkable(indoorMapTile);

        // Verify
        Assert.assertFalse(result);
    }

    @Test
    public void testClosestWalkable_currentIsWalkable() throws MCGAPathFindingException {
        // Test Data
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[1][1]);
        IndoorMapTile indoorMapTile = new IndoorMapTile(0,0);
        PathFinderTile tile = new PathFinderTile(indoorMapTile);
        map.setTile(0,0, tile);

        // Mock
        Mockito.doReturn(true).when(map).isWalkable(indoorMapTile);

        // Execute
        PathFinderTile result = map.closestWalkable(indoorMapTile);

        // Verify
        Assert.assertEquals(indoorMapTile, result.getIndoorMapTile());
        Assert.assertEquals(tile, result);
    }

    @Test
    public void testClosestWalkable_currentIsNotWalkable() throws MCGAPathFindingException {
        // Test Data
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[3][1]);
        IndoorMapTile indoorMapTile = new IndoorMapTile(0,0);
        PathFinderTile tile = new PathFinderTile(indoorMapTile);
        map.setTile(0,0, tile);

        // Mock
        Mockito.doReturn(false).when(map).isWalkable(indoorMapTile);
        Mockito.doReturn(tile).when(map).findBreadthFirstSearch(0,0);

        // Execute
        PathFinderTile result = map.closestWalkable(indoorMapTile);

        // Verify
        Assert.assertEquals(indoorMapTile, result.getIndoorMapTile());
        Assert.assertEquals(tile, result);
    }

    @Test
    public void testFindBreadthFirstSearch_existsToRight() throws MCGAPathFindingException{
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[5][5]);
        PathFinderTile tile = new PathFinderTile(new IndoorMapTile(2,0));
        map.setTile(2,0, tile);

        // Execute
        PathFinderTile result = map.findBreadthFirstSearch(0,0);

        // Verify
        Assert.assertEquals(tile, result);
    }

    @Test
    public void testFindBreadthFirstSearch_existsToLeft() throws MCGAPathFindingException{
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[5][5]);
        PathFinderTile tile = new PathFinderTile(new IndoorMapTile(0,0));
        map.setTile(0,0, tile);

        // Execute
        PathFinderTile result = map.findBreadthFirstSearch(2,0);

        // Verify
        Assert.assertEquals(tile, result);
    }

    @Test
    public void testFindBreadthFirstSearch_existsOnTop() throws MCGAPathFindingException{
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[5][5]);
        PathFinderTile tile = new PathFinderTile(new IndoorMapTile(0,4));
        map.setTile(0,4, tile);

        // Execute
        PathFinderTile result = map.findBreadthFirstSearch(0,0);

        // Verify
        Assert.assertEquals(tile, result);
    }

    @Test
    public void testFindBreadthFirstSearch_existsOnBottom() throws MCGAPathFindingException{
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[5][5]);
        PathFinderTile tile = new PathFinderTile(new IndoorMapTile(0,0));
        map.setTile(0,0, tile);

        // Execute
        PathFinderTile result = map.findBreadthFirstSearch(0,4);

        // Verify
        Assert.assertEquals(tile, result);
    }

    @Test(expected = MCGAPathFindingException.class)
    public void testFindClosestWalkable_existsInDiagonal() throws MCGAPathFindingException{
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[3][2]);
        PathFinderTile tile = new PathFinderTile(new IndoorMapTile(2,1));
        map.setTile(2,1, tile);

        // Execute
        PathFinderTile result = map.findBreadthFirstSearch(0,0);
    }

    @Test(expected = MCGAPathFindingException.class)
    public void testFindClosestWalkable_noWalkable() throws MCGAPathFindingException{
        TiledMap map = Mockito.spy(TiledMap.class);
        map.setAllTiles(new PathFinderTile[3][2]);

        // Execute
        PathFinderTile result = map.findBreadthFirstSearch(0,0);
    }
}
