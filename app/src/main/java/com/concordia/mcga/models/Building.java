package com.concordia.mcga.models;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class Building {
    private LatLng centerCoordinate;
    private String abbreviatedName;
    private String name;
    private ArrayList<LatLng> edgeCoordinateList;
    private final static int strokeColor = Color.YELLOW;
    private final static int strokeWidth = 2;
    private final static int fillColor = 0x996d171f;

    public Building(LatLng coordinates, String name, String abbreviatedName) {
        this.centerCoordinate = coordinates;
        this.name = name;
        this.abbreviatedName = abbreviatedName;
        edgeCoordinateList = new ArrayList<>();
    }

    public LatLng getCoordinates(){
        return centerCoordinate;
    }

    public String getName(){
        return name;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void addEdgeCoordinate(LatLng edgeCoordinate){
        if(edgeCoordinate != null) {
            edgeCoordinateList.add(edgeCoordinate);
        }
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

}
