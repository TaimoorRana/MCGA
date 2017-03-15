package com.concordia.mcga.fragments;

// Junit Import
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class NavigationFragmentTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void clickGPSButton_ChecksGPSFail() throws Exception {   //Problematic void that takes no argument and return nothing
        //Test fails, brings the user to the GPS screen so he can enable it

    }

    @Test
    public void clickGPSButton_CheckGPSSuccess() throws Exception{  //Problematic void that takes no argument and return nothing
        //Test, loads new location on the map, centers map on LatLng
    }

    @Test
    public void locateMe_locatesMe() throws Exception { //Problematic void that takes no argument and return nothing
        /*assertEquals(ContextCompat.checkSelfPermission(mapFragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION), PackageManager.PERMISSION_GRANTED, 0);
        assertNotNull(mapFragment.getActivity().getSystemService(Context.LOCATION_SERVICE));
        assertNotNull(gpsmanager.getBestProvider(criteria, true));
        assertNull(location);
        assertNull(myPosition);*/
    }

    /*@Test
    public void onActivityCreated() throws Exception {
    Temporarily put on hold (future modifications will be brought in by pull requests)
    }*/

}

