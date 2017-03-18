package com.concordia.mcga.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class NavigationFragment extends Fragment implements OnMapReadyCallback, OnCameraIdleListener, Subject {

    //Enum representing which map view is active
    private enum ViewType {
        INDOOR, OUTDOOR
    }

    //Outdoor Map
    private final float CAMPUS_DEFAULT_ZOOM_LEVEL = 16f;
    private GoogleMap map;
    private List<Observer> observerList = new ArrayList<>();

    //State
    private ViewType viewType;
    private Campus currentCampus = Campus.SGW;

    private boolean indoorMapVisible = false;
    private boolean outdoorMapVisible = false;


    //Fragments
    private LinearLayoutCompat parentLayout;
    private SupportMapFragment mapFragment;
    private TransportButtonFragment transportButtonFragment;
    private IndoorMapFragment indoorMapFragment;

    private BottomSheetDirectionsFragment directionsFragment;
    private BottomSheetBuildingInfoFragment buildingInfoFragment;


    //View Components
    private Button campusButton;
    private Button viewSwitchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentLayout = (LinearLayoutCompat) inflater.inflate(R.layout.nav_main_fragment, container, false);

        //Init Fragments
        transportButtonFragment = (TransportButtonFragment) getChildFragmentManager().findFragmentById(R.id.transportButton);
        indoorMapFragment = (IndoorMapFragment) getChildFragmentManager().findFragmentById(R.id.indoormap);
        directionsFragment = (BottomSheetDirectionsFragment) getChildFragmentManager().findFragmentById(R.id.directionsFragment);
        buildingInfoFragment = (BottomSheetBuildingInfoFragment) getChildFragmentManager().findFragmentById(R.id.buildingInfoFragment);

        //Init View Components
        campusButton = (Button) parentLayout.findViewById(R.id.campusButton);
        viewSwitchButton = (Button) parentLayout.findViewById(R.id.viewSwitchButton);
        viewSwitchButton.setText("GO INDOORS");
        viewSwitchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType == ViewType.OUTDOOR) {
                    showIndoorMap();
                    campusButton.setVisibility(View.GONE);
                    viewSwitchButton.setText("GO OUTDOORS");
                    showDirectionsFragment(true);
                    showBuildingInfoFragment(false);
                } else {
                    showOutdoorMap();
                    campusButton.setVisibility(View.VISIBLE);
                    viewSwitchButton.setText("GO INDOORS");
                    showDirectionsFragment(false);
                    showBuildingInfoFragment(true);
                }
            }
        });


        //Set initial view type
        viewType = ViewType.OUTDOOR;
        
        //Hide Fragments
        getChildFragmentManager().beginTransaction().hide(indoorMapFragment).commit();

        //Hide Fragments
        showTransportButton(true);


        // Set the building information bottomsheet to true
        // When the app starts
        // Set the directions one to false
        showBuildingInfoFragment(true);
        showDirectionsFragment(false);

        return parentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
        showOutdoorMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);

        //Settings
        map.getUiSettings().setMapToolbarEnabled(false);

        //Map Customization
        applyCustomGoogleMapsStyle();
        Campus.populateCampusesWithBuildings();
        addBuildingMarkersAndPolygons();

        updateCampus();
    }

    /**

     * Shows or hides the bottom sheet building information fragment
     * @param isVisible
     */
    private void showBuildingInfoFragment(boolean isVisible) {
        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(buildingInfoFragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().hide(buildingInfoFragment).commit();
        }
    }

    /**
     * Shows or hides the directions bottom sheet fragment
     * @param isVisible
     */
    private void showDirectionsFragment(boolean isVisible) {
        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(directionsFragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().hide(directionsFragment).commit();
        }
    }
    /*
     * Shows or hides the indoor map, will hide the outdoormap if visible
     */
    public void showIndoorMap() {
        outdoorMapVisible = false;
        indoorMapVisible = true;
        viewType = ViewType.INDOOR;
        getChildFragmentManager().beginTransaction().show(indoorMapFragment).hide(mapFragment).commit();
    }

    /**
     * Shows or hides the outdoor map, will hide the indoormap if visible
     */
    public void showOutdoorMap() {
        outdoorMapVisible = true;
        indoorMapVisible = false;
        viewType = ViewType.OUTDOOR;
        getChildFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
    }

    /**
     * Shows or hides the transport button
     * @param isVisible
     */
    public void showTransportButton(boolean isVisible) {
        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(transportButtonFragment).commit();
        } else {
            getChildFragmentManager().beginTransaction().hide(transportButtonFragment).commit();

        }
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
                if(building == null){
                    building = Campus.LOY.getBuilding(polygon);
                }
                getActivity().getApplicationContext().createToast(building.getShortName());
                String buildingName = building.getShortName();
                buildingInfoFragment.setBuildingInformation(buildingName, "add", "7:00", "23:00");
                buildingInfoFragment.clear();
                // TEMPORARY
                if (buildingName.equals("H")){
                    buildingInfoFragment.displayHBuildingAssociations();
                }
                else if (buildingName.equals("JM")){
                    buildingInfoFragment.displayMBBuildingAssociations();
                }
                buildingInfoFragment.collapse();

            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                /**
                 * ONLY FOR DEMO PURPOSES
                 */
                Building building = Campus.SGW.getBuilding(marker);
                if(building == null){
                    building = Campus.LOY.getBuilding(marker);
                }
                getActivity().getApplicationContext().createToast(building.getShortName());
                String buildingName = building.getShortName();
                buildingInfoFragment.setBuildingInformation(buildingName, "address", "7:00", "23:00");
                buildingInfoFragment.clear();
                // TEMPORARY
                if (buildingName.equals("H")){
                    buildingInfoFragment.displayHBuildingAssociations();
                }
                else if (buildingName.equals("JM")){
                    buildingInfoFragment.displayMBBuildingAssociations();
                }
                buildingInfoFragment.collapse();
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

    //Getters
    public boolean isIndoorMapVisible() {
        return indoorMapVisible;
    }




    public boolean isOutdoorMapVisible() {
        return outdoorMapVisible;
    }

    public ViewType getViewType() {
        return viewType;
    }
}

