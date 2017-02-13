package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

public class NavigationFragment extends Fragment implements OnMapReadyCallback {

    private static final MarkerOptions LOYOLA_MARKER = new MarkerOptions().position(Campus.LOYOLA.getMapCoordinates()).title(
            Campus.LOYOLA.getName());
    private static final MarkerOptions SGW_MARKER = new MarkerOptions().position(Campus.SGW.getMapCoordinates())
            .title(Campus.SGW.getName());
    Campus currentCampus = Campus.SGW;
    GoogleMap map;

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

        MultiStateToggleButton button = (MultiStateToggleButton) getView().findViewById(R.id.campusButton);
        button.setValue(0); // set SGW by default
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                switchCampus(position == 1);
            }
        });
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

    public void switchCampus(boolean loyola){
        if (loyola){
            currentCampus = Campus.LOYOLA;
        } else {
            currentCampus = Campus.SGW;
        }
        updateCampus();
    }

    void updateCampus(){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCampus.getMapCoordinates(), 16));
    }
}
