package com.concordia.mcga.models;

import android.graphics.Color;

import com.concordia.mcga.helperClasses.Observer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class Building extends POI implements Observer {
    private final static int strokeColor = Color.YELLOW;
    private final static int strokeWidth = 2;
    private final static int fillColor = 0x996d171f;
    public int resourceImageId;
    protected Marker marker;
    private float markerVisibleAtMinimumZoomLevel = 15f;
    private LatLng centerCoordinate;
    private String shortName;
    private MarkerOptions markerOptions;
    private List<LatLng> edgeCoordinateList;
    private List<IndoorMap> floorMaps;

    public Building(LatLng centerCoordinate, String name, String shortName, MarkerOptions markerOptions) {
        super(centerCoordinate, name);
        this.shortName = shortName;
        this.markerOptions = markerOptions.position(cente   rCoordinate).anchor(0.5f, 0.5f);
        edgeCoordinateList = new ArrayList<>();
        floorMaps = new ArrayList<>();
        this.centerCoordinate = centerCoordinate;
    }

    public List<IndoorMap> getFloorMaps() {
        return floorMaps;
    }

    public void setFloorMaps(List<IndoorMap> floorMaps) {
        this.floorMaps = floorMaps;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Building addEdgeCoordinate(List<LatLng> edgeCoordinates) {
        edgeCoordinateList = edgeCoordinates;
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


    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public void update(float mapZoomLevel) {
        if (mapZoomLevel >= markerVisibleAtMinimumZoomLevel) {
            marker.setVisible(true);
        } else {
            marker.setVisible(false);
        }
    }

    public List<LatLng> getEdgeCoordinateList() {
        return edgeCoordinateList;
    }

    public Building setResourceImageId(int resourceImageId) {
        this.resourceImageId = resourceImageId;
        return this;
    }

}
