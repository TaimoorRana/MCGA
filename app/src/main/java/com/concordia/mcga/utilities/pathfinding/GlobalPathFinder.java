package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.concordia.mcga.helperClasses.OutdoorDirections;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.POI;
import com.concordia.mcga.models.Room;
import com.google.android.gms.maps.model.LatLng;
import com.jcabi.aspects.Timeable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GlobalPathFinder implements Runnable {

    public enum PathType {
        INDOOR_TO_OUTDOOR,
        OUTDOOR_TO_INDOOR,
        INDOOR_TO_OUTDOOR_TO_INDOOR
    }

    private final POI startPOI;
    private final POI destPOI;
    private MainActivity activity;
    private PathType pathType;

    private Map<Floor, List<IndoorMapTile>> startDirections;
    private Map<Floor, List<IndoorMapTile>> endDirections;

    private Thread startIndoorDirectionsThread;

    private OutdoorDirections outdoorDirections;

    /**
     * @param activity reference to the MainActivity
     * @param startPOI
     * @param destPOI
     */
    public GlobalPathFinder(MainActivity activity, POI startPOI, POI destPOI) {
        this.startPOI = startPOI;
        this.destPOI = destPOI;
        this.activity = activity;
        this.outdoorDirections = activity.getNavigationFragment().getOutdoorDirections();

        this.startDirections = new HashMap<>();
        this.endDirections = new HashMap<>();
    }

    @Override
    public void run() {
        //First determine type of path by analyzing POIs
        determinePathType();

        //Depending on the type of path, generate the appropriate parameters.
        switch (pathType) {
            case INDOOR_TO_OUTDOOR:
                generateStartIndoorDirections();
                break;
            case OUTDOOR_TO_INDOOR:
                generateDestIndoorDirections();
                break;
            case INDOOR_TO_OUTDOOR_TO_INDOOR:
                generateStartIndoorDirections();
                generateDestIndoorDirections();
                break;
        }

        generateOutdoorDirections();

        if (startIndoorDirectionsThread != null) {
            try {
                startIndoorDirectionsThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // LOGIC HERE
        activity.notifyPathfindingComplete();
    }

    private void determinePathType() {
        if (startPOI instanceof Room && destPOI instanceof Room) {
            pathType = PathType.INDOOR_TO_OUTDOOR_TO_INDOOR;
        } else if (startPOI instanceof Room && destPOI instanceof POI) {
            pathType = PathType.INDOOR_TO_OUTDOOR;
        } else if (startPOI instanceof POI && destPOI instanceof Room) {
            pathType = PathType.OUTDOOR_TO_INDOOR;
        }
    }

    private void generateOutdoorDirections() {
        outdoorDirections.setMap(activity.getNavigationFragment().getGoogleMap());

        LatLng buildingStart = null;
        if (startPOI instanceof Room) {
            buildingStart = ((Room) startPOI).getFloor().getBuilding().getMapCoordinates();
        }
        outdoorDirections.setOrigin(buildingStart);
        outdoorDirections.setDestination(destPOI.getMapCoordinates());
        
        outdoorDirections.requestDirections();
    }

    private void generateStartIndoorDirections() {
        IndoorPOI exitPortal = ((Room) startPOI).getFloor().getBuilding().getDefaultPortal();
        GeneratePath indoorDirections = new GeneratePath((IndoorPOI) startPOI, exitPortal, true);
        startIndoorDirectionsThread = new Thread(indoorDirections);

        startIndoorDirectionsThread.start();
    }

    private void generateDestIndoorDirections() {
        //GeneratePath indoorDirections = new GeneratePath();
     /*   Thread startIndoorDirectionsThread = new Thread(indoorDirections);

        startIndoorDirectionsThread.start();*/
    }


    /**
     * Returns building directions for the indoor start building
     *
     * @return An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code> <b>Returns null if there is no start indoor directions</b>
     */
    public Map<Floor, List<IndoorMapTile>> getStartBuildingDirections() {
        return  this.startDirections;
    }

    /**
     * Returns building directions for the indoor destination building
     *
     * @return An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code> <b>Returns null if there is no dest indoor directions</b>
     */
    public Map<Floor, List<IndoorMapTile>> getDestBuildingDirections() {
        return  this.endDirections;
    }


    private void setStartDirections(Map<Floor, List<IndoorMapTile>> startDirections) {
        this.startDirections = startDirections;
    }

    private void setEndDirections(Map<Floor, List<IndoorMapTile>> endDirections) {
        this.endDirections = endDirections;
    }

    /**
     * @return A LatLng Array, where index 0 is the start of the outdoor component
     * and index 1 is the destination of the outdoor component
     */
    public OutdoorDirections getOutdoorDirections() {
        return outdoorDirections;
    }

    public POI getStartPOI() {
        return startPOI;
    }

    public POI getDestPOI() {
        return destPOI;
    }

    public PathType getPathType() {
        return pathType;
    }

    private class GeneratePath implements Runnable {


        private final IndoorPOI start;
        private final IndoorPOI dest;
        private final boolean isStart;

       // private Map<Floor, List<IndoorMapTile>> directions;

        public GeneratePath(IndoorPOI start, IndoorPOI dest, boolean isStart) {
            this.start = start;
            this.dest = dest;
            this.isStart = isStart;

            try {
                populateTiledMaps();
            } catch (MCGADatabaseException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void populateTiledMaps() throws MCGADatabaseException {
            if (this.start.getFloor().equals(this.dest.getFloor())) {
                this.start.getFloor().populateTiledMap();
            } else {
                this.start.getFloor().populateTiledMap();
                this.dest.getFloor().populateTiledMap();
            }

        }

        public void depopulateTiledMaps() {
            if (this.start.getFloor().equals(this.dest.getFloor())) {
                this.start.getFloor().clearTiledMap();
            } else {
                this.start.getFloor().clearTiledMap();
                this.dest.getFloor().clearTiledMap();
            }

        }

        @Override
        @Timeable(limit = 6, unit = TimeUnit.SECONDS)
        public void run() {
            MultiMapPathFinder pf = new MultiMapPathFinder();
            Map<Floor, List<IndoorMapTile>> pathTiles;
            Map<Floor, List<IndoorMapTile>> pathTilesJunctions = new HashMap<>();
            try {
                pathTiles = pf.shortestPath(start, dest);

                for (Map.Entry<Floor, List<IndoorMapTile>> pair : pathTiles.entrySet()) {
                    pathTilesJunctions.put(pair.getKey(), SingleMapPathFinder.shortestPathJunctions(pair.getValue()));
                }

            } catch (MCGAPathFindingException e) {
                e.printStackTrace();
            }

            if (isStart) {
                setStartDirections(pathTilesJunctions);
            } else {
                setEndDirections(pathTilesJunctions);
            }

            pathTiles = null;
            pathTilesJunctions = null;
            depopulateTiledMaps();
        }
    }
}
