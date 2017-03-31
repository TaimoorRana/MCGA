package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;

import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.utilities.pathfinding.PathFinderTile.Type;

import java.io.File;
import java.io.IOException;
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
        IndoorMapTile walkableStart = map.closestWalkable(start).getIndoorMapTile();
        IndoorMapTile walkableDest = map.closestWalkable(dest).getIndoorMapTile();

        map.setStartTile(walkableStart.getCoordinateX(), walkableStart.getCoordinateY());
        map.setEndTile(walkableDest.getCoordinateX(), walkableDest.getCoordinateY());
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
            if (current.getTileType() == Type.START) {
                break;
            }
            current = current.getParent();
        }
        return returnList;
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
            if (tile.getDistFromEnd() == 0 && tile.getTileType() != Type.DESTINATION) {
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


    /**
     * Given a list of generated path tiles, finds only the tiles where the direction changes. Useful for getting points to draw lines on a map.
     *
     * @param pathTiles
     * @return
     * @throws MCGAPathFindingException
     */
    public static List<IndoorMapTile> shortestPathJunctions(List<IndoorMapTile> pathTiles) throws MCGAPathFindingException {
        List<IndoorMapTile> pathTilesJunctions = new ArrayList<>();

        IndoorMapTile firstPft = pathTiles.get(0);
        pathTilesJunctions.add(firstPft);
        int lastX = firstPft.getCoordinateX();
        int lastY = firstPft.getCoordinateY();

        for (int i = 1; i < pathTiles.size(); i++) {
            IndoorMapTile pft = pathTiles.get(i);
            if (!(pft.getCoordinateX() == lastX && pft.getCoordinateY() != lastY) && !(pft.getCoordinateY() == lastY && pft.getCoordinateX() != lastX)) {
                pathTilesJunctions.add(pft);
                lastX = pft.getCoordinateX();
                lastY = pft.getCoordinateY();
            }

            //If its the last one, always add it
            if (i == pathTiles.size() - 1) {
                pathTilesJunctions.add(pft);
            }
        }

        return pathTilesJunctions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (PathFinderTile[] tiles : map.getAllTiles()) {
            for (PathFinderTile tile : tiles) {
                if (tile == null) {
                    builder.append(" ");
                } else if (Type.START.equals(tile.getTileType())) {
                    builder.append("S");
                } else if (Type.DESTINATION.equals(tile.getTileType())) {
                    builder.append("D");
                } else if (openSet.contains(tile)) {
                    builder.append("o");
                } else if (closedSet.contains(tile)) {
                    builder.append("c");
                } else {
                    builder.append(".");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
