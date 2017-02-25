package com.concordia.mcga.models;

import android.database.Cursor;

import com.concordia.mcga.helperClasses.DatabaseHelper;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Campus extends POI {
    public static final Campus LOY = new Campus(new LatLng(45.458563, -73.640156), "Loyola Campus", "LOY");
    public static final Campus SGW = new Campus(new LatLng(45.497100, -73.579077), "SGW Campus", "SGW");
    private String shortName;
    private List<Building> buildings;

    private Campus(LatLng mapCoordinates, String name, String shortName) {
        super(mapCoordinates, name);
        this.shortName = shortName;
        buildings = new ArrayList<>();
    }

    public static void populateCampusesWithBuildings() {
        if (DatabaseHelper.getInstance() == null || !SGW.buildings.isEmpty() || !LOY.buildings.isEmpty())  // if the database has not been initialized || buildings already exists
        {
            return;
        }

        final int NAME_COLUMN_INDEX = 1, SHORT_NAME_COLUMN_INDEX = 2, CENTER_COORDINATE_COLUMN_INDEX = 3,
                EDGE_COORDINATES_COLUMN_INDEX = 4, RESOURCE_IMAGE_COLUMN_INDEX = 5, IS_SMALL_BUILDING_COLUMN_INDEX = 6, CAMPUS_COLUMN_INDEX = 7;

        Gson gson = new Gson();
        Cursor res = DatabaseHelper.getDb().rawQuery("select * from building", null);
        Type listType = new TypeToken<List<LatLng>>() {
        }.getType();

        while (res.moveToNext()) {
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(res.getInt(RESOURCE_IMAGE_COLUMN_INDEX)));
            if (res.getString(CAMPUS_COLUMN_INDEX).equalsIgnoreCase(SGW.getShortName())) {
                if (res.getInt(IS_SMALL_BUILDING_COLUMN_INDEX) == 1) // 1 means that it is a small building
                {
                    SGW.buildings.add(new SmallBuilding(gson.fromJson(res.getString(CENTER_COORDINATE_COLUMN_INDEX), LatLng.class), res.getString(NAME_COLUMN_INDEX), res.getString(SHORT_NAME_COLUMN_INDEX), markerOptions)
                            .addEdgeCoordinate((List<LatLng>) gson.fromJson(res.getString(EDGE_COORDINATES_COLUMN_INDEX), listType)));
                } else {
                    SGW.buildings.add(new Building(gson.fromJson(res.getString(CENTER_COORDINATE_COLUMN_INDEX), LatLng.class), res.getString(NAME_COLUMN_INDEX), res.getString(SHORT_NAME_COLUMN_INDEX), markerOptions)
                            .addEdgeCoordinate((List<LatLng>) gson.fromJson(res.getString(EDGE_COORDINATES_COLUMN_INDEX), listType)));
                }
            } else {
                if (res.getInt(IS_SMALL_BUILDING_COLUMN_INDEX) == 1) // 1 means that it is a small building
                {
                    LOY.buildings.add(new SmallBuilding(gson.fromJson(res.getString(CENTER_COORDINATE_COLUMN_INDEX), LatLng.class), res.getString(NAME_COLUMN_INDEX), res.getString(SHORT_NAME_COLUMN_INDEX), markerOptions)
                            .addEdgeCoordinate((List<LatLng>) gson.fromJson(res.getString(EDGE_COORDINATES_COLUMN_INDEX), listType)));
                } else {
                    LOY.buildings.add(new Building(gson.fromJson(res.getString(CENTER_COORDINATE_COLUMN_INDEX), LatLng.class), res.getString(NAME_COLUMN_INDEX), res.getString(SHORT_NAME_COLUMN_INDEX), markerOptions)
                            .addEdgeCoordinate((List<LatLng>) gson.fromJson(res.getString(EDGE_COORDINATES_COLUMN_INDEX), listType)));
                }
            }
        }
        res.close();
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public String getShortName() {
        return shortName;
    }

}
