package com.concordia.mcga.models;

import android.database.Cursor;
import android.graphics.Color;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.factories.RoomFactory;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.factories.IndoorMapFactory;
import com.concordia.mcga.helperClasses.Observer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Room> rooms;
    private Polygon polygon;
    private Map<Integer, Floor> floorMaps;

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
        floorMaps = new HashMap<>();
        rooms = new ArrayList<>();
    }

    /**
     *  Populate this Building object with rooms retrieved via database.
     */
    public void populateRooms() {
        final int BUILDING_COLUMN_INDEX = 4;
        Cursor res;

        try {
            if (!rooms.isEmpty())  // if the building has already had its POIs retrieved
            {
                return;
            }
            res = DatabaseConnector.getInstance().getDb().rawQuery("select * from room", null);
        } catch (MCGADatabaseException e) {
            throw new Error("Database not initialized");
        }
        while (res.moveToNext()) {
            if (res.getString(BUILDING_COLUMN_INDEX).equals(getShortName())) {
                rooms.add(RoomFactory.createRoom(res));
            }
        }
        res.close();
    }

    /**
     *
     * @return Floor maps for the given floor Number
     */
    public Floor getFloorMap(int floorNumber) {
        Floor returnMap = floorMaps.get(floorNumber);
        if (returnMap == null){
            returnMap = IndoorMapFactory.getInstance().createIndoorMap(this, floorNumber);
            floorMaps.put(floorNumber, returnMap);
        }
        return returnMap;
    }

    /**
     * Get all the rooms belonging to this building
     * @return List of all the rooms associated with this building
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Setter for floorMaps
     * @param floorMaps
     */
    public void setFloorMaps(Map<Integer, Floor> floorMaps) {
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
