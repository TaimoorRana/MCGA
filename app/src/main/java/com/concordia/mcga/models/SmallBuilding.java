package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by taimoorrana on 2017-02-16.
 */

public class SmallBuilding extends Building {
    private float markerVisibleAtMinimumZoomLevel = 18f;

    /**
     *  returns a Building object
     * @param centerCoordinate Center of the building
     * @param name Full name of the building
     * @param shortName Short name of the building
     * @param markerOptions Building's icon
     */
    public SmallBuilding(LatLng centerCoordinate, String name, String shortName, MarkerOptions markerOptions) {
        super(centerCoordinate, name, shortName, markerOptions);
    }

    /**
     * Depending the map's current zoom level, The marker visibility will turn on or off
     * @param mapZoomLevel map's current zoom level
     */
    @Override
    public void update(float mapZoomLevel) {
        if (mapZoomLevel >= markerVisibleAtMinimumZoomLevel) {
            marker.setVisible(true);
        } else {
            marker.setVisible(false);
        }
    }

}
