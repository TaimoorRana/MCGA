package com.concordia.mcga.helperClasses;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class OutdoorDirections {
    private static OutdoorDirections outdoorDirections;
    private List<OutdoorPath> outdoorPathList;
    private String selectedTransportMode = null;
    private OutdoorPath selectedOutdoorPath;

    private OutdoorDirections() {
        outdoorPathList = new ArrayList<>();
        List<String> transportModes = new ArrayList<String>() {{
            add(MCGATransportMode.BICYCLING);
            add(MCGATransportMode.DRIVING);
            add(MCGATransportMode.TRANSIT);
            add(MCGATransportMode.WALKING);
        }};
        for (int i = 0; i < transportModes.size(); i++) {
            outdoorPathList.add(new OutdoorPath());
            outdoorPathList.get(i).setTransportMode(transportModes.get(i));
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
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.requestDirection();
        }
    }

    /**
     * Set origin for all transportation
     *
     * @param origin
     */
    public void setOrigin(LatLng origin) {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.setOrigin(origin);
        }
    }

    /**
     * Set destination for all transportation
     * @param destination
     */
    public void setDestination(LatLng destination) {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.setDestination(destination);
        }
    }

    /**
     * Set map for all transportation
     * @param map will be used to draw paths on
     */
    public void setMap(GoogleMap map) {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.setMap(map);
        }
    }

    /**
     * Set context for all transportation
     * @param context
     */
    public void setContext(Context context) {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.setContext(context);
        }
    }

    /**
     * @param transportMode
     * @return The duration of the transportation mode
     */
    public String getDuration(String transportMode) {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            if (outdoorPath.getTransportMode().equalsIgnoreCase(transportMode)) {
                return outdoorPath.getDuration();
            }
        }
        return "";
    }

    /**
     * Delete all paths shown on the map
     */
    public void deleteDirection() {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.deleteDirection();
        }
    }

    /**
     * Deletes all previous path, if any exists and draws a path for the selected mode of transportation
     */
    public void drawPathForSelectedTransportMode() {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.deleteDirection();
        }
        selectedOutdoorPath.drawPath();
    }

    /**
     * @return The OutdoorPath object that was selected as a transportation mode
     */
    public OutdoorPath getDirectionObject() {
        return selectedOutdoorPath != null ? selectedOutdoorPath : null;
    }

    /**
     *
     * @return directions for the selected mode of transportation
     */
    public List<String> getInstructionsForSelectedTransportMode() {
        return selectedOutdoorPath.getInstructions();
    }

    public String getSelectedTransportMode() {
        return selectedTransportMode;
    }

    /**
     * @param selectedTransportMode sets the transport mode the will be used to draw a path on the map
     */
    public void setSelectedTransportMode(String selectedTransportMode) {
        for (OutdoorPath outdoorPath : outdoorPathList) {
            if (outdoorPath.getTransportMode().equalsIgnoreCase(selectedTransportMode)) {
                selectedOutdoorPath = outdoorPath;
                this.selectedTransportMode = selectedTransportMode;
            }
        }


    }

    public void setServerKey(String serverKey){
        for (OutdoorPath outdoorPath : outdoorPathList) {
            outdoorPath.setServerKey(serverKey);
        }
    }

}
