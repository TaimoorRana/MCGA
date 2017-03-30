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
    private List<OutdoorDirection> outdoorDirectionList;
    private String selectedTransportMode = null;
    private OutdoorDirection selectedOutdoorDirection;

    private OutdoorDirections() {
        outdoorDirectionList = new ArrayList<>();
        List<String> transportModes = new ArrayList<String>() {{
            add(TransportMode.BICYCLING);
            add(TransportMode.DRIVING);
            add(TransportMode.TRANSIT);
            add(TransportMode.WALKING);
        }};
        int TOTAL_OUTDOOR_DIRECTION = 4;
        for (int i = 0; i < TOTAL_OUTDOOR_DIRECTION; i++) {
            outdoorDirectionList.add(new OutdoorDirection());
            outdoorDirectionList.get(i).setTransportMode(transportModes.get(i));
        }
    }

    /**
     * @return the instance of this class (Singleton Pattern)
     */
    public static OutdoorDirections getInstance() {
        if (outdoorDirections == null) {
            outdoorDirections = new OutdoorDirections();
        }
        return outdoorDirections;
    }

    /**
     * Request directions for all transportation
     */
    public void requestDirections() {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.requestDirection();
        }
    }

    /**
     * Set origin for all transportation
     *
     * @param origin
     */
    public void setOrigin(LatLng origin) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setOrigin(origin);
        }
    }

    /**
     * Set destination for all transportation
     * @param destination
     */
    public void setDestination(LatLng destination) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setDestination(destination);
        }
    }

    /**
     * Set map for all transportation
     * @param map will be used to draw paths on
     */
    public void setMap(GoogleMap map) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setMap(map);
        }
    }

    /**
     * Set context for all transportation
     * @param context
     */
    public void setContext(Context context) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setContext(context);
        }
    }

    /**
     * @param transportMode
     * @return The duration of the transportation mode
     */
    public String getDuration(String transportMode) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            if (outdoorDirection.getTransportMode().equalsIgnoreCase(transportMode)) {
                return outdoorDirection.getDuration();
            }
        }
        return "";
    }

    /**
     * Delete all paths shown on the map
     */
    public void deleteDirection() {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.deleteDirection();
        }
    }

    /**
     * @param selectedTransportMode sets the transport mode the will be used to draw a path on the map
     */
    public void setSelectedTransportMode(String selectedTransportMode) {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            if (outdoorDirection.getTransportMode().equalsIgnoreCase(selectedTransportMode)) {
                selectedOutdoorDirection = outdoorDirection;
                this.selectedTransportMode = selectedTransportMode;
            }
        }


    }

    /**
     * Deletes all previous path, if any exists and draws a path for the selected mode of transportation
     */
    public void drawPathForSelectedTransportMode() {
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.deleteDirection();
        }
        selectedOutdoorDirection.drawPath();
    }

    /**
     * @return The OutdoorDirection object that was selected as a transportation mode
     */
    public OutdoorDirection getDirectionObject() {
        return selectedOutdoorDirection != null ? selectedOutdoorDirection : null;
    }

    /**
     *
     * @return directions for the selected mode of transportation
     */
    public List<String> getInstructionsForSelectedTransportMode() {
        return selectedOutdoorDirection.getInstructions();
    }

    public String getSelectedTransportMode() {
        return selectedTransportMode;
    }

    public void setServerKey(String serverKey){
        for (OutdoorDirection outdoorDirection : outdoorDirectionList) {
            outdoorDirection.setServerKey(serverKey);
        }
    }

}
