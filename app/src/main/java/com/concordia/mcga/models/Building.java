package com.concordia.mcga.models;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Building extends POI {
    private final static int strokeColor = Color.YELLOW;
    private final static int strokeWidth = 2;
    private final static int fillColor = 0x996d171f;
    private String shortName;
    private MarkerOptions markerOptions;
    private List<LatLng> edgeCoordinateList;
    private List<IndoorMap> floorMaps;

    public Building(LatLng centerCoordinates, String name, String shortName, MarkerOptions markerOptions) {
        super(centerCoordinates, name);
        this.shortName = shortName;
        this.markerOptions = markerOptions.position(centerCoordinates).anchor(0.5f, 0.5f);
        edgeCoordinateList = new ArrayList<>();
        floorMaps = new ArrayList<>();
    }

    public List<IndoorMap> getFloorMaps() {
        return floorMaps;
    }

    public void setFloorMaps(List<IndoorMap> floorMaps) {
        this.floorMaps = floorMaps;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public Building addEdgeCoordinate(LatLng... edgeCoordinates) {
        edgeCoordinateList = Arrays.asList(edgeCoordinates);
        return this;
    }

    public PolygonOptions getPolygonOverlayOptions(){

        if(edgeCoordinateList.isEmpty()){
            return null;
        }

        PolygonOptions polygonOptions = new PolygonOptions();

        for (LatLng edgeCoordinate : edgeCoordinateList) {
            polygonOptions.add(edgeCoordinate);
        }

        polygonOptions.strokeColor(strokeColor)
                    .strokeWidth(strokeWidth)
                    .fillColor(fillColor);

        return polygonOptions;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

}
