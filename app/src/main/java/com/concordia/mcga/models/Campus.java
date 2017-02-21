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
    public static final Campus LOYOLA = new Campus(new LatLng(45.458563, -73.640156), "Loyola Campus", "LOY");
    public static final Campus SGW = new Campus(new LatLng(45.497100, -73.579077), "SGW Campus", "SGW");
    private String shortName;
    private ArrayList<Building> buildings;

    private Campus(LatLng mapCoordinates, String name, String shortName) {
        super(mapCoordinates, name);
        this.shortName = shortName;
        buildings = new ArrayList<>();
    }

    public void populateCampusesWithBuildings() {
        if (DatabaseHelper.getInstance() == null || !buildings.isEmpty())  // if the database has not been initialized || buildings already exists
        {
            return;
        }
        int nameColumnIndex = 1, shortNameColumnIndex = 2, centerCoordinateColumnIndex = 3,
                edgeCoordinatesColumnIndex = 4, resourceImageColumnIndex = 5, isSmallBuildingColumnIndex = 6;

        Gson gson = new Gson();
        Cursor res = DatabaseHelper.db.rawQuery("select * from building", null);
        Type listType = new TypeToken<List<LatLng>>() {
        }.getType();
        while (res.moveToNext()) {
            MarkerOptions adMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(res.getInt(resourceImageColumnIndex)));
            if (res.getInt(isSmallBuildingColumnIndex) == 1) // 1 means that it is a small building
            {
                buildings.add(new SmallBuilding(gson.fromJson(res.getString(centerCoordinateColumnIndex), LatLng.class), res.getString(nameColumnIndex), res.getString(shortNameColumnIndex), adMarkerOptions)
                        .addEdgeCoordinate((List<LatLng>) gson.fromJson(res.getString(edgeCoordinatesColumnIndex), listType)));
            } else {
                buildings.add(new Building(gson.fromJson(res.getString(centerCoordinateColumnIndex), LatLng.class), res.getString(nameColumnIndex), res.getString(shortNameColumnIndex), adMarkerOptions)
                        .addEdgeCoordinate((List<LatLng>) gson.fromJson(res.getString(edgeCoordinatesColumnIndex), listType)));
            }
        }
        res.close();
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
