package com.concordia.mcga.helperClasses;

import com.akexorcist.googledirection.constant.TransportMode;

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
    }

    @Test
    public void createOutdoorDirectionsTest(){
        assertNotEquals(null,outdoorDirections);
    }

    @Test
    public void setSelectedTransportModeTest(){
        outdoorDirections.setSelectedTransportMode("");
        assertEquals(null,outdoorDirections.getSelectedTransportMode());

        outdoorDirections.setSelectedTransportMode(TransportMode.BICYCLING);
        assertEquals("bicycling",outdoorDirections.getSelectedTransportMode());
    }





}
