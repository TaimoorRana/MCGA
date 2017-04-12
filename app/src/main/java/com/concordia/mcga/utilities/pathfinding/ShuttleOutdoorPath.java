package com.concordia.mcga.utilities.pathfinding;

import android.content.Context;

import com.concordia.mcga.fragments.NavigationFragment;
import com.concordia.mcga.helperClasses.IOutdoorPath;
import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ShuttleOutdoorPath implements IOutdoorPath {
    private OutdoorPath userToShuttleStopPath;
    private OutdoorPath sgwToLoyPath;
    private OutdoorPath shuttleStopToBuildingPath;

    private final LatLng SGW_STOP = new LatLng(45.497067, -73.578463);
    private final LatLng LOY_STOP = new LatLng(45.457832, -73.638908);

    private String serverKey;
    private LatLng origin, destination;
    private List<String> instructions;
    private GoogleMap map;
    private Context context;
    private String transportMode;
    private int durationMinutes;
    private int durationHours;
    private Campus startCampus = Campus.SGW;


    public ShuttleOutdoorPath(){
        userToShuttleStopPath = new OutdoorPath();
        sgwToLoyPath = new OutdoorPath();
        shuttleStopToBuildingPath = new OutdoorPath();
        instructions = new ArrayList<>();
    }

    @Override
    public void setOrigin(LatLng origin) {
        setStartCampus();
        this.origin = origin;
        if(startCampus == Campus.SGW) {
            sgwToLoyPath.setOrigin(SGW_STOP);
            shuttleStopToBuildingPath.setOrigin(LOY_STOP);
        }else {
            sgwToLoyPath.setOrigin(LOY_STOP);
            shuttleStopToBuildingPath.setOrigin(SGW_STOP);
        }
        userToShuttleStopPath.setOrigin(origin);
    }


    @Override
    public void setDestination(LatLng destination) {
        this.destination = destination;
        if(startCampus == Campus.SGW) {
            userToShuttleStopPath.setDestination(SGW_STOP);
            sgwToLoyPath.setDestination(LOY_STOP);
        }else{
            userToShuttleStopPath.setDestination(LOY_STOP);
            sgwToLoyPath.setDestination(SGW_STOP);
        }
        shuttleStopToBuildingPath.setDestination(destination);
    }

    @Override
    public LatLng getDestination() {
        return destination;
    }

    /**
     * Makes a https request to get a direction from origin to destination
     */
    @Override
    public void requestDirection() {
        userToShuttleStopPath.requestDirection();
        sgwToLoyPath.requestDirection();
        shuttleStopToBuildingPath.requestDirection();
    }

    @Override
    public LatLng getOrigin() {
        return origin;
    }

    @Override
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
        userToShuttleStopPath.setTransportMode(MCGATransportMode.WALKING);
        sgwToLoyPath.setTransportMode(MCGATransportMode.DRIVING);
        shuttleStopToBuildingPath.setTransportMode(MCGATransportMode.WALKING);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
        userToShuttleStopPath.setContext(context);
        sgwToLoyPath.setContext(context);
        shuttleStopToBuildingPath.setContext(context);
    }

    @Override
    public String getTransportMode() {
        return transportMode;
    }

    @Override
    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
        userToShuttleStopPath.setServerKey(serverKey);
        sgwToLoyPath.setServerKey(serverKey);
        shuttleStopToBuildingPath.setServerKey(serverKey);
    }

    @Override
    public int getDurationMinutes() {
        int totalMinutes = 0;
        totalMinutes += userToShuttleStopPath.getDurationMinutes();
        totalMinutes += sgwToLoyPath.getDurationMinutes();
        totalMinutes += shuttleStopToBuildingPath.getDurationMinutes();
        durationMinutes = totalMinutes;
        return durationMinutes;
    }

    @Override
    public int getDurationHours() {
        int totalHours = 0;
        totalHours += userToShuttleStopPath.getDurationHours();
        totalHours += sgwToLoyPath.getDurationHours();
        totalHours += shuttleStopToBuildingPath.getDurationHours();
        durationMinutes = totalHours;
        return durationHours;
    }

    @Override
    public void setPathSelected(boolean isPathSelected) {
        userToShuttleStopPath.setPathSelected(isPathSelected);
        sgwToLoyPath.setPathSelected(isPathSelected);
        shuttleStopToBuildingPath.setPathSelected(isPathSelected);
    }

    @Override
    public void setMap(GoogleMap map) {
        this.map = map;
        userToShuttleStopPath.setMap(map);
        sgwToLoyPath.setMap(map);
        shuttleStopToBuildingPath.setMap(map);
    }

    /**
     * @return list of instructions to get from origin to destination
     */
    @Override
    public List<String> getInstructions() {
        List<String> allInstructions = new ArrayList<>();
        allInstructions.addAll(userToShuttleStopPath.getInstructions());
        allInstructions.addAll(sgwToLoyPath.getInstructions());
        allInstructions.addAll(shuttleStopToBuildingPath.getInstructions());
        return allInstructions;
    }

    @Override
    public void clearInstructions(){
        instructions.clear();
    }

    /**
     * Deletes Paths on the map
     */
    @Override
    public void deleteDirection() {
        shuttleStopToBuildingPath.deleteDirection();
        userToShuttleStopPath.deleteDirection();
        sgwToLoyPath.deleteDirection();
    }

    /**
     * @return Route total duration in "Xh Ym" format
     */
    @Override
    public String getDuration() {
        int totalMinutes = getDurationMinutes();
        totalMinutes += getDurationHours() * 60;

        int minutes = totalMinutes % 60;
        int hours = totalMinutes / 60;

        if(hours > 0){
            return hours + "h " + minutes+ "m";
        }else {
            return minutes+"m";
        }
    }

    /**
     * Draws the path on the map
     */
    @Override
    public void drawPath() {
        userToShuttleStopPath.drawPath();
        sgwToLoyPath.drawPath();
        shuttleStopToBuildingPath.drawPath();
    }

    public void setStartCampus(){
        Double distanceFromSGWCampus = NavigationFragment.distanceBetween(origin,Campus.SGW.getMapCoordinates());
        Double distanceFromLOYCampus = NavigationFragment.distanceBetween(origin,Campus.LOY.getMapCoordinates());
        if(distanceFromSGWCampus <= distanceFromLOYCampus){
            startCampus = Campus.SGW;
        }else{
            startCampus = Campus.LOY;
        }
    }


}
