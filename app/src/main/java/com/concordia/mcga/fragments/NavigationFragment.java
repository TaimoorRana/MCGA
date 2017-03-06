package com.concordia.mcga.fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.POISearchAdapter;
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

public class NavigationFragment extends Fragment implements OnMapReadyCallback, OnCameraIdleListener, Subject, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

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
    private View rootView;
    private Button campusButton;
    private Button viewSwitchButton;

    // Search components
    private SearchView search;
    private POISearchAdapter poiSearchAdapter;
    private ExpandableListView searchList;
    private Dialog searchDialog;

    private enum ViewType {
        INDOOR, OUTDOOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentLayout = (LinearLayoutCompat) inflater.inflate(R.layout.nav_main_fragment, container, false);
        rootView = parentLayout.findViewById(R.id.navigationMain);

        //Init Fragments
        transportButtonFragment = (TransportButtonFragment) getChildFragmentManager().findFragmentById(R.id.transportButton);
        indoorMapFragment = (IndoorMapFragment) getChildFragmentManager().findFragmentById(R.id.indoormap);

        //Init View Components
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

        setupSearchAttributes();
        setupSearchList();

        return parentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
     * Expands all the view result groups (showing the POIs under the campus search results)
     */
    private void expandAll() {
        if (poiSearchAdapter.getGroupCount() == 0) {
            searchDialog.dismiss();
        } else {
            searchDialog.show();
            for (int i = 0; i < poiSearchAdapter.getGroupCount(); i++) {
                searchList.expandGroup(i);
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /**
     * Constructor helper function to set up the search functionality of the view
     */
    private void setupSearchAttributes() {
        //Search
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
                Context.SEARCH_SERVICE);
        search = (SearchView) parentLayout.findViewById(R.id.navigationSearch);
        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);


        //Custom search dialog
        searchDialog = new Dialog(getActivity());
        searchDialog.setCanceledOnTouchOutside(true);
        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.setContentView(R.layout.search_dialog);
        Window window = searchDialog.getWindow();
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

        // Set dialog window offset and height
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.y = 200;
        wmlp.height = 450;
        window.setAttributes(wmlp);
    }

    /**
     * Constructor helper function to set up the list and listener for the navigation search
     */
    private void setupSearchList() {
        searchList = (ExpandableListView) searchDialog.findViewById(R.id.expandableList);
        poiSearchAdapter = new POISearchAdapter(getActivity());
        searchList.setAdapter(poiSearchAdapter);

        searchList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Building dest = (Building)poiSearchAdapter.getChild(groupPosition, childPosition);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(dest.getMapCoordinates(),
                        CAMPUS_DEFAULT_ZOOM_LEVEL));
                onClose();
                //Toast.makeText(getActivity(), dest.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
