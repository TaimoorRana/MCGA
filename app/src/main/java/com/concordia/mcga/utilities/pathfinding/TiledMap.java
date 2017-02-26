package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;

import com.concordia.mcga.utilities.pathfinding.PathFinderTile.Type;

public class TiledMap {
    private PathFinderTile[][] allTiles;
    private PathFinderTile startTile;
    private PathFinderTile endTile;


    public TiledMap(int sizeX, int sizeY) {
        allTiles = new PathFinderTile[sizeX][sizeY];
    }

    public void setStartTile(int x, int y) {
        startTile = getTile(x, y);
        startTile.setTileType(Type.START);
    }

    public PathFinderTile getStartTile() {
        return startTile;
    }

    public void setEndTile(int x, int y) {
        endTile = getTile(x, y);
        endTile.setTileType(Type.DESTINATION);
    }

    public PathFinderTile getEndTile() {
        return endTile;
    }

    public void setTile(int x, int y, PathFinderTile tile) {
        allTiles[x][y] = tile;
    }

    public void makeWalkable(int x, int y) {
        if (allTiles[x][y] == null) {
            allTiles[x][y] = new PathFinderTile(x, y);
        }
    }

    public PathFinderTile getTile(int x, int y) {
        if (x < 0 || x >= allTiles.length ||
                y < 0 || y >= allTiles[0].length) {
            return null;
        } else {
            return allTiles[x][y];
        }
    }
}
