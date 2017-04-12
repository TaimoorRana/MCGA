package com.concordia.mcga.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.RelativeLayout;

import android.widget.ToggleButton;


import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.activities.StudentSpotActivity;
import com.concordia.mcga.helperClasses.Observer;
import com.concordia.mcga.utilities.pathfinding.OutdoorDirections;
import com.concordia.mcga.helperClasses.Subject;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
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
import com.google.maps.android.SphericalUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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

    private boolean buildingInfoShown = false;
    private boolean transportButtonVisible = false;
    private ToggleButton toggleButton;


    //Fragments
    private RelativeLayout parentLayout;
    private SupportMapFragment mapFragment;
    private TransportButtonFragment transportButtonFragment;
    private IndoorMapFragment indoorMapFragment;
    private BottomSheetDirectionsFragment directionsFragment;
    private BottomSheetBuildingInfoFragment buildingInfoFragment;

    //View Components
    private Button campusButton;
    private View navToolBar;

    private boolean directionMode = false;

    private Button viewSwitchButton;
    private FloatingActionButton directionsButton;
    private FloatingActionButton mapCenterButton;

    private POI bottomSheetBuilding;
    private boolean outdoors = true;

    private Button nextDirection, previousDirection;

    //State
    private Building lastClickedBuilding;

    public final static int FLAG_DIRECTIONS = 0, FLAG_INFO = 1, FLAG_NO_DISPLAY = -1;
    private int building_flag = FLAG_NO_DISPLAY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentLayout = (RelativeLayout) inflater.inflate(R.layout.nav_main_fragment, container, false);
        navToolBar = parentLayout.findViewById(R.id.toolbar);


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
                //Close Search Options
                ((MainActivity)getActivity()).setSearchState(MainActivity.SearchState.NONE);
                ((MainActivity)getActivity()).updateSearchUI();
                ((MainActivity)getActivity()).onClose();
                //Close slider
                showBuildingInfoFragment(false);
                showDirectionsFragment(false);
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
        directionsButton = (FloatingActionButton) parentLayout.findViewById(R.id.directionsButton);
        directionsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlag(FLAG_NO_DISPLAY);
                if (bottomSheetBuilding != null) {
                    ((MainActivity) getActivity()).setNavigationPOI(bottomSheetBuilding, false);
                }
            }
        });

        campusButton.setVisibility(View.VISIBLE);
        viewSwitchButton.setText("GO INDOORS");

        viewSwitchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType == ViewType.OUTDOOR) {
                    showIndoorMap(lastClickedBuilding);
                    setFlag(FLAG_NO_DISPLAY);
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
        setFlag(FLAG_NO_DISPLAY);

        buildingInfoFragment.updateBottomSheet();
        parentLayout.requestFocus();

        return parentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toggleButton = (ToggleButton) getView().findViewById(R.id.campusButton);
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
        relativeButtonThread();
    }

    /**
     * Positions the Locate Me button as well as the
     * Directions button relative to the bottomsheet at all times
     */
    private void relativeButtonThread(){
        //Thread
        Thread thread = new Thread(){
            @Override
            public void run(){
                while(true) {
                    try {
                        sleep(20);
                        int y = 0;
                        if ((building_flag == FLAG_INFO)) {
                            directionsButton.setVisibility(View.VISIBLE);
                            y = buildingInfoFragment.getTop();

                            directionsButton.setY(y - 2 * mapCenterButton.getHeight() + mapCenterButton.getHeight() /2 );
                            mapCenterButton.setY(y - 2 * mapCenterButton.getHeight() + mapCenterButton.getHeight() /2 );
                        }
                        else if (building_flag == FLAG_DIRECTIONS){
                            //directionsButton.setVisibility(View.INVISIBLE);
                            y = directionsFragment.getTop();
                            mapCenterButton.setY(y - mapCenterButton.getHeight());
                            directionsButton.setY(y -  mapCenterButton.getHeight());

                        }
                        else if (building_flag == FLAG_NO_DISPLAY)
                        {
                            //directionsButton.setVisibility(View.INVISIBLE);
                            mapCenterButton.setY( parentLayout.getHeight() - mapCenterButton.getHeight());
                            directionsButton.setY( parentLayout.getHeight() - mapCenterButton.getHeight());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
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
        currentCampus=closestCampus(distanceBetween(((MainActivity) getActivity()).getGpsManager().getLocation(),Campus.SGW.getMapCoordinates()),distanceBetween(((MainActivity) getActivity()).getGpsManager().getLocation(),Campus.LOY.getMapCoordinates()));
        updateCampus();
    }

    /**
     *
     * @param point1: First LatLng to compare
     * @param point2: Second LatLng to compare
     * @return a double calculated by the different in LatLng by SphericalUtil
     * This method calculates the distance between two LatLng variables.
     * To be used with method "closestCampus".
     */
    public static Double distanceBetween(LatLng point1, LatLng point2){
        if(point1==null||point2 == null){
            return 0.0;
        }
        else{
            return SphericalUtil.computeDistanceBetween(point1, point2);
        }
    }

    /**
     *
     * @param distance1: Distance returned from myLocation to SGW Campus
     * @param distance2: Distance returned from myLocation to LOY Campus
     * @return: Returns the campus it's closest to
     * This method is used to return a campus depending on the user's current location by comparing the distance between the user and both campus's
     * location using the previous "distanceBetween" method
     */
    public Campus closestCampus(double distance1, double distance2){
        if(distance1<=distance2){
            toggleButton.setChecked(false);
            return Campus.SGW;
        }
        else{
            toggleButton.setChecked(true);
            return Campus.LOY;
        }
    }

    public void onRoomSearch(Room room) {
        if (viewType == ViewType.OUTDOOR) {
            showIndoorMap(room.getFloor().getBuilding());
        }
        indoorMapFragment.onRoomSearch(room);
    }

    public void setFlag(int flag){
        building_flag = flag;
        showBottomSheet();
    }

    private void showBottomSheet(){
        if (building_flag == FLAG_DIRECTIONS){
            Log.d("bottomsheet-", "DIRECTIONS");
            showBuildingInfoFragment(false);
            showDirectionsFragment(true);
        }
        else if(building_flag == FLAG_INFO){
            Log.d("bottomsheet-", "INFO");
            showBuildingInfoFragment(true);
            showDirectionsFragment(false);
            buildingInfoFragment.collapse();
        }
        else if(building_flag == FLAG_NO_DISPLAY){
            Log.d("bottomsheet-", "NO_DISPLAY");
            showDirectionsFragment(false);
            showBuildingInfoFragment(false);
        }

    }

    /**
     * Shows or hides the indoor map, will hide the outdoormap and transport button if visible
     *
     * @param isVisible
     */
    private void showBuildingInfoFragment(boolean isVisible) {
        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(buildingInfoFragment).commitAllowingStateLoss();;
            buildingInfoFragment.collapse();
            outdoors = true;
            // this boolean exist because it is not in the directions fragment
            // Creates 4 combinations between buildingInfoShown and outdoors
            buildingInfoShown = true;
        } else {
            getChildFragmentManager().beginTransaction().hide(buildingInfoFragment).commitAllowingStateLoss();;
            outdoors = false;
            buildingInfoShown = false;
        }
    }

    /**
     * Shows or hides the directions bottom sheet fragment
     * @param isVisible
     */
    public void showDirectionsFragment(boolean isVisible) {
        directionMode = isVisible;

        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(directionsFragment).commitAllowingStateLoss();
            outdoors = false;
        } else {
            getChildFragmentManager().beginTransaction().hide(directionsFragment).commitAllowingStateLoss();
            outdoors = true;
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
     * Updates Bottomsheet displaying information about a building
     * once the user taps on a building
     * @param buildingName
     */
    private void updateBuildingInfoSheet(String buildingName){
        buildingInfoFragment.setBuildingName(buildingName);
        buildingInfoFragment.setBuildingInformation(buildingName, "add", "7:00", "23:00");
        buildingInfoFragment.updateBottomSheet();
        setFlag(FLAG_INFO);
        parentLayout.postInvalidate();
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

        String name = building.getShortName();
        updateBuildingInfoSheet(name);
        bottomSheetBuilding = (Building)multiBuildingMap.get(polygon.getId());

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

        String name = building.getShortName();
        updateBuildingInfoSheet(name);
        bottomSheetBuilding = (Building)multiBuildingMap.get(marker.getId());

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

    public TransportButtonFragment getTransportButtonFragment() {
        return transportButtonFragment;
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

