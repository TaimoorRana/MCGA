package com.concordia.mcga.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.activities.StudentSpotActivity;
import com.concordia.mcga.adapters.POISearchAdapter;
import com.concordia.mcga.helperClasses.Observer;
import com.concordia.mcga.helperClasses.OutdoorDirections;
import com.concordia.mcga.helperClasses.Subject;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.POI;
import com.concordia.mcga.models.StudentSpot;
import com.concordia.mcga.models.Room;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class NavigationFragment extends Fragment implements OnMapReadyCallback,
        OnCameraIdleListener, Subject {
    //Enum representing which map view is active
    public enum ViewType {
        INDOOR, OUTDOOR
    }

    //Outdoor Map
    private final float CAMPUS_DEFAULT_ZOOM_LEVEL = 16f;
    //Outdoor direction
    private OutdoorDirections outdoorDirections = new OutdoorDirections();
    private GoogleMap map;
    private List<Observer> observerList = new ArrayList<>();
    private Map<String, Object> multiBuildingMap = new HashMap<>();
    //State
    private ViewType viewType;
    private Campus currentCampus = Campus.SGW;
    private boolean transportButtonVisible = false;

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
    private FloatingActionButton mapCenterButton;
    private Button nextDirection, previousDirection;
    //State
    private Building lastClickedBuilding;


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
        mapCenterButton = (FloatingActionButton) parentLayout.findViewById(R.id.mapCenterButton);
        mapCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType == ViewType.INDOOR) {
                    showOutdoorMap();
                }
                ((MainActivity)getActivity()).onClose();
                //Camera Movement
                LatLng location = ((MainActivity) getActivity()).getGpsManager().getLocation();
                if (location != null) {
                    camMove(location);
                    toggleMapLocation(true);
                }
            }
        });

        campusButton = (Button) parentLayout.findViewById(R.id.campusButton);
        viewSwitchButton = (Button) parentLayout.findViewById(R.id.viewSwitchButton);

        campusButton.setVisibility(View.VISIBLE);
        viewSwitchButton.setText("GO INDOORS");

        viewSwitchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType == ViewType.OUTDOOR) {
                    showIndoorMap(lastClickedBuilding);
                } else {
                    showOutdoorMap();
                }
            }
        });

        //Set initial view type
        viewType = ViewType.OUTDOOR;

        //Hide Fragments
        showTransportButton(true);

        // Set the building information bottomsheet to true
        // When the app starts
        // Set the directions one to false
        showBuildingInfoFragment(true);
        showDirectionsFragment(false);

        //Requests focus on creation. Prevents text views from being auto selected on launch.
        parentLayout.requestFocus();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.SPOT_REQUEST_CODE && resultCode == RESULT_OK) {
            String json = data.getExtras().getString(StudentSpotActivity.SPOT_IDENTIFIER);
            StudentSpot spot = new Gson().fromJson(json, StudentSpot.class);
            ((MainActivity)getActivity()).setNavigationPOI(spot, true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);

        //Settings
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setIndoorEnabled(false);

        //initializing outdoor directions
        transportButtonFragment.setOutdoorDirections(outdoorDirections);
        outdoorDirections.setContext(getActivity().getApplicationContext());
        outdoorDirections.setServerKey(getResources().getString(R.string.google_maps_key));
        outdoorDirections.setMap(map);

        //Map Customization
        applyCustomGoogleMapsStyle();
        Campus.populateCampusesWithBuildings();
        addBuildingMarkersAndPolygons();

        updateCampus();
    }

    public void onRoomSearch(Room room) {
        if (viewType == ViewType.OUTDOOR) {
            showIndoorMap(room.getFloor().getBuilding());
        }

        indoorMapFragment.onRoomSearch(room);
    }

    /**
     * Shows or hides the indoor map, will hide the outdoormap and transport button if visible
     *
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
    public void showDirectionsFragment(boolean isVisible) {
        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(directionsFragment).commit();

        } else {
            getChildFragmentManager().beginTransaction().hide(directionsFragment).commit();
        }
    }
    /*
     * Shows or hides the indoor map, will hide the outdoormap if visible
     */
    public void showIndoorMap(Building building) {
        if (building.getRooms().size() > 0) {
            viewType = ViewType.INDOOR;

            showTransportButton(false);
            campusButton.setVisibility(View.GONE);
            showBuildingInfoFragment(false);

            getChildFragmentManager().beginTransaction().show(indoorMapFragment).hide(mapFragment).commit();
            viewSwitchButton.setText("GO OUTDOORS");

            indoorMapFragment.initializeBuilding(building);
            indoorMapFragment.drawCurrentWalkablePath();
        }
    }


    /**
     * Shows or hides the outdoor map, will hide the indoormap if visible
     */
    public void showOutdoorMap() {
        viewType = ViewType.OUTDOOR;

        showTransportButton(true);
        campusButton.setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();

        viewSwitchButton.setText("GO INDOORS");
        showBuildingInfoFragment(true);
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
        transportButtonVisible = isVisible;
    }

    /**
     *
     * @param polygon
     */
    private void setBottomSheetContent(Polygon polygon){
        /**
         * ONLY FOR DEMO PURPOSES
         */
        Building building = Campus.getBuilding(polygon);
        if (building == null) {
            building = Campus.getBuilding(polygon);
        }
        ((MainActivity) getActivity()).createToast(building.getShortName());
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

        ((MainActivity)getActivity()).setNavigationPOI((Building)
                multiBuildingMap.get(polygon.getId()), false);
    }

    /**
     *
     * @param marker
     */
    private void setBottomSheetContent(Marker marker){
        /**
         * ONLY FOR DEMO PURPOSES
         */
        Building building = Campus.getBuilding(marker);
        if(building == null){
            building = Campus.getBuilding(marker);
        }
        ((MainActivity) getActivity()).createToast(building.getShortName());
        String buildingName = building.getShortName();
        buildingInfoFragment.setBuildingInformation(buildingName, "address", "7:00", "23:00");
        buildingInfoFragment.clear();
        // TEMPORARY
        if (buildingName.equals("H")) {
            buildingInfoFragment.displayHBuildingAssociations();
        } else if (buildingName.equals("JM")) {
            buildingInfoFragment.displayMBBuildingAssociations();
        }
        buildingInfoFragment.collapse();

        ((MainActivity)getActivity()).setNavigationPOI((Building) multiBuildingMap.get(marker.getId()), false);
    }

    /**
     * add markers and polygons overlay for each building
     */
    private void addBuildingMarkersAndPolygons() {
        List<Building> allBuildings = new ArrayList<>();
        allBuildings.addAll(Campus.SGW.getBuildings());
        allBuildings.addAll(Campus.LOY.getBuildings());

        for (Building building : allBuildings) {
            createBuildingMarkersAndPolygonOverlay(building);
        }

        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                lastClickedBuilding = Campus.getBuilding(polygon);
                setBottomSheetContent(polygon);
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                lastClickedBuilding = Campus.getBuilding(marker);
                setBottomSheetContent(marker);
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

        // Add the building to a map to be retrieved later
        multiBuildingMap.put(polygon.getId(), building);

        Marker marker = map.addMarker(building.getMarkerOptions());
        multiBuildingMap.put(marker.getId(), building);

        building.setPolygon(polygon);
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

    public void clearAllPaths() {
        clearOutdoorPath();
        clearIndoorPath();
    }

    public void clearOutdoorPath() {
        if (outdoorDirections != null) {
            outdoorDirections.deleteDirection();
        }
    }

    public void clearIndoorPath() {
        if (indoorMapFragment != null) {
            indoorMapFragment.clearWalkablePaths();
        }
    }

    public void generateOutdoorPath(POI start, POI dest) {
        outdoorDirections.setOrigin(start.getMapCoordinates());
        outdoorDirections.setDestination(dest.getMapCoordinates());
        outdoorDirections.requestDirections();
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

    public void camMove(LatLng MyPos){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MyPos, 16f));//Camera Update method
    }

    public void toggleMapLocation(boolean active) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(active);
        } else {
            map.setMyLocationEnabled(false);
        }
    }

    public String getTransportationType() {
        return transportButtonFragment.getTransportType();
    }

    public boolean isTransportButtonVisible() { return transportButtonVisible; }

    public ViewType getViewType() {
        return viewType;
    }

    public OutdoorDirections getOutdoorDirections() {
        return outdoorDirections;
    }

    public IndoorMapFragment getIndoorMapFragment() {
        return indoorMapFragment;
    }

    public GoogleMap getMap() {
        return map;
    }

    public BottomSheetDirectionsFragment getDirectionsFragment(){
        return directionsFragment;
    }
}

