package com.concordia.mcga.helperClasses;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class OutdoorDirectionsTest {
    private OutdoorDirections outdoorDirections;
    private LatLng origin, destination;

    @Before
    public void setUp(){
        outdoorDirections = new OutdoorDirections();
        outdoorDirections.setSelectedTransportMode(MCGATransportMode.BICYCLING);
        origin =  new LatLng(45.0, 47.0);
        outdoorDirections.setOrigin(origin);
        destination =  new LatLng(44.0, 46.0);
        outdoorDirections.setDestination(destination);
        outdoorDirections.requestDirections();
    }

    @Test
    public void createOutdoorDirectionsTest(){
        assertNotNull(outdoorDirections);
    }

    @Test
    public void setSelectedTransportModeTest(){
        assertEquals("bicycling",outdoorDirections.getSelectedTransportMode());
    }

    @Test
    public void setOriginTest(){
        assertEquals(origin,outdoorDirections.getDirectionObject().getOrigin());
    }

    @Test
    public void setDestinationTest(){
        assertEquals(destination,outdoorDirections.getDirectionObject().getDestination());
    }







}
