package com.concordia.mcga.models;

import android.graphics.Color;

import com.concordia.mcga.helperClasses.Observer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class Building extends POI implements Observer {
    private final static int strokeColor = Color.YELLOW;
    private final static int strokeWidth = 2;
    private final static int fillColor = 0x996d171f;
    public int resourceImageId;
    Marker marker;
    private float markerVisibleAtMinimumZoomLevel = 15f;
    private String shortName;
    private MarkerOptions markerOptions;
    private List<LatLng> edgeCoordinateList;
    private List<IndoorMap> floorMaps;
    private Polygon polygon;

    /**
     *  returns a Building object
     * @param centerCoordinate Center of the building
     * @param name Full name of the building
     * @param shortName Short name of the building
     * @param markerOptions Building's icon
     */
    public Building(LatLng centerCoordinate, String name, String shortName, MarkerOptions markerOptions) {
        super(centerCoordinate, name);
        this.shortName = shortName;
        this.markerOptions = markerOptions.position(centerCoordinate).anchor(0.5f, 0.5f);
        edgeCoordinateList = new ArrayList<>();
        floorMaps = new ArrayList<>();
    }

    /**
     *
     * @return List of floor maps for this maps
     */
    public List<IndoorMap> getFloorMaps() {
        return floorMaps;
    }

    /**
     *
     * @param floorMaps Set floor maps for this building
     */
    public void setFloorMaps(List<IndoorMap> floorMaps) {
        this.floorMaps = floorMaps;
    }

    /**
     * @return Get building short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     *
     * @param shortName set building short name
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     *
     * @param edgeCoordinates Edge coordinates of the building
     * @return the building object
     */
    public Building addEdgeCoordinate(List<LatLng> edgeCoordinates) {
        edgeCoordinateList = edgeCoordinates;
        return this;
    }

    /**
     *
     * @return PolygonOptions object
     */
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

    /**
     *
     * @return Return MarkerOptions object used for the building icon
     */
    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    /**
     *
     * @param marker set building Marker object
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
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

    /**
     *
     * @return List of edges of this building
     */
    public List<LatLng> getEdgeCoordinateList() {
        return edgeCoordinateList;
    }

    /**
     *
     * @param resourceImageId Icon's resource id
     */
    public void setResourceImageId(int resourceImageId) {
        this.resourceImageId = resourceImageId;
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

    public Polygon getPolygon(){
        return polygon;
    }

    public Marker getMarker(){
        return marker;
    }

}
