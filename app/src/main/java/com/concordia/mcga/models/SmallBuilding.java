package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by taimoorrana on 2017-02-16.
 */

public class SmallBuilding extends Building {
    private float markerVisibleAtMinimumZoomLevel = 18f;

    public SmallBuilding(LatLng centerCoordinates, String name, String shortName, MarkerOptions markerOptions) {
        super(centerCoordinates, name, shortName, markerOptions);
    }

    @Override
    public void update(float mapZoomLevel) {
        if (mapZoomLevel >= markerVisibleAtMinimumZoomLevel) {
            marker.setVisible(true);
        } else {
            marker.setVisible(false);
        }
    }

}
