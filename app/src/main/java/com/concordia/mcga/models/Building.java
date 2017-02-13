package com.concordia.mcga.models;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Building {
    private final static int strokeColor = Color.YELLOW;
    private final static int strokeWidth = 2;
    private final static int fillColor = 0x996d171f;
    private LatLng centerCoordinate;
    private String shortName;
    private String name;
    private MarkerOptions markerOptions;
    private List<LatLng> edgeCoordinateList;

    public Building(LatLng centerCoordinates, String name, String shortName, MarkerOptions markerOptions) {
        this.centerCoordinate = centerCoordinates;
        this.name = name;
        this.shortName = shortName;
        this.markerOptions = markerOptions.position(centerCoordinates).anchor(0.5f, 0.5f);
        edgeCoordinateList = new ArrayList<>();
    }

    public LatLng getCenterCoordinates(){
        return centerCoordinate;
    }

    public String getName(){
        return name;
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
