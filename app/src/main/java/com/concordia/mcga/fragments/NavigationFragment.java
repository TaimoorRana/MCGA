package com.concordia.mcga.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
    LocationListener gpsListen = new LocationListener() {
        public void onLocationChanged(Location location) {
            //Method called when new location is found by the network

            Log.d("Message: ", "Location changed," + location.getLatitude() + "," + location.getLongitude() + ".");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
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
    private LocationManager gpsmanager; //LocationManager instance to check gps activity
    private LatLng myPosition; //Creating LatLng to store current position
    private Location location;


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
                Log.d("Testing mapCenterButton", "Initializing OnClickListener");
                if (!gpsmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.d("Testing AlertGPS Launch", "Initializing method");
                    AlertGPS();
                    Log.d("Testing AlertGPS Launch", "Finished AlertGPS");

                }
                Log.d("Testing", "Checkpoint 1 - Button initializer");
                if (viewType == ViewType.INDOOR) {
                    viewType = ViewType.OUTDOOR;
                    getChildFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
                    getChildFragmentManager().beginTransaction().show(transportButtonFragment).commit(); //To be removed after Outside transportation Google API incorporation
                    campusButton.setVisibility(View.VISIBLE);
                    viewSwitchButton.setText("GO INDOORS");
                }
                if (navigationSearch.getText() != null) { //Clear Text Label - This is subject to a ton of changes depending on how MCGA-12 goes
                    navigationSearch.setText("");
                }

                locateMe();

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
        gpsmanager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

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
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        //Map Customization
        applyCustomGoogleMapsStyle();
        Campus.populateCampusesWithBuildings();
        addBuildingMarkersAndPolygons();

        updateCampus();
    }

    /**
     * add markers and polygons overlay for each building
     */
    private void addBuildingMarkersAndPolygons() {
        final List<Building> sgwBuildings = Campus.SGW.getBuildings();
        final List<Building> loyBuildings = Campus.LOY.getBuildings();

        for (Building building : sgwBuildings) {
            createBuildingMarkersAndPolygonOverlay(building);
        }

        for (Building building : loyBuildings) {
            createBuildingMarkersAndPolygonOverlay(building);
        }
        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                /**
                 * ONLY FOR DEMO PURPOSES
                 */
                Building building = Campus.SGW.getBuilding(polygon);
                if (building == null) {
                    building = Campus.LOY.getBuilding(polygon);
                }
                ((MainActivity) getActivity()).createToast(building.getShortName());

            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                /**
                 * ONLY FOR DEMO PURPOSES
                 */
                Building building = Campus.SGW.getBuilding(marker);
                if (building == null) {
                    building = Campus.LOY.getBuilding(marker);
                }
                ((MainActivity) getActivity()).createToast(building.getShortName());
                return true;
            }
        });
    }

    /**
     * Create markers and polygons overlay for each building
     */
    private void createBuildingMarkersAndPolygonOverlay(Building building) {
        register(building);

        Polygon polygon = map.addPolygon(building.getPolygonOverlayOptions());
        polygon.setClickable(true);
        building.setPolygon(polygon);

        Marker marker = map.addMarker(building.getMarkerOptions());
        marker.setTitle(building.getShortName());
        building.setMarker(marker);
    }


    /**
     * Applying custom google map style in order to get rid of unwanted POI and other information that is not useful to our application
     */
    private void applyCustomGoogleMapsStyle() {
        map.setIndoorEnabled(false);
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

    /**
     * Applying custom google map style in order to get rid of unwanted POI and other information that is not useful to our application
     */
    void updateCampus() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCampus.getMapCoordinates(), CAMPUS_DEFAULT_ZOOM_LEVEL));
    }

    /**
     * When the camera idles, notify the observers
     */
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

    public void AlertGPS() { //GPS detection method
        Log.e("Testing Alert GPS", "Alert GPS Start");
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
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
        if (ContextCompat.checkSelfPermission(mapFragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mapFragment.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            gpsmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 2, gpsListen); //Enable Network Provider updates
            map.setMyLocationEnabled(true); //Enable Google Map layer over mapFragment
            location = gpsmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //Force Network provider due to GPS problems with different phone brands
            if (location != null) {
                double latitude = location.getLatitude(); //Getting latitude of the current location
                double longitude = location.getLongitude(); // Getting longitude of the current location
                myPosition = new LatLng(latitude, longitude); // Creating a LatLng object for the current location
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, CAMPUS_DEFAULT_ZOOM_LEVEL));//Camera Update method
            }
        }
    }

    private enum ViewType {
        INDOOR, OUTDOOR
    }
}
