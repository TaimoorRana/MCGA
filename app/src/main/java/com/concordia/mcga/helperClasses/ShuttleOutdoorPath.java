package com.concordia.mcga.helperClasses;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ShuttleOutdoorPath implements IOutdoorPath {
    private OutdoorPath userToShuttleStopPath;
    private OutdoorPath sgwToLoyPath;
    private OutdoorPath shuttleStopToBuildingPath;

    private final LatLng SGW_STOP = new LatLng(45.497067, -73.578463);
    private final LatLng LOY_STOP = new LatLng(45.457832, -73.638908);
    private final LatLng USER_LOCATION = new LatLng(45.497480, -73.578109);

    private String serverKey;
    private LatLng origin, destination;
    private List<String> instructions;
    private GoogleMap map;
    private Context context;
    private String transportMode;
    private int durationMinutes;
    private int durationHours;


    public ShuttleOutdoorPath(){
        userToShuttleStopPath = new OutdoorPath();
        userToShuttleStopPath.setTransitPathColor(0x801767e8);
        sgwToLoyPath = new OutdoorPath();
        shuttleStopToBuildingPath = new OutdoorPath();
        shuttleStopToBuildingPath.setTransitPathColor(0x801767e8);
    }

    @Override
    public void setOrigin(LatLng origin) {
        this.origin = origin;
        userToShuttleStopPath.setOrigin(USER_LOCATION); // user's position
        sgwToLoyPath.setOrigin(SGW_STOP);
        shuttleStopToBuildingPath.setOrigin(LOY_STOP);
    }


    @Override
    public void setDestination(LatLng destination) {
        this.destination = destination;
        userToShuttleStopPath.setDestination(SGW_STOP);
        sgwToLoyPath.setDestination(LOY_STOP);
        shuttleStopToBuildingPath.setDestination(destination);
    }

    @Override
    public LatLng getDestination() {
        return destination;
    }

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
        return 0;
    }

    @Override
    public int getDurationHours() {
        return 0;
    }

    @Override
    public void setMap(GoogleMap map) {
        this.map = map;
        userToShuttleStopPath.setMap(map);
        sgwToLoyPath.setMap(map);
        shuttleStopToBuildingPath.setMap(map);
    }

    @Override
    public List<String> getInstructions() {
        return null;
    }

    @Override
    public void deleteDirection() {
        shuttleStopToBuildingPath.deleteDirection();
        userToShuttleStopPath.deleteDirection();
        sgwToLoyPath.deleteDirection();
    }

    @Override
    public String getDuration() {
        return null;
    }

    @Override
    public void drawPath() {
        userToShuttleStopPath.drawPath();
        sgwToLoyPath.drawPath();
        shuttleStopToBuildingPath.drawPath();
    }
}
