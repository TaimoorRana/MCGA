package com.concordia.mcga.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final MarkerOptions LOYOLA_MARKER = new MarkerOptions().position(Campus.LOYOLA.getMapCoordinates()).title(
        Campus.LOYOLA.getName());
    private static final MarkerOptions SGW_MARKER = new MarkerOptions().position(Campus.SGW.getMapCoordinates())
        .title(Campus.SGW.getName());
    Campus currentCampus = Campus.SGW;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("Google Map Style", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Google Map Style", "Can't find style. Error: ", e);
        }
        googleMap.addMarker(LOYOLA_MARKER);
        googleMap.addMarker(SGW_MARKER);

        Campus.SGW.populateCampusWithBuildings();
        Campus.LOYOLA.populateCampusWithBuildings();

        ArrayList<Building> sgwBuildings = Campus.SGW.getBuildings();
        ArrayList<Building> loyBuildings = Campus.LOYOLA.getBuildings();

        for (Building building : sgwBuildings) {
            map.addPolygon(building.getPolygonOverlayOptions());
            map.addMarker(building.getMarkerOptions());
        }

        for (Building building : loyBuildings) {
            map.addPolygon(building.getPolygonOverlayOptions());
            map.addMarker(building.getMarkerOptions());
        }

        updateCampus();
    }

    public void switchCampus(View v){
        if (currentCampus.equals(Campus.LOYOLA)){
            currentCampus = Campus.SGW;
        } else {
            currentCampus = Campus.LOYOLA;
        }
        updateCampus();
    }

    void updateCampus(){
        Button campusButton = (Button) findViewById(R.id.campusButton);
        campusButton.setText(currentCampus.getShortName());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCampus.getMapCoordinates(), 16));
    }

}
