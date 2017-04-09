package com.concordia.mcga.shadows;

import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.concordia.mcga.helperClasses.OutdoorDirections;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Created by taimoorrana on 2017-03-26.
 */
@Implements(OutdoorDirections.class)
public class ShadowOutdoorDirections {

    @Implementation
    public String getDuration(String transportMode) {
        switch (transportMode) {
            case MCGATransportMode.DRIVING:
                return "1 minute";
            case MCGATransportMode.TRANSIT:
                return "2 minutes";
            case MCGATransportMode.BICYCLING:
                return "3 minutes";
            case MCGATransportMode.WALKING:
                return "4 minutes";
            default:
                return "";
        }
    }

    @Implementation
    public int getHoursForTransportType(String transportMode){
        switch (transportMode) {
            case MCGATransportMode.DRIVING:
                return 1;
            case MCGATransportMode.TRANSIT:
                return 2;
            case MCGATransportMode.BICYCLING:
                return 3;
            case MCGATransportMode.WALKING:
                return 4;
            default:
                return 0;
        }
    }

    @Implementation
    public int getMinutesForTransportType(String transportMode){
        switch (transportMode) {
            case MCGATransportMode.DRIVING:
                return 1;
            case MCGATransportMode.TRANSIT:
                return 2;
            case MCGATransportMode.BICYCLING:
                return 3;
            case MCGATransportMode.WALKING:
                return 4;
            default:
                return 0;
        }
    }
}
