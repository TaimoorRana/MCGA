package com.concordia.mcga.utilities.pathfinding;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.concordia.mcga.exceptions.MCGADifferentFloorException;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.ConnectedPOI;
import com.concordia.mcga.models.IndoorMap;
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
     * @param start - Starting {@link IndoorPOI } used for navigation
     * @param dest - Destination {@link IndoorPOI } used for navigation
     * @return - An ordered Map, containing {@link IndoorMap} elements as keys, with their
     * associated ordered routes as values. <br/> e.g. <br/>
     *      {<br/>
     *         IndoorMap1 : [PathfinderTile(1,1), PathfinderTile(1,2)]<br/>
     *         IndoorMap2 : [PathfinderTile(2,2), PathfinderTile(2,3)]<br/>
     *      }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     *                                    destination
     */
    public Map<IndoorMap, List<PathFinderTile>> shortestPath(IndoorPOI start, IndoorPOI dest)
        throws MCGAPathFindingException {
        if (start.getIndoorMap().equals(dest.getIndoorMap())) {
            return sameFloorNavigation(start, dest);
        }
        if (start.getIndoorMap().getBuilding().equals(dest.getIndoorMap().getBuilding())) {
            return sameBuildingNavigation(start, dest);
        }
        return differentBuildingNavigation(start, dest);
    }

    /**
     * Finds the shortest path between 2 different {@link IndoorPOI}s.
     * Works with {@link IndoorPOI}s on the same floor.
     * @param start - Starting {@link IndoorPOI } used for navigation
     * @param dest - Destination {@link IndoorPOI } used for navigation
     * @return - An ordered Map, containing {@link IndoorMap} elements as keys, with their
     * associated ordered routes as values. <br/> e.g. <br/>
     *      {<br/>
     *         IndoorMap1 : [PathfinderTile(1,1), PathfinderTile(1,2)]<br/>
     *         IndoorMap2 : [PathfinderTile(2,2), PathfinderTile(2,3)]<br/>
     *      }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     *                                    destination
     */
    @NonNull
    Map<IndoorMap, List<PathFinderTile>> sameFloorNavigation(IndoorPOI start,
        IndoorPOI dest) throws MCGAPathFindingException {
        List<PathFinderTile> pathList = getDirectionList(start, dest);
        LinkedHashMap<IndoorMap, List<PathFinderTile>> returnMap = new LinkedHashMap<>();
        returnMap.put(start.getIndoorMap(), pathList);
        return returnMap;
    }

    /**
     * TODO
     */
    Map<IndoorMap, List<PathFinderTile>> differentBuildingNavigation(IndoorPOI start,
        IndoorPOI dest) {
        return null;
    }

    /**
     * Finds the shortest path between 2 different {@link IndoorPOI}s.
     * Works with {@link IndoorPOI}s in the same building, but on different floors.
     * @param start - Starting {@link IndoorPOI } used for navigation
     * @param dest - Destination {@link IndoorPOI } used for navigation
     * @return - An ordered Map, containing {@link IndoorMap} elements as keys, with their
     * associated ordered routes as values. <br/> e.g. <br/>
     *      {<br/>
     *         IndoorMap1 : [PathfinderTile(1,1), PathfinderTile(1,2)]<br/>
     *         IndoorMap2 : [PathfinderTile(2,2), PathfinderTile(2,3)]<br/>
     *      }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     *                                    destination
     */
    Map<IndoorMap, List<PathFinderTile>> sameBuildingNavigation(IndoorPOI start,
        IndoorPOI dest) throws MCGAPathFindingException {
        ConnectedPOI connectedPOI = getClosestConnectedPOI(start, dest);
        return createPath(start, dest, connectedPOI);
    }

    /**
     * @param start - first {@link com.concordia.mcga.models.POI}
     * @param dest - second {@link com.concordia.mcga.models.POI}
     * @return the closest {@link ConnectedPOI} to the start {@link com.concordia.mcga.models.POI}
     * that connects both start and dest POI floors.
     * @throws MCGAPathFindingException - Exception thrown when there is no {@link ConnectedPOI}
     * with a connection to both the <b>start</b> and <b>dest</b> {@link
     * com.concordia.mcga.models.POI}'s floors.
     */
    @Nullable
    ConnectedPOI getClosestConnectedPOI(IndoorPOI start, IndoorPOI dest)
        throws MCGAPathFindingException {
        ConnectedPOI closestConnectedPOI = null;
        int closestDistance = Integer.MAX_VALUE;

        // TODO: add conditional statements and exclude some connected POIs based on user preferences
        List<ConnectedPOI> connectedPOIList = new ArrayList<>();
        connectedPOIList.addAll(start.getIndoorMap().getElevators());
        connectedPOIList.addAll(start.getIndoorMap().getEscalators());
        connectedPOIList.addAll(start.getIndoorMap().getStaircases());

        for (ConnectedPOI connectedPOI : connectedPOIList) {
            if (connectedPOI.getFloorPOI(start.getFloorNumber()) != null
                && connectedPOI.getFloorPOI(dest.getFloorNumber()) != null) {
                IndoorPOI floorPOI = connectedPOI.getFloorPOI(start.getFloorNumber());
                try {
                    int distance = floorPOI.calculateDistanceTo(start);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestConnectedPOI = connectedPOI;
                    }
                } catch (MCGADifferentFloorException e) {
                    Log.d(this.getClass().getName(),
                        "getClosestConnectedPOI: Distance calculation error for object: " + floorPOI.toString());
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
     * @param start - Starting {@link IndoorPOI } used for navigation
     * @param dest - Destination {@link IndoorPOI } used for navigation
     * @param connectedPOI - {@link ConnectedPOI} that will be used to travel from one start floor
     * to dest floor
     * @return - An ordered Map, containing {@link IndoorMap} elements as keys, with their
     * associated ordered routes as values. <br/> e.g. <br/>
     *      {<br/>
     *         IndoorMap1 : [PathfinderTile(1,1), PathfinderTile(1,2)]<br/>
     *         IndoorMap2 : [PathfinderTile(2,2), PathfinderTile(2,3)]<br/>
     *      }
     * </code>
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     * connectedPOI, or destination and connectedPOI
     */
    @NonNull
    Map<IndoorMap, List<PathFinderTile>> createPath(IndoorPOI start, IndoorPOI dest,
        ConnectedPOI connectedPOI) throws MCGAPathFindingException {
        Map<IndoorMap, List<PathFinderTile>> returnMap = new LinkedHashMap<>();

        List<PathFinderTile> startFloorList = getDirectionList(start, connectedPOI.getFloorPOI(start.getFloorNumber()));
        List<PathFinderTile> destFloorList = getDirectionList(connectedPOI.getFloorPOI(dest.getFloorNumber()), dest);
        returnMap.put(start.getIndoorMap(), startFloorList);
        returnMap.put(dest.getIndoorMap(), destFloorList);

        return returnMap;
    }

    /**
     * @param start - Start {@link IndoorPOI}, which is on the same floor as dest
     * @param dest - Destination {@link IndoorPOI}, which is on the same floor as start
     * @return A list of Tiles that are traversed by the path
     * @throws MCGAPathFindingException - thrown when there is no valid path between the start and
     * destination
     */
    List<PathFinderTile> getDirectionList(IndoorPOI start, IndoorPOI dest) throws MCGAPathFindingException {
        return new SingleMapPathFinder(start.getIndoorMap().getMap())
                .shortestPath(start, dest);
    }
}
