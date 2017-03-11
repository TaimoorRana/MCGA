package com.concordia.mcga.helperClasses;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class OutdoorDirectionTest {
    OutdoorDirection direction = new OutdoorDirection();
    LatLng origin, destination;

    @Before
    public void setUp() throws Exception {
        origin = new LatLng(45.458563, -73.640156);
        destination = new LatLng(45.497100, -73.579077);
        direction.setOrigin(origin);
        direction.setDestination(destination);
    }

    @Test
    public void onDirectionSuccess() throws Exception {

    }

    @Test
    public void requestDirection() throws Exception {

    }

    @Test
    public void getDistance() throws Exception {

    }

    @Test
    public void getDuration() throws Exception {

    }

    @Test
    public void getOrigin() throws Exception {
        assertEquals(origin, direction.getOrigin());
    }

    @Test
    public void setOrigin() throws Exception {

    }

    @Test
    public void getDestination() throws Exception {
        assertEquals(destination, direction.getDestination());
    }

    @Test
    public void setDestination() throws Exception {

    }

    @Test
    public void getServerKey() throws Exception {

    }

    @Test
    public void getPolylines() throws Exception {

    }

    @Test
    public void setPolylines() throws Exception {

    }

    @Test
    public void getOriginMarker() throws Exception {

    }

    @Test
    public void setOriginMarker() throws Exception {

    }

    @Test
    public void getDestinationMarker() throws Exception {

    }

    @Test
    public void setDestinationMarker() throws Exception {

    }

    @Test
    public void getLeg() throws Exception {

    }

    @Test
    public void setLeg() throws Exception {

    }

    @Test
    public void getMap() throws Exception {

    }

    @Test
    public void setMap() throws Exception {

    }

    @Test
    public void getContext() throws Exception {

    }

    @Test
    public void setContext() throws Exception {

    }

    @Test
    public void getTransportMode() throws Exception {

    }

    @Test
    public void setTransportMode() throws Exception {

    }

    @Test
    public void getSteps() throws Exception {

    }

    @Test
    public void setSteps() throws Exception {

    }

    @Test
    public void deleteDirection() throws Exception {

    }

    @Test
    public void getInstructions() throws Exception {

    }

}