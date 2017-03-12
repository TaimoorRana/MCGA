package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.exceptions.MCGAPathFindingException;

public class TiledMap {
    private PathFinderTile[][] allTiles;
    private PathFinderTile startTile;
    private PathFinderTile endTile;

    /**
     * Package visibility to allow for mockito testing
     */
    TiledMap(){}

    public TiledMap(int sizeX, int sizeY) {
        allTiles = new PathFinderTile[sizeX][sizeY];
    }

    public void setStartTile(int x, int y) {
        startTile = getTile(x, y);
        startTile.setTileType(PathFinderTile.Type.START);
        startTile.setDistFromStart(0);
        startTile.setParent(startTile);
    }

    /**
     * Package visibility to allow for mockito testing
     */
    void setAllTiles(PathFinderTile[][] allTiles) {
        this.allTiles = allTiles;
    }

    public PathFinderTile getStartTile() {
        return startTile;
    }

    public void setEndTile(int x, int y) {
        endTile = getTile(x, y);
        endTile.setTileType(PathFinderTile.Type.DESTINATION);
    }

    public PathFinderTile getEndTile() {
        return endTile;
    }

    public void setTile(int x, int y, PathFinderTile tile) {
        allTiles[x][y] = tile;
    }

    /**
     * Sets the given X,Y coordinate as walkable
     *
     * @param indoorMapTile -
     */
    public void makeWalkable(IndoorMapTile indoorMapTile) {
        allTiles[indoorMapTile.getCoordinateX()][indoorMapTile.getCoordinateY()] = new PathFinderTile(indoorMapTile);
    }

    /**
     * @param indoorMapTile an {@link IndoorMapTile}
     * @return whether the given {@link IndoorMapTile} object is a valid walkable path or not
     */
    public boolean isWalkable(IndoorMapTile indoorMapTile) {
        return getTile(indoorMapTile.getCoordinateX(), indoorMapTile.getCoordinateY()) != null;
    }

    /**
     * @param indoorMapTile {@link IndoorMapTile} to use for the breadth first search algorithm
     * @return The closest PathFinderTile to the current given coordinates. If the current coordinates
     *         are walkable, then it returns them. Else, it looks at tiles that are directly above,
     *         below, to the left and to the right. Does not look at tiles that are diagonal to
     *         the given coordinates.
     * @throws MCGAPathFindingException Thrown when no valid coordinate can be found.
     */
    public PathFinderTile closestWalkable(IndoorMapTile indoorMapTile) throws MCGAPathFindingException {
        int coordinateX = indoorMapTile.getCoordinateX();
        int coordinateY = indoorMapTile.getCoordinateY();
        if (isWalkable(indoorMapTile)) {
            return allTiles[coordinateX][coordinateY];
        } else {
            return findBreadthFirstSearch(coordinateX, coordinateY);
        }
    }

    /**
     *
     * @param coordinateX X coordinate to use for the breadth first search algorithm
     * @param coordinateY Y coordinate to use for the breadth first search algorithm
     * @return The closest PathFinderTile to the current given coordinates. Looks at tiles
     *         that are directly above, below, to the left and to the right. Does not look at
     *         tiles that are diagonal to the given coordinates.
     * @throws MCGAPathFindingException Thrown when no valid coordinate can be found.
     */
    PathFinderTile findBreadthFirstSearch(int coordinateX, int coordinateY) throws MCGAPathFindingException {
        int delta = 1;
        int limit = Math.max(allTiles.length, allTiles[0].length);
        while (delta < limit) {
            PathFinderTile tile = getTile(coordinateX + delta, coordinateY);
            if (tile != null) {
                return tile;
            }
            tile = getTile(coordinateX, coordinateY + delta);
            if (tile != null) {
                return tile;
            }
            if (delta > 0) {
                delta = -1 * delta;
            } else {
                delta = (-1 * delta) + 1;
            }
        }
        throw new MCGAPathFindingException(
                String.format("No PathFinderTile found close to the coordinates: {x:%d, y:%d}", coordinateX, coordinateY
                ));
    }

    /**
     * @param x The Y coordinate
     * @param y The X coordinate
     * @return The {@link PathFinderTile} that is at coordinate X and Y. Returns null if
     *         there is no {@link PathFinderTile} at the given coordinate, or if the given
     *         coorinates are out of the bounds of the map.
     */
    public PathFinderTile getTile(int x, int y) {
        if (x < 0 || x >= allTiles.length ||
                y < 0 || y >= allTiles[0].length) {
            return null;
        } else {
            return allTiles[x][y];
        }
    }
}
