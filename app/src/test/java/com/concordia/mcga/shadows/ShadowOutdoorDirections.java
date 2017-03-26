package com.concordia.mcga.shadows;

import com.akexorcist.googledirection.constant.TransportMode;
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
            case TransportMode.DRIVING:
                return "1 minute";
            case TransportMode.TRANSIT:
                return "2 minutes";
            case TransportMode.BICYCLING:
                return "3 minutes";
            case TransportMode.WALKING:
                return "4 minutes";
        }
        return "";
    }
}
