package com.concordia.mcga.utilities.pathfinding;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.ConnectedPOI;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MultiMapPathFinder {

    /**
     * Finds the shortest path between 2 different {@link IndoorPOI}s.
     * Works with {@link IndoorPOI}s on the same floor,
     * on different floor in the same building and in different buildings.
     *
     * @param start - Starting {@link IndoorPOI } used for navigation
     * @param dest - Destination {@link IndoorPOI } used for navigation
     * @return - An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> IndoorMap1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> IndoorMap2 : [PathfinderTile(2,2), PathfinderTile(2,3)]<br/> }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     * destination
     */
    public Map<Floor, List<IndoorMapTile>> shortestPath(IndoorPOI start, IndoorPOI dest)
            throws MCGAPathFindingException, MCGADatabaseException {
        if (start.getFloor().equals(dest.getFloor())) {
            return sameFloorNavigation(start.getFloor(), start.getTile(), dest.getTile());
        }
        if (start.getFloor().getBuilding().equals(dest.getFloor().getBuilding())) {
            return sameBuildingNavigation(start.getFloor(), start.getTile(), dest.getFloor(),
                dest.getTile());
        }
        return differentBuildingNavigation(start, dest);
    }

    /**
     * Finds the shortest path between 2 different {@link IndoorPOI}s.
     * Works with {@link IndoorPOI}s on the same floor.
     *
     * @param start - Starting {@link IndoorPOI } used for navigation
     * @param dest - Destination {@link IndoorPOI } used for navigation
     * @return - An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> IndoorMap1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> IndoorMap2 : [PathfinderTile(2,2), PathfinderTile(2,3)]<br/> }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     * destination
     */
    @NonNull
    Map<Floor, List<IndoorMapTile>> sameFloorNavigation(Floor floor, IndoorMapTile start,
        IndoorMapTile dest) throws MCGAPathFindingException, MCGADatabaseException {
        floor.populateTiledMap();
        List<IndoorMapTile> pathList = getDirectionList(floor.getMap(), start, dest);
        LinkedHashMap<Floor, List<IndoorMapTile>> returnMap = new LinkedHashMap<>();
        floor.clearTiledMap();
        returnMap.put(floor, pathList);
        return returnMap;
    }

    /**
     * TODO
     */
    @SuppressWarnings("UnusedParameters")
    Map<Floor, List<IndoorMapTile>> differentBuildingNavigation(IndoorPOI start,
        IndoorPOI dest) {
        return null;
    }

    /**
     * Finds the shortest path between 2 different {@link IndoorPOI}s.
     * Works with {@link IndoorPOI}s in the same building, but on different floors.
     *
     * @param startFloor - Starting {@link Floor} used for navigation
     * @param startTile - Starting {@link IndoorMapTile } used for navigation
     * @param destFloor - Destination {@link Floor} used for navigation
     * @param destTile - Destination {@link IndoorMapTile } used for navigation
     * @return - An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     * destination
     */
    Map<Floor, List<IndoorMapTile>> sameBuildingNavigation(Floor startFloor,
        IndoorMapTile startTile, Floor destFloor,
        IndoorMapTile destTile) throws MCGAPathFindingException, MCGADatabaseException {
        ConnectedPOI connectedPOI = getClosestConnectedPOI(startFloor, startTile, destFloor, destTile);

        Map<Floor, List<IndoorMapTile>> returnMap = new LinkedHashMap<>();
        startFloor.populateTiledMap();
        List<IndoorMapTile> startFloorList = getDirectionList(startFloor.getMap(),
            startTile, connectedPOI.getFloorPOI(startFloor.getFloorNumber()).getTile());
        startFloor.clearTiledMap();
        destFloor.populateTiledMap();
        List<IndoorMapTile> destFloorList = getDirectionList(destFloor.getMap(),
            connectedPOI.getFloorPOI(destFloor.getFloorNumber()).getTile(), destTile);
        destFloor.clearTiledMap();
        returnMap.put(startFloor, startFloorList);
        returnMap.put(destFloor, destFloorList);

        return returnMap;
    }

    /**
     * @param startFloor - Starting {@link Floor} used for navigation
     * @param startTile - Starting {@link IndoorMapTile } used for navigation
     * @param destFloor - Destination {@link Floor} used for navigation
     * @param destTile - Destination {@link IndoorMapTile } used for navigation
     * @return the closest {@link ConnectedPOI} to the start and destination
     *         that connects both start and dest POI floors.
     * @throws MCGAPathFindingException - Exception thrown when there is no {@link ConnectedPOI}
     *         with a connection to both the startFloor and destFloor.
     */
    @Nullable
    ConnectedPOI getClosestConnectedPOI(Floor startFloor, IndoorMapTile startTile, Floor destFloor,
        IndoorMapTile destTile)
        throws MCGAPathFindingException {
        ConnectedPOI closestConnectedPOI = null;
        int closestDistance = Integer.MAX_VALUE;

        // TODO: add conditional statements and exclude some connected POIs based on user preferences
        List<ConnectedPOI> connectedPOIList = new ArrayList<>();
        connectedPOIList.addAll(startFloor.getElevators());
        connectedPOIList.addAll(startFloor.getEscalators());
        connectedPOIList.addAll(startFloor.getStaircases());

        for (ConnectedPOI connectedPOI : connectedPOIList) {
            if (connectedPOI.getFloorPOI(startFloor.getFloorNumber()) != null
                  && connectedPOI.getFloorPOI(destFloor.getFloorNumber()) != null) {
                IndoorPOI floorPOI = connectedPOI.getFloorPOI(startFloor.getFloorNumber());
                int distance = floorPOI.getTile().calculateDistanceTo(startTile);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestConnectedPOI = connectedPOI;
                }

            }
        }
        if (closestConnectedPOI == null) {
            throw new MCGAPathFindingException(
                "No Connection found between start and destination floors");
        }
        return closestConnectedPOI;
    }

    /**
     * @param map - A floor's {@link TiledMap}
     * @param start - Start {@link IndoorMapTile}, which is on the same floor as dest
     * @param dest - Destination {@link IndoorMapTile}, which is on the same floor as start
     * @return A list of Tiles that are traversed by the path
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     * destination
     */
    List<IndoorMapTile> getDirectionList(TiledMap map, IndoorMapTile start, IndoorMapTile dest)
        throws MCGAPathFindingException {
        return new SingleMapPathFinder(map)
            .shortestPath(start, dest);
    }
}
