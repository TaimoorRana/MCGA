package com.concordia.mcga.helperClasses;

import android.content.Context;

import com.akexorcist.googledirection.constant.TransportMode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taimoorrana on 2017-03-25.
 */

public class OutdoorDirections {
    private static OutdoorDirections outdoorDirections;
    List<OutdoorDirection> outdoorDirectionList;
    List<String> transportModes;
    String selectedTransportMode;

    public OutdoorDirections() {
        outdoorDirectionList = new ArrayList<>();
        transportModes = new ArrayList<String>() {{
            add(TransportMode.BICYCLING);
            add(TransportMode.DRIVING);
            add(TransportMode.TRANSIT);
            add(TransportMode.WALKING);
        }};
        for (int i = 0; i < 4; i++) {
            outdoorDirectionList.add(new OutdoorDirection());
            outdoorDirectionList.get(i).setTransportMode(transportModes.get(i));
        }
    }

    public static OutdoorDirections getInstance() {
        if (outdoorDirections == null) {
            outdoorDirections = new OutdoorDirections();
        }
        return outdoorDirections;
    }

    public void requestDirections() {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.requestDirection();
        }
    }

    public void setOrigin(LatLng origin) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setOrigin(origin);
        }
    }

    public void setDestination(LatLng destination) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setDestination(destination);
        }
    }

    public void setMap(GoogleMap map) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setMap(map);
        }
    }

    public void setContext(Context context) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setContext(context);
        }
    }

    public String getDuration(String transportMode) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            if (outdoorDirection.getTransportMode().equalsIgnoreCase(transportMode)) {
                return outdoorDirection.getDuration();
            }
        }
        return "";
    }

    public void deleteDirection() {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.deleteDirection();
        }
    }


    public void setSelectedTransportMode(String selectedTransportMode) {
        this.selectedTransportMode = selectedTransportMode;
    }

    public void drawPathForSelectedTransportMode() {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.deleteDirection();
            if (outdoorDirection.getTransportMode().equalsIgnoreCase(selectedTransportMode)) {
                outdoorDirection.drawPath();
            }
        }
    }


}
