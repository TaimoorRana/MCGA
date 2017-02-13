package com.concordia.mcga.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private enum ViewType {
        INDOOR, OUTDOOR
    };

    private static final MarkerOptions LOYOLA_MARKER = new MarkerOptions().position(Campus.LOYOLA.getMapCoordinates()).title(
        Campus.LOYOLA.getName());
    private static final MarkerOptions SGW_MARKER = new MarkerOptions().position(Campus.SGW.getMapCoordinates())
        .title(Campus.SGW.getName());
    Campus currentCampus = Campus.SGW;
    GoogleMap map;

    //State
    private ViewType viewType;

    //Fragments
    private SupportMapFragment mapFragment;
    private TransportButtonFragment transportButtonFragment;
    private IndoorMapFragment indoorMapFragment;

    //View Components
    private Button campusButton;
    private Button viewSwitchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Init Fragments
        transportButtonFragment = (TransportButtonFragment) getSupportFragmentManager().findFragmentById(R.id.transportButton);
        indoorMapFragment = (IndoorMapFragment) getSupportFragmentManager().findFragmentById(R.id.indoormap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Init View Components
        campusButton = (Button) findViewById(R.id.campusButton);
        viewSwitchButton = (Button) findViewById(R.id.viewSwitchButton);
        viewSwitchButton.setText("GO INDOORS");


        //Set initial view type
        viewType = ViewType.OUTDOOR;

        getSupportFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
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
        googleMap.addMarker(LOYOLA_MARKER);
        googleMap.addMarker(SGW_MARKER);
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
        //Button campusButton = (Button) findViewById(R.id.campusButton);
        campusButton.setText(currentCampus.getShortName());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCampus.getMapCoordinates(), 16));
    }

    public void toggleView(View v) {
        if (viewType == ViewType.OUTDOOR) {
            viewType = ViewType.INDOOR;
            getSupportFragmentManager().beginTransaction().show(indoorMapFragment).hide(mapFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(transportButtonFragment).commit();
            campusButton.setVisibility(View.GONE);
            viewSwitchButton.setText("GO OUTDOORS");
        } else {
            viewType = ViewType.OUTDOOR;
            getSupportFragmentManager().beginTransaction().show(mapFragment).hide(indoorMapFragment).commit();
            getSupportFragmentManager().beginTransaction().show(transportButtonFragment).commit();
            campusButton.setVisibility(View.VISIBLE);
            viewSwitchButton.setText("GO INDOORS");
        }
    }

}
