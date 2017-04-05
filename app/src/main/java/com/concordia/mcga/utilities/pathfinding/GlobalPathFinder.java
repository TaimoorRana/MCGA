package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.POI;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public class GlobalPathFinder implements Runnable {
    private final POI startPOI;
    private final POI destPOI;
    private MainActivity activity;
    private Map<Floor, List<IndoorMapTile>> startBuildingDirections;
    private Map<Floor, List<IndoorMapTile>> destBuildingDirections;
    private LatLng[] outdoorCoordinates;
    private MultiMapPathFinder indoorPathFinder;

    /**
     * @param activity reference to the MainActivity
     * @param startPOI
     * @param destPOI
     */
    GlobalPathFinder(MainActivity activity, POI startPOI, POI destPOI) {
        this.startPOI = startPOI;
        this.destPOI = destPOI;
        this.activity = activity;
        indoorPathFinder = new MultiMapPathFinder();
    }

    @Override
    public void run() {
        try {
            if (startPOI instanceof IndoorPOI && destPOI instanceof IndoorPOI) {
                if (
                        ((IndoorPOI) startPOI).getFloor().getBuilding()
                        .equals(
                        ((IndoorPOI) destPOI).getFloor().getBuilding())
                    ) {
                    sameBuildingNavigation();
                } else {
                    differentBuildingNavigation();
                }
            } else if (startPOI instanceof IndoorPOI) {
                indoorToOutdoorNavigation();
            } else if (destPOI instanceof IndoorPOI) {
                outdoorToIndoorNavigation();
            } else { // Both POIs are external
                externalOnlyNavigation();
            }
        } catch (MCGAPathFindingException e) {
            Log.e(this.getClass().getName(), "ERROR generating navigation path");
        }
        activity.notifyPathfindingComplete();
    }

    private void indoorToOutdoorNavigation() throws MCGAPathFindingException {
        IndoorPOI startIndoorPOI = (IndoorPOI) startPOI;
        IndoorPOI destIndoorPOI = ((IndoorPOI) startPOI).getFloor().getBuilding().getPortals().iterator().next();

        startBuildingDirections = indoorPathFinder.shortestPath(startIndoorPOI, destIndoorPOI);

        outdoorCoordinates = new LatLng[2];
        outdoorCoordinates[0] = destIndoorPOI.getMapCoordinates();
        outdoorCoordinates[1] = destPOI.getMapCoordinates();
    }

    private void outdoorToIndoorNavigation() throws MCGAPathFindingException {
        IndoorPOI destIndoorPOI = (IndoorPOI) destPOI;
        IndoorPOI startIndoorPOI = destIndoorPOI.getFloor().getBuilding().getPortals().iterator().next();

        destBuildingDirections = indoorPathFinder.shortestPath(startIndoorPOI, destIndoorPOI);

        outdoorCoordinates = new LatLng[2];
        outdoorCoordinates[0] = startPOI.getMapCoordinates();
        outdoorCoordinates[1] = startIndoorPOI.getMapCoordinates();

    }

    private void externalOnlyNavigation() {
        outdoorCoordinates = new LatLng[2];
        outdoorCoordinates[0] = startPOI.getMapCoordinates();
        outdoorCoordinates[1] = destPOI.getMapCoordinates();
    }

    private void differentBuildingNavigation() throws MCGAPathFindingException {
        IndoorPOI start1IndoorPOI = (IndoorPOI) startPOI;
        IndoorPOI dest1IndoorPOI = ((IndoorPOI) startPOI).getFloor().getBuilding().getPortals().iterator().next();

        startBuildingDirections = indoorPathFinder.shortestPath(start1IndoorPOI, dest1IndoorPOI);

        IndoorPOI start2IndoorPOI = ((IndoorPOI) destPOI).getFloor().getBuilding().getPortals().iterator().next();
        IndoorPOI dest2IndoorPOI = ((IndoorPOI) destPOI);

        destBuildingDirections = indoorPathFinder.shortestPath(start2IndoorPOI, dest2IndoorPOI);

        outdoorCoordinates = new LatLng[2];
        outdoorCoordinates[0] = dest1IndoorPOI.getMapCoordinates();
        outdoorCoordinates[1] = start2IndoorPOI.getMapCoordinates();
    }

    private void sameBuildingNavigation() throws MCGAPathFindingException {
        IndoorPOI startIndoorPOI = (IndoorPOI) startPOI;
        IndoorPOI destIndoorPOI = (IndoorPOI) destPOI;

        startBuildingDirections = indoorPathFinder.shortestPath(startIndoorPOI, destIndoorPOI);

    }

    /**
     * Returns building directions for the indoor start building
     *
     * @return An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code> <b>Returns null if there is no start indoor directions. I.e. if the start
     * POI is a non-indoor POI</b>
     */
    public Map<Floor, List<IndoorMapTile>> getStartBuildingDirections() {
        return startBuildingDirections;
    }

    /**
     * Returns building directions for the indoor destination building
     *
     * @return An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code> <b>Returns null if there is no dest indoor directions. i.e. if the destination is outdoors,
     * or the directions are from an IndoorPOI to an IndoorPOI in the same building</b>
     */
    public Map<Floor, List<IndoorMapTile>> getDestBuildingDirections() {
        return destBuildingDirections;
    }

    /**
     * @return A LatLng Array, where index 0 is the start of the outdoor component
     * and index 1 is the destination of the outdoor component. Returns null if
     * there is no oudoor component to the directions.
     */
    public LatLng[] getOutDoorCoordinates() {
        return outdoorCoordinates;
    }

    public POI getStartPOI() {
        return startPOI;
    }

    public POI getDestPOI() {
        return destPOI;
    }

    void setIndoorPathFinder(MultiMapPathFinder indoorPathFinder) {
        this.indoorPathFinder = indoorPathFinder;
    }
}

