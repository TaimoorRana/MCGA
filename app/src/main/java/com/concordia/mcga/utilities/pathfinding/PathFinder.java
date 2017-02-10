package com.concordia.mcga.utilities.pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pathfinding class, which runs the A* shortest pathfinding algorithm
 */
public class PathFinder {

    private TiledMap map;
    private Set<PathFinderTile> openSet;
    private Set<PathFinderTile> closedSet;

    public PathFinder() {
        openSet = new HashSet<>();
        closedSet = new HashSet<>();
        map = new TiledMap(10); // Example map, 10x10 grid of all valid paths.
    }

    /**
     *  Finds the shortest path between the given x,y coordinates
     * @param startX - starting position's x coordinate
     * @param startY - starting position's y coordinate
     * @param destX - ending position's x coordinate
     * @param destY - ending position's y coordinate
     * @return - Returns a list of the tiles found in the shortest path. Sorted from first to last.
     * @throws Exception - Thrown if there exists no valid path between both points
     */
    public List<PathFinderTile> shortestPath(int startX, int startY, int destX, int destY)
        throws Exception {
        map.setStartTile(startX, startY);
        map.setEndTile(destX, destY);
        openSet.add(map.getStartTile());
        PathFinderTile current;
        while (true) {
            current = lowestOpen();
            if (current.equals(map.getEndTile())) {
                break;
            }
            nextIteration(current);
        }
        // Current is now the destination tile. Path is available by traversing parents.
        List<PathFinderTile> returnList = new ArrayList<>();
        while (current != null) {
            returnList.add(0, current);
            current = current.getParent();
        }
        return returnList;
    }

    /**
     * Runs an iteration of the A* algorithm.
     * @param current - Currently examined tile.
     * @throws Exception
     */
    private void nextIteration(PathFinderTile current) throws Exception {
        openSet.remove(current);
        closedSet.add(current);

        PathFinderTile[] adjacentTiles = new PathFinderTile[4];
        adjacentTiles[0] = map.getTile(current.getCoordinateX() + 1, current.getCoordinateY());
        adjacentTiles[1] = map.getTile(current.getCoordinateX() - 1, current.getCoordinateY());
        adjacentTiles[2] = map.getTile(current.getCoordinateX(), current.getCoordinateY() - 1);
        adjacentTiles[3] = map.getTile(current.getCoordinateX(), current.getCoordinateY() + 1);

        for (PathFinderTile tile : adjacentTiles) {
            if (tile == null) {
                continue;
            }
            if (!closedSet.contains(tile)) {
                if (openSet.contains(tile)) {
                    int newDist = tile.calculateDistFromStart();
                    if (newDist < tile.getDistFromStart()) {
                        tile.setDistFromStart(newDist);
                        tile.setParent(current);
                        openSet.remove(tile);
                        openSet.add(tile);
                    }
                } else {
                    tile.setParent(current);
                    tile.setDistFromStart(tile.calculateDistFromStart());
                    openSet.add(tile);
                }
            }
        }
    }

    private PathFinderTile lowestOpen() throws Exception {
        PathFinderTile lowest = PathFinderTile.MAX_COST;
        for (PathFinderTile tile : openSet) {
            if (tile.getCost() < lowest.getCost()) {
                lowest = tile;
            }
        }
        if (lowest.equals(PathFinderTile.MAX_COST)) {
            throw new Exception("No Valid Path Found!");
        }
        return lowest;
    }
}
