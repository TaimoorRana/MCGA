package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.utilities.pathfinding.PathFinderTile.Type;

public class TiledMap {
    private PathFinderTile[][] allTiles;
    private PathFinderTile startTile;
    private PathFinderTile endTile;


    public TiledMap(int size){
        allTiles = new PathFinderTile[size][size];
        for (int i = 0; i < allTiles.length; i++){
            for (int j = 0; j < allTiles[0].length; j++){
                allTiles[i][j] = new PathFinderTile(i, j);
            }
        }
    }

    public void setStartTile(int x, int y){
        startTile = getTile(x,y);
        startTile.setTileType(Type.START);
    }

    public PathFinderTile getStartTile() {
        return startTile;
    }

    public void setEndTile(int x, int y){
        endTile = getTile(x,y);
        endTile.setTileType(Type.DESTINATION);
    }

    public PathFinderTile getEndTile() {
        return endTile;
    }
    public void setTile(int x, int y, PathFinderTile tile){
        allTiles[x][y] = tile;
    }

    public PathFinderTile getTile(int x, int y){
        if (x < 0 || x >= allTiles.length ||
            y < 0 || y >= allTiles[0].length){
            return null;
        } else {
            return allTiles[x][y];
        }
    }
}
