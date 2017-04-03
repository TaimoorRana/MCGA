package com.concordia.mcga.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.akexorcist.googledirection.constant.TransportMode;
import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.POISearchAdapter;
import com.concordia.mcga.helperClasses.Observer;
import com.concordia.mcga.helperClasses.OutdoorDirections;
import com.concordia.mcga.helperClasses.Subject;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.POI;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class NavigationFragment extends Fragment implements OnMapReadyCallback,
        OnCameraIdleListener, Subject, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    //Enum representing which map view is active
    private enum ViewType {
        INDOOR, OUTDOOR
    }

    private enum SearchState {
        NONE, LOCATION, DESTINATION, LOCATION_DESTINATION
    }

    //Outdoor Map
    private final float CAMPUS_DEFAULT_ZOOM_LEVEL = 16f;
    //Outdoor direction
    private OutdoorDirections outdoorDirections = new OutdoorDirections();
    private LocationListener gpsListen = new LocationListener() {
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
    private Map<String, Object> multiBuildingMap = new HashMap<>();
    //State
    private ViewType viewType;
    private Campus currentCampus = Campus.SGW;
    private boolean indoorMapVisible = false;
    private boolean outdoorMapVisible = false;
    private boolean buildingInfoShown = false;



    //Fragments
    private RelativeLayout parentLayout;
    private SupportMapFragment mapFragment;
    private TransportButtonFragment transportButtonFragment;
    private IndoorMapFragment indoorMapFragment;
    private BottomSheetDirectionsFragment directionsFragment;
    private BottomSheetBuildingInfoFragment buildingInfoFragment;


    private View bottomSheetView;

    //View Components
    private View rootView;
    private View toolbarView;
    private Button campusButton;
    private View navToolBar;

    private Button viewSwitchButton;
    private FloatingActionButton mapCenterButton;
    //GPS attributes
    private LocationManager gpsmanager; //LocationManager instance to check gps activity
    // Search components
    private SearchView search;
    private POISearchAdapter poiSearchAdapter;
    private ExpandableListView searchList;
    private Dialog searchDialog;
    private POI location;
    private POI destination;
    private SearchState searchState;

    private boolean outdoors = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentLayout = (RelativeLayout) inflater.inflate(R.layout.nav_main_fragment, container, false);
        rootView = parentLayout.findViewById(R.id.navigationMain);
        toolbarView = parentLayout.findViewById(R.id.nav_toolbar);
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
                if (!gpsmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if(alertGPS(mapFragment.getActivity())){
                        Log.d("Testing AlertGPS Launch", "Finished AlertGPS");
                    }
                }
                if (viewType == ViewType.INDOOR) {
                    viewType = ViewType.OUTDOOR;
                    getChildFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
                    getChildFragmentManager().beginTransaction().show(transportButtonFragment).commit(); //To be removed after Outside transportation Google API incorporation
                    campusButton.setVisibility(View.VISIBLE);
                    viewSwitchButton.setText("GO INDOORS");
                }
                onClose();
                //Camera Movement
                camMove(locateMe(map, mapFragment.getActivity(), gpsmanager, gpsListen));
            }
        });





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
                    showBuildingInfoFragment(false);
                }
            }
        });





        //Set initial view type
        viewType = ViewType.OUTDOOR;

        //Hide Fragments

        AppCompatImageButton locationCancelButton;
        locationCancelButton = (AppCompatImageButton)toolbarView.findViewById(
                R.id.search_location_button);
        locationCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                location = null;
                if (destination == null) {
                    searchState = SearchState.NONE;
                } else {
                    searchState = SearchState.DESTINATION;
                }
                updateSearchUI();
            }
        });

        AppCompatImageButton destinationCancelButton;
        destinationCancelButton = (AppCompatImageButton)toolbarView.findViewById(
                R.id.search_destination_button);
        destinationCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = null;
                if (location == null) {
                    searchState = SearchState.NONE;
                } else {
                    searchState = SearchState.LOCATION;
                }
                updateSearchUI();
            }
        });

        //Hide Indoor Fragment

        getChildFragmentManager().beginTransaction().hide(indoorMapFragment).commit();

        //Hide Fragments
        showTransportButton(true);



        // Set the building information bottomsheet to true
        // When the app starts
        // Set the directions one to false
        showBuildingInfoFragment(false);
        showDirectionsFragment(false);

        //Hide Fragments
        showTransportButton(true);

        setupSearchAttributes();
        setupSearchList();

        //Set initial view type
        viewType = ViewType.OUTDOOR;

        // Display no location/destination by default
        searchState = SearchState.NONE;
        updateSearchUI();

        buildingInfoFragment.updateBottomSheet();

        return parentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gpsmanager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

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

        //Thread
        Thread thread = new Thread(){
            @Override
            public void run(){
                while(true) {
                    try {
                        sleep(20);
                        int y = 0;
                        try {
                            if (outdoors) {
                                y = buildingInfoFragment.getTop();

                            } else {
                                y = directionsFragment.getTop();
                            }
                        } catch (Exception e) {
                        }
                        if ((buildingInfoShown)) {
                            mapCenterButton.setY(y - parentLayout.getHeight() + toolbarView.getHeight() + mapCenterButton.getHeight());
                        }
                        else if (!outdoors){
                            mapCenterButton.setY(y - parentLayout.getHeight() + toolbarView.getHeight() + mapCenterButton.getHeight());
                        }
                        else
                        {
                            mapCenterButton.setY(parentLayout.getHeight() - toolbarView.getHeight() - mapCenterButton.getHeight());
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
        outdoorDirections.setSelectedTransportMode(TransportMode.DRIVING);

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
            outdoors = true;
            buildingInfoShown = true;
        } else {
            getChildFragmentManager().beginTransaction().hide(buildingInfoFragment).commit();
            outdoors = false;
            buildingInfoShown = false;
        }
    }

    /**
     * Shows or hides the directions bottom sheet fragment
     * @param isVisible
     */
    private void showDirectionsFragment(boolean isVisible) {
        if (isVisible) {
            getChildFragmentManager().beginTransaction().show(directionsFragment).commit();
            outdoors = false;
        } else {
            getChildFragmentManager().beginTransaction().hide(directionsFragment).commit();
            outdoors = true;
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
        buildingInfoFragment.setBuildingName(name);
        buildingInfoFragment.setBuildingInformation(name, "add", "7:00", "23:00");
        buildingInfoFragment.updateBottomSheet();
        showBuildingInfoFragment(true);


        setNavigationPOI((Building) multiBuildingMap.get(polygon.getId()));
        parentLayout.postInvalidate();
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
        buildingInfoFragment.setBuildingName(name);
        buildingInfoFragment.setBuildingInformation(name, "add", "7:00", "23:00");
        buildingInfoFragment.updateBottomSheet();
        showBuildingInfoFragment(true);
        parentLayout.invalidate();
        setNavigationPOI((Building) multiBuildingMap.get(marker.getId()));

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
                setBottomSheetContent(polygon);
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
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

    /**
     * Build alert dialog on fragment's activity
     * Shown iff (gpsmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) is false
     * Prompt user to enable the GPS
     * If user presses "Enable GPS",  minimize application and prompt user to GPS Android window
     */

    public static boolean alertGPS(final Activity activity) { //GPS detection method
        AlertDialog.Builder build = new AlertDialog.Builder(activity);
        build
                .setTitle("GPS Detection Services")
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activity.startActivity(i);
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
        return true;
    }

    /**
     *
     * @param map object of the navigation fragment's
     * @param activity acquired with mapfragment.getActivity()
     * @param gpsmanager object of LocationManager (from android and not google maps)
     * @param gpsListen object from interface LocationListener (from android and not google maps)
     * @return true if the operation is a success (if permission is acquired && a location was succesfully retrieved
     *
     * Method run to acquire user's location on a map, display it and update camera to it
     * if permission NOT found, request the permission
     * Enable GPS provider updates on locationmanager (requires permission check)
     * Enable Google Map layer over map object to display user's location on the map
     * Instantiate location with last known location of Network provider
     * if no location found, return false, otherwise, map centers on user's location
     *
     */

    public LatLng locateMe(GoogleMap map, Activity activity, LocationManager gpsmanager, LocationListener gpsListen) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        } else {
            gpsmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 2, gpsListen); //Enable Network Provider updates
            map.setMyLocationEnabled(true); //Enable Google Map layer over mapFragment
            Location location = gpsmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //Force Network provider due to GPS problems with different phone brands
            if (location != null) {
                double latitude = location.getLatitude(); //Getting latitude of the current location
                double longitude = location.getLongitude(); // Getting longitude of the current location
                LatLng myPosition = new LatLng(latitude, longitude); // Creating a LatLng object for the current location
                return myPosition;
            }
            else
                return null;
        }
    }

    public void camMove(LatLng MyPos){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MyPos, 16f));//Camera Update method
    }


    // Bug in API, onClose doesn't get called. Use this manually
    @Override
    public boolean onClose() {
        search.setQuery("", false);
        search.clearFocus();
        rootView.requestFocus();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        poiSearchAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        poiSearchAdapter.filterData(newText);
        expandAll();
        return false;
    }

    /**
     * Updates the UI elements associated with the search state.
     * If no location or destination has been selected, hide the elements.
     * If a location has been specified, hide the destination element but update the location label.
     * If a destination has been specified, hide the location element but update destination label.
     * If both have been specified, show everything and update both labels.
     */
    private void updateSearchUI() {

        LinearLayoutCompat locationLayout = (LinearLayoutCompat) toolbarView.findViewById(R.id.search_location);
        LinearLayoutCompat destinationLayout = (LinearLayoutCompat) toolbarView.findViewById(R.id.search_destination);


        if (location != null) {
            AppCompatTextView locationText = (AppCompatTextView)
                    toolbarView.findViewById(R.id.search_location_text);
            setDisplayName(location, locationText);
            outdoorDirections.setOrigin(location.getMapCoordinates());
        }else{
            outdoorDirections.setOrigin(null);
        }
        if (destination != null) {
            AppCompatTextView destinationText = (AppCompatTextView)
                    toolbarView.findViewById(R.id.search_destination_text);
            setDisplayName(destination, destinationText);
            outdoorDirections.setDestination(destination.getMapCoordinates());
        }else{
            outdoorDirections.setDestination(null);
        }

        outdoorDirections.deleteDirection();
        if (location != null && destination != null) {
            outdoorDirections.requestDirections();
        }


        if (searchState == SearchState.NONE) {
            locationLayout.setVisibility(View.GONE);
            destinationLayout.setVisibility(View.GONE);
            search.setQueryHint("Enter location...");
            search.setVisibility(View.VISIBLE);
        } else if (searchState == SearchState.LOCATION) {
            locationLayout.setVisibility(View.VISIBLE);
            destinationLayout.setVisibility(View.GONE);
            search.setQueryHint("Enter destination...");
            search.setVisibility(View.VISIBLE);
        } else if (searchState == SearchState.DESTINATION) {
            locationLayout.setVisibility(View.GONE);
            destinationLayout.setVisibility(View.VISIBLE);
            search.setQueryHint("Enter location...");
            search.setVisibility(View.VISIBLE);
        } else { // searchState == SearchState.LOCATION_DESTINATION
            locationLayout.setVisibility(View.VISIBLE);
            destinationLayout.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
            search.setQueryHint("Search...");
        }
    }




    /**
     * Expands all the view result groups (showing the POIs under the campus search results)
     */
    private void expandAll() {
        if (poiSearchAdapter.getGroupCount() == 0) {
            searchDialog.dismiss();
        } else {
            searchDialog.show();
            for (int i = 0; i < poiSearchAdapter.getGroupCount(); i++) {
                if (i != POISearchAdapter.MY_LOCATION_GROUP_POSITION) {
                    searchList.expandGroup(i);
                }
            }
        }
    }

    /**
     * Sets the display name for a label from a POI.
     * If the POI is a building, use the short name instead of the long one ("H" vs. "Hall")
     * Else, use the full POI name
     * @param poi Point of interest to display on the text view
     * @param textView Text view to set text on
     */
    private void setDisplayName(POI poi, AppCompatTextView textView) {
        if (poi instanceof Building) {
            textView.setText(((Building) poi).getShortName());
        } else {
            textView.setText(poi.getName());
        }
    }

    /**
     * Constructor helper function to set up the search functionality of the view
     */
    private void setupSearchAttributes() {
        //Search
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
                Context.SEARCH_SERVICE);
        search = (SearchView) parentLayout.findViewById(R.id.navigation_search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        search.setQueryHint("Enter destination");

        //Custom search dialog
        searchDialog = new Dialog(getActivity());
        searchDialog.setCanceledOnTouchOutside(true);
        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.setContentView(R.layout.search_dialog);
        final Window window = searchDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT);

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Set dialog window offset and height
                        WindowManager.LayoutParams wmlp = window.getAttributes();
                        int xy[] = new int[2];
                        parentLayout.findViewById(R.id.navigation_search).getLocationOnScreen(xy);
                        wmlp.y = xy[1] + 20;

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);
                        wmlp.height = (r.bottom - r.top) - wmlp.y;
                        window.setAttributes(wmlp);
                    }
                }
        );
    }

    /**
     * Constructor helper function to set up the list and listener for the navigation search
     */
    private void setupSearchList() {
        searchList = (ExpandableListView) searchDialog.findViewById(R.id.expandableList);
        poiSearchAdapter = new POISearchAdapter(getActivity(), Campus.SGW, Campus.LOY);
        searchList.setAdapter(poiSearchAdapter);

        searchList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                POI dest = (POI)poiSearchAdapter.getChild(groupPosition, childPosition);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(dest.getMapCoordinates(),
                        CAMPUS_DEFAULT_ZOOM_LEVEL));

                setNavigationPOI(dest);
                onClose();
                return true;
            }
        });

        searchList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == POISearchAdapter.MY_LOCATION_GROUP_POSITION) {
                    POI myPOI = new POI(locateMe(map, mapFragment.getActivity(), gpsmanager, gpsListen), getString(R.string.my_location_string));
                    setNavigationPOI(myPOI);
                    onClose();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Sets the navigation state and location/destination for internal use
     * @param dest The {@link POI} location to be added to the search state
     * @return Whether the state was updated or not
     */
    private boolean setNavigationPOI(POI dest) {
        if (searchState == SearchState.NONE) {
            location = dest;
            searchState = SearchState.LOCATION;
        } else if (searchState == SearchState.DESTINATION) {
            location = dest;
            searchState = SearchState.LOCATION_DESTINATION;
        } else if (searchState == SearchState.LOCATION) {
            destination = dest;
            searchState = SearchState.LOCATION_DESTINATION;
        } else { // if searchState == SearchState.LOCATION_DESTINATION
            return false;
        }
        updateSearchUI();
        return true;
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

