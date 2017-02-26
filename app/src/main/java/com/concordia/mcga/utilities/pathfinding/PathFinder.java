package com.concordia.mcga.utilities.pathfinding;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pathfinding class, which runs the A* shortest pathfinding algorithm
 */
public class PathFinder {

    TiledMap map;
    Set<PathFinderTile> openSet;
    Set<PathFinderTile> closedSet;

    public PathFinder(TiledMap map) {
        openSet = new HashSet<>();
        closedSet = new HashSet<>();
        this.map = map;
    }

    /**
     * Finds the shortest path between the given x,y coordinates
     *
     * @param startX - starting position's x coordinate
     * @param startY - starting position's y coordinate
     * @param destX  - ending position's x coordinate
     * @param destY  - ending position's y coordinate
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
            if (current == null) {
                throw new Exception("No valid path to destination!");
            } else if (current.equals(map.getEndTile())) {
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
     * Finds the shortest path but only returns points where a direction change occurs. Useful for plotting lines on a map using markers.
     *
     * @param startX - starting position's x coordinate
     * @param startY - starting position's y coordinate
     * @param destX  - ending position's x coordinate
     * @param destY  - ending position's y coordinate
     * @return - Returns a list of the tiles found in the shortest path. Sorted from first to last.
     * @throws Exception - Thrown if there exists no valid path between both points
     */
    public List<PathFinderTile> shortestPathJunctions(int startX, int startY, int destX, int destY) throws Exception {
        ArrayList<PathFinderTile> pathTiles = new ArrayList<PathFinderTile>(shortestPath(startX, startY, destX, destY));
        ArrayList<PathFinderTile> pathTilesJunctions = new ArrayList<PathFinderTile>();

        PathFinderTile firstPft = pathTiles.get(0);
        pathTilesJunctions.add(firstPft);
        int curX = firstPft.getCoordinateX();
        int curY = firstPft.getCoordinateY();

        for (int i = 1; i < pathTiles.size(); i++) {
            PathFinderTile pft = pathTiles.get(i);
            if (!(pft.getCoordinateX() == curX && pft.getCoordinateY() != curY) && !(pft.getCoordinateY() == curY && pft.getCoordinateX() != curX)) {
                pathTilesJunctions.add(pft);
                curX = pft.getCoordinateX();
                curY = pft.getCoordinateY();
            }
        }

        return pathTilesJunctions;
    }

    public static JSONArray toJSONArray(List<PathFinderTile> pathTilesJunctions) {
        JSONArray pftArray = new JSONArray();
        for (PathFinderTile pft : pathTilesJunctions) {
            pftArray.put(pft.toJSON());
        }
        return pftArray;
    }

    /**
     * Runs an iteration of the A* algorithm.
     *
     * @param current - Currently examined tile.
     */
    private void nextIteration(PathFinderTile current) {
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
                    }
                } else {
                    tile.setParent(current);
                    tile.setDistFromStart(tile.calculateDistFromStart());
                    openSet.add(tile);
                }
            }
        }
    }

    private PathFinderTile lowestOpen() {
        PathFinderTile lowest = PathFinderTile.MAX_COST;
        for (PathFinderTile tile : openSet) {
            if (tile.getCost() < lowest.getCost()) {
                lowest = tile;
            }
        }
        if (lowest.equals(PathFinderTile.MAX_COST)) {
            return null;
        }
        return lowest;
    }
}
