package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

public class Portal extends IndoorPOI {
    public Portal(LatLng mapCoordinates, String name, IndoorMapTile tile) {
        super(mapCoordinates, name, tile);
    }
}
