package com.concordia.mcga.helperClasses;

import com.akexorcist.googledirection.constant.TransportMode;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Created by taimoorrana on 2017-03-30.
 */

@RunWith(JUnit4.class)
public class OutdoorDirectionsTest {
    OutdoorDirections outdoorDirections;

    @Before
    public void setup(){
        outdoorDirections = OutdoorDirections.getInstance();
        outdoorDirections.setSelectedTransportMode(TransportMode.BICYCLING);
    }

    @Test
    public void createOutdoorDirectionsTest(){
        assertNotEquals(null,outdoorDirections);
    }

    @Test
    public void setSelectedTransportModeTest(){
        assertEquals("bicycling",outdoorDirections.getSelectedTransportMode());
    }

    @Test
    public void setOriginTest(){
        LatLng origin =  new LatLng(45.0, 47.0);
        outdoorDirections.setOrigin(origin);
        assertEquals(origin,outdoorDirections.getDirectionObject().getOrigin());
    }

    @Test
    public void setDestinationTest(){
        LatLng destination =  new LatLng(44.0, 46.0);
        outdoorDirections.setDestination(destination);
        assertEquals(destination,outdoorDirections.getDirectionObject().getDestination());
    }






}
