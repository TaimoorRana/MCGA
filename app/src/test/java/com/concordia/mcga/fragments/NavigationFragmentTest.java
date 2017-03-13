package com.concordia.mcga.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;


import com.concordia.mcga.helperClasses.Observer;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NavigationFragmentTest {


    //Outdoor Map
    private final float CAMPUS_DEFAULT_ZOOM_LEVEL = 16f;
    private GoogleMap map;

    //State

    //Fragments
    private SupportMapFragment mapFragment;

    //View Components
    private Button campusButton;
    private Button viewSwitchButton;
    private FloatingActionButton mapCenterButton;
    //GPS attributes
    private LocationManager gpsmanager;
    private LatLng myPosition;
    Criteria criteria = new Criteria();
    private Location location;

    // Search components
    private Dialog searchDialog;

    @Before
    public void setUp() throws Exception {
        //myPosition = new LatLng(45.4968, -73.5788);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void alertGPS_isSuccessful() throws Exception {
        //Test skip, android core functionality
    }

    @Test
    public void locateMe_locatesMe() throws Exception {
        /*assertEquals(ContextCompat.checkSelfPermission(mapFragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION), PackageManager.PERMISSION_GRANTED);
        assertNotNull(mapFragment.getActivity().getSystemService(Context.LOCATION_SERVICE));
        assertNotNull(gpsmanager.getBestProvider(criteria, true));
        assertNotNull(location);
        assertNotNull(myPosition);*/
        //Move Camera test, need to read more
    }

    @Test
    public void onActivityCreated() throws Exception {

    }

}

