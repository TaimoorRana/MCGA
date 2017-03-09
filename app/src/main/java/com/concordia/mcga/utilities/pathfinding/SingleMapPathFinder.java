package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.utilities.pathfinding.PathFinderTile.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.json.JSONArray;

/**
 * Pathfinding class, which runs the A* shortest pathfinding algorithm
 */
public class SingleMapPathFinder {

    TiledMap map;
    TreeSet<PathFinderTile> openSet;
    Set<PathFinderTile> closedSet;

    public SingleMapPathFinder(TiledMap map) {
        openSet = new TreeSet<>();
        closedSet = new HashSet<>();
        // TODO deep clone map.
        this.map = map;
    }

    /**
     * Finds the shortest path between the given x,y coordinates
     *
     * @param start - starting position
     * @param dest  - ending position
     * @return - Returns a list of the tiles found in the shortest path. Sorted from first to last.
     * @throws MCGAPathFindingException - Thrown if there exists no valid path between both points
     */
    List<IndoorMapTile> shortestPath(IndoorMapTile start, IndoorMapTile dest)
            throws MCGAPathFindingException {
        map.setStartTile(start.getCoordinateX(), start.getCoordinateY());
        map.setEndTile(dest.getCoordinateX(), dest.getCoordinateY());
        openSet.add(map.getStartTile());
        PathFinderTile current;
        while (true) {
            if (openSet.isEmpty()) {
                throw new MCGAPathFindingException("No valid path to destination!");
            }
            current = openSet.first();
            if (current.equals(map.getEndTile())) {
                break;
            }
            nextIteration(current);
        }
        // Current is now the destination tile. Path is available by traversing parents.
        List<IndoorMapTile> returnList = new ArrayList<>();
        while (true) {
            returnList.add(0, current.getIndoorMapTile());
            if (current.getTileType() == Type.START){
                break;
            }
            current = current.getParent();
        }
        return returnList;
    }

    /**
     * Finds the shortest path but only returns points where a direction change occurs. Useful for plotting lines on a map using markers.
     *
     * @param start - starting position
     * @param dest  - ending position
     * @return - Returns a list of the tiles found in the shortest path. Sorted from first to last.
     * @throws MCGAPathFindingException - Thrown if there exists no valid path between both points
     */
    public List<IndoorMapTile> shortestPathJunctions(IndoorMapTile start, IndoorMapTile dest) throws MCGAPathFindingException {
        ArrayList<IndoorMapTile> pathTiles = new ArrayList<IndoorMapTile>(shortestPath(start, dest));
        ArrayList<IndoorMapTile> pathTilesJunctions = new ArrayList<IndoorMapTile>();

        IndoorMapTile firstPft = pathTiles.get(0);
        pathTilesJunctions.add(firstPft);
        int curX = firstPft.getCoordinateX();
        int curY = firstPft.getCoordinateY();

        for (int i = 1; i < pathTiles.size(); i++) {
            IndoorMapTile pft = pathTiles.get(i);
            if (!(pft.getCoordinateX() == curX && pft.getCoordinateY() != curY) && !(pft.getCoordinateY() == curY && pft.getCoordinateX() != curX)) {
                pathTilesJunctions.add(pft);
                curX = pft.getCoordinateX();
                curY = pft.getCoordinateY();
            }
        }

        return pathTilesJunctions;
    }

    public static JSONArray toJSONArray(List<IndoorMapTile> pathTilesJunctions) {
        JSONArray pftArray = new JSONArray();
        for (IndoorMapTile pft : pathTilesJunctions) {
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
        adjacentTiles[0] = map.getTile(current.getIndoorMapTile().getCoordinateX() + 1, current.getIndoorMapTile().getCoordinateY());
        adjacentTiles[1] = map.getTile(current.getIndoorMapTile().getCoordinateX() - 1, current.getIndoorMapTile().getCoordinateY());
        adjacentTiles[2] = map.getTile(current.getIndoorMapTile().getCoordinateX(), current.getIndoorMapTile().getCoordinateY() - 1);
        adjacentTiles[3] = map.getTile(current.getIndoorMapTile().getCoordinateX(), current.getIndoorMapTile().getCoordinateY() + 1);

        for (PathFinderTile tile : adjacentTiles) {
            if (tile == null) {
                continue;
            }
            if (tile.getDistFromEnd() == 0 && tile.getTileType() != Type.DESTINATION){
                tile.setDistFromEnd(tile.calculateDistanceTo(map.getEndTile()));
            }
            if (!closedSet.contains(tile)) {
                if (openSet.contains(tile)) {
                    int newDist = current.calculateDistFromStart() + 1;
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
}
