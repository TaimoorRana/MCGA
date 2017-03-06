package com.concordia.mcga.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.Manifest;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.helperClasses.Observer;
import com.concordia.mcga.helperClasses.Subject;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class NavigationFragment extends Fragment implements OnMapReadyCallback, OnCameraIdleListener, Subject {

    private final float CAMPUS_DEFAULT_ZOOM_LEVEL = 16f;
    Campus currentCampus = Campus.SGW;
    private GoogleMap map;
    private List<Observer> observerList = new ArrayList<>();
    //State
    private ViewType viewType;
    //Fragments
    private LinearLayoutCompat parentLayout;
    private SupportMapFragment mapFragment;
    private TransportButtonFragment transportButtonFragment;
    private IndoorMapFragment indoorMapFragment;
    //View Components
    private Button campusButton;
    private Button viewSwitchButton;
    private FloatingActionButton mapCenterButton;
    private EditText navigationSearch;
    //GPS attributes
    private LocationManager gpsmanager;
    private LatLng myPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentLayout = (LinearLayoutCompat) inflater.inflate(R.layout.nav_main_fragment, container, false);

        //Init Fragments
        transportButtonFragment = (TransportButtonFragment) getChildFragmentManager().findFragmentById(R.id.transportButton);
        indoorMapFragment = (IndoorMapFragment) getChildFragmentManager().findFragmentById(R.id.indoormap);
        //Init View Components
        navigationSearch = (EditText) parentLayout.findViewById(R.id.navigationSearch);
        mapCenterButton = (FloatingActionButton) parentLayout.findViewById(R.id.mapCenterButton);
        mapCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean conditionGPS = false;
                Log.d("Testing mapCenterButton", "Initializing OnClickListener");
                if (!gpsmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //Check if GPS is turned on in the phone
                    Log.d("Testing AlertGPS Launch", "Initializing method");
                    AlertGPS(); //Run GPS activation method
                    Log.d("Testing AlertGPS Launch", "Finished AlertGPS");
                    if (gpsmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        conditionGPS = true;
                    }
                }
                Log.d("Testing", "Checkpoint 1 - Button initializer");
                if (viewType == ViewType.INDOOR) { //Move to Outdoor if the user is viewing the indoor
                    viewType = ViewType.OUTDOOR;
                    getChildFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
                    getChildFragmentManager().beginTransaction().show(transportButtonFragment).commit();
                    campusButton.setVisibility(View.VISIBLE);
                    viewSwitchButton.setText("GO OUTDOORS");
                }
                if (navigationSearch.getText() != null) { //Clear Text Label - This is subject to change depending on how MCGA-12 goes
                    navigationSearch.setText("");
                }
                if (conditionGPS) { //Verify GPS is on before running location method and implementing the marker
                    locateMe();
                    Log.d("Test 2", "Checkpoint Manifest check");
                }
                if (ContextCompat.checkSelfPermission(mapFragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == //Enable the Layer if permission found in Android Manifest
                        PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission checked", "checkSelfPermission passed with no errors");
                    map.setMyLocationEnabled(true); //Enabling the Location layer
                    Log.d("Permission checked", "Location Layer implementation succesful");
                } else {
                    //Request the Permission from the manifest
                    ActivityCompat.requestPermissions(mapFragment.getActivity(), new String[]{ //Requesting the permission
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1); //Give dummy reference number referring to permission rationale
                }
            }
        });

        campusButton = (Button) parentLayout.findViewById(R.id.campusButton);
        viewSwitchButton = (Button) parentLayout.findViewById(R.id.viewSwitchButton);
        viewSwitchButton.setText("GO INDOORS");
        viewSwitchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType == ViewType.OUTDOOR) {
                    viewType = ViewType.INDOOR;
                    getChildFragmentManager().beginTransaction().show(indoorMapFragment).hide(mapFragment).commit();
                    getChildFragmentManager().beginTransaction().hide(transportButtonFragment).commit();
                    campusButton.setVisibility(View.GONE);
                    viewSwitchButton.setText("GO OUTDOORS");
                } else {
                    viewType = ViewType.OUTDOOR;
                    getChildFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
                    getChildFragmentManager().beginTransaction().show(transportButtonFragment).commit();
                    campusButton.setVisibility(View.VISIBLE);
                    viewSwitchButton.setText("GO INDOORS");
                }
            }
        });

        //Set initial view type
        viewType = ViewType.OUTDOOR;

        //Hide Indoor Fragment
        getChildFragmentManager().beginTransaction().hide(indoorMapFragment).commit();

        return parentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gpsmanager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE); //Initialization of locationmanager object with the Location_Service

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button toggleButton = (Button) getView().findViewById(R.id.campusButton);
        toggleButton.setBackgroundColor(Color.parseColor("#850f02"));
        toggleButton.setTextColor(Color.WHITE);
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCampus == Campus.LOY) {
                    currentCampus = Campus.SGW;
                } else {
                    currentCampus = Campus.LOY;
                }
                updateCampus();
            }
        });
        //Show outdoor map on start
        getFragmentManager().beginTransaction().show(mapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);

        //Settings
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(true); //Enable touchscreen motion array for Google map object
        map.getUiSettings().setMyLocationButtonEnabled(false);  //Turn off  the built-in GPS locator button

        //Map Customization
        applyCustomGoogleMapsStyle();
        Campus.populateCampusesWithBuildings();
        addBuildingMarkers();
        updateCampus();
    }

    private void addBuildingMarkers() {
        List<Building> sgwBuildings = Campus.SGW.getBuildings();
        List<Building> loyBuildings = Campus.LOY.getBuildings();


        for (Building building : sgwBuildings) {
            createBuildingMarkersAndPolygonOverlay(building);
        }

        for (Building building : loyBuildings) {
            createBuildingMarkersAndPolygonOverlay(building);
        }
        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                ((MainActivity) getActivity()).createToast("Building Clicked");
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ((MainActivity) getActivity()).createToast("Building Clicked");
                return true;
            }
        });
    }

    private void createBuildingMarkersAndPolygonOverlay(Building building) {
        register(building);
        map.addPolygon(building.getPolygonOverlayOptions()).setClickable(true);

        Marker marker = map.addMarker(building.getMarkerOptions());
        building.setMarker(marker);
    }

    private void applyCustomGoogleMapsStyle() {
        try {
            // Customise the styling of the base map using a JSON object define
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));

            if (!success) {
                Log.e("Google Map Style", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Google Map Style", "Can't find style. Error: ", e);
        }
    }

    void updateCampus() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCampus.getMapCoordinates(), CAMPUS_DEFAULT_ZOOM_LEVEL));
    }

    @Override
    public void onCameraIdle() {
        notifyObservers();
    }

    @Override
    public void register(Observer observer) {
        if (observer != null) {
            observerList.add(observer);
        }
    }

    @Override
    public void unRegister(Observer observer) {
        if (observer != null) {
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(map.getCameraPosition().zoom);
        }
    }

    private enum ViewType {
        INDOOR, OUTDOOR
    }

    public void AlertGPS() { //This function alerts the user if the GPS functionality of the phone is turned off and prompts the user to turn it on
        Log.e("Testing Alert GPS", "Alert GPS Start");
        AlertDialog.Builder build = new AlertDialog.Builder(
                mapFragment.getActivity());
        Log.e("Testing Alert GPS", "AlertDialog builder successful");
        build
                .setTitle("GPS Detection Services")
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(i);
                            }
                        });
        build.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = build.create();
        alert.show();
    }

    public void locateMe() {


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE); // Getting LocationManager object from System Service LOCATION_SERVICE
        Criteria criteria = new Criteria();// Creating a criteria object to retrieve provider
        String provider = locationManager.getBestProvider(criteria, true);// Getting the name of the best provider
        if (ContextCompat.checkSelfPermission(mapFragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //Checking permission in the manifest

            ActivityCompat.requestPermissions(mapFragment.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2); //Requesting the permission
        }
        Location location = locationManager.getLastKnownLocation(provider); // Missing Permissions - Getting Current Location, problem is public Location class constructor was overridden by Arek and can't take in a String

        if (location != null) {
            double latitude = location.getLatitude(); // Getting latitude of the current location
            double longitude = location.getLongitude(); // Getting longitude of the current location
            myPosition = new LatLng(latitude, longitude); // Creating a LatLng object for the current location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, CAMPUS_DEFAULT_ZOOM_LEVEL));//Camera Update method to the position
        }
    }
}



