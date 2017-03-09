package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.utilities.pathfinding.IndoorMapTile.Type;

public class TiledMap {
    private IndoorMapTile[][] allTiles;
    private IndoorMapTile startTile;
    private IndoorMapTile endTile;


    public TiledMap(int sizeX, int sizeY) {
        allTiles = new IndoorMapTile[sizeX][sizeY];
    }

    public void setStartTile(int x, int y) {
        startTile = getTile(x, y);
        startTile.setTileType(Type.START);
        startTile.setParent(startTile);
    }

    public IndoorMapTile getStartTile() {
        return startTile;
    }

    public void setEndTile(int x, int y) {
        endTile = getTile(x, y);
        endTile.setTileType(Type.DESTINATION);
    }

    public IndoorMapTile getEndTile() {
        return endTile;
    }

    public void setTile(int x, int y, IndoorMapTile tile) {
        allTiles[x][y] = tile;
    }

    /**
     * Sets the given X,Y coordinate as walkable
     * @param x - X coordinate
     * @param y - Y coordinate
     */
    public void makeWalkable(int x, int y) {
        if (allTiles[x][y] == null) {
            allTiles[x][y] = new IndoorMapTile(x, y);
        }
    }

    public IndoorMapTile getTile(int x, int y) {
        if (x < 0 || x >= allTiles.length ||
                y < 0 || y >= allTiles[0].length) {
            return null;
        } else {
            return allTiles[x][y];
        }
    }
}
