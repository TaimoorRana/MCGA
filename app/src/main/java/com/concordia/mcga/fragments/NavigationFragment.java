package com.concordia.mcga.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class NavigationFragment extends Fragment implements OnMapReadyCallback, OnCameraIdleListener {

    private static final MarkerOptions LOYOLA_MARKER = new MarkerOptions().position(Campus.LOYOLA.getMapCoordinates()).title(Campus.LOYOLA.getName());
    private static final MarkerOptions SGW_MARKER = new MarkerOptions().position(Campus.SGW.getMapCoordinates()).title(Campus.SGW.getName());
    private static final float streetLevelZoom = 15f;
    Campus currentCampus = Campus.SGW;
    GoogleMap map;
    private ArrayList<Marker> buildingMarkers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button toggleButton = (Button) getView().findViewById(R.id.campusButton);
        toggleButton.setBackgroundColor(Color.parseColor("#850f02"));
        toggleButton.setTextColor(Color.WHITE);
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCampus == Campus.LOYOLA) {
                    currentCampus = Campus.SGW;
                } else {
                    currentCampus = Campus.LOYOLA;
                }
                updateCampus();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);

        applyCustomGoogleMapsStyle();
        populateCampuses();
        addCampusMarkers();
        addBuildingMarkers();

        updateCampus();
    }

    public void switchCampus(boolean loyola) {
    }

    private void addCampusMarkers() {
        map.addMarker(LOYOLA_MARKER);
        map.addMarker(SGW_MARKER);
    }

    private void addBuildingMarkers() {
        ArrayList<Building> sgwBuildings = Campus.SGW.getBuildings();
        ArrayList<Building> loyBuildings = Campus.LOYOLA.getBuildings();

        for (Building building : sgwBuildings) {
            map.addPolygon(building.getPolygonOverlayOptions());
            buildingMarkers.add(map.addMarker(building.getMarkerOptions()));
        }

        for (Building building : loyBuildings) {
            map.addPolygon(building.getPolygonOverlayOptions());
            buildingMarkers.add(map.addMarker(building.getMarkerOptions()));
        }
    }

    private void populateCampuses() {
        Campus.SGW.populateCampusWithBuildings();
        Campus.LOYOLA.populateCampusWithBuildings();
    }

    private void applyCustomGoogleMapsStyle() {
        try {
            // Customise the styling of the base map using a JSON object defined
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCampus.getMapCoordinates(), 16));
    }

    @Override
    public void onCameraIdle() {
        if (map.getCameraPosition().zoom >= streetLevelZoom) {
            for (Marker marker : buildingMarkers) {
                marker.setVisible(true);
            }
        } else {
            for (Marker marker : buildingMarkers) {
                marker.setVisible(false);
            }
        }
    }
}
