package com.concordia.mcga.helperClasses;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class ShuttleOutdoorPath extends OutdoorPath {
    private OutdoorPath userToShuttleStopPath;
    private OutdoorPath sgwToLoyPath;
    private OutdoorPath shuttleStopToBuildingPath;

    private final LatLng SGW_STOP = new LatLng(45.497067, -73.578463);
    private final LatLng LOY_STOP = new LatLng(45.457832, -73.638908);
    private final LatLng USER_LOCATION = new LatLng(45.497480, -73.578109);

    public ShuttleOutdoorPath(){
        userToShuttleStopPath = new OutdoorPath();
        sgwToLoyPath = new OutdoorPath();
        shuttleStopToBuildingPath = new OutdoorPath();
    }

    @Override
    public void setOrigin(LatLng origin) {
        userToShuttleStopPath.setOrigin(USER_LOCATION); // user's position
        sgwToLoyPath.setOrigin(SGW_STOP);
        shuttleStopToBuildingPath.setOrigin(LOY_STOP);
    }


    @Override
    public void setDestination(LatLng destination) {
        userToShuttleStopPath.setDestination(SGW_STOP);
        sgwToLoyPath.setDestination(LOY_STOP);
        shuttleStopToBuildingPath.setDestination(destination);
    }

    @Override
    public void requestDirection() {
        userToShuttleStopPath.requestDirection();
        sgwToLoyPath.requestDirection();
        shuttleStopToBuildingPath.requestDirection();
    }

    @Override
    public void setTransportMode(String transportMode) {
        userToShuttleStopPath.setTransportMode(MCGATransportMode.WALKING);
        sgwToLoyPath.setTransportMode(MCGATransportMode.BICYCLING);
        shuttleStopToBuildingPath.setTransportMode(MCGATransportMode.WALKING);
    }

    @Override
    public void setContext(Context context) {
        userToShuttleStopPath.setContext(context);
        sgwToLoyPath.setContext(context);
        shuttleStopToBuildingPath.setContext(context);
    }

    @Override
    public void setServerKey(String serverKey) {
        userToShuttleStopPath.setServerKey(serverKey);
        sgwToLoyPath.setServerKey(serverKey);
        shuttleStopToBuildingPath.setServerKey(serverKey);
    }

    @Override
    public void setMap(GoogleMap map) {
        userToShuttleStopPath.setMap(map);
        sgwToLoyPath.setMap(map);
        shuttleStopToBuildingPath.setMap(map);
    }

    @Override
    public void drawPath() {
        userToShuttleStopPath.drawPath();
        sgwToLoyPath.drawPath();
        shuttleStopToBuildingPath.drawPath();
    }
}
