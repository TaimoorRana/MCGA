package com.concordia.mcga.models;

import android.database.Cursor;

import com.concordia.mcga.factories.BuildingFactory;
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

    /**
     *  Populates both SGW and Loyola {@link Campus} objects from the data found in the SQLite database.
     */
    public static void populateCampusesWithBuildings() {
        if (DatabaseHelper.getInstance() == null || !SGW.buildings.isEmpty() || !LOY.buildings.isEmpty())  // if the database has not been initialized || buildings already exists
        {
            return;
        }
        final int CAMPUS_COLUMN_INDEX = 7;
        Cursor res = DatabaseHelper.getDb().rawQuery("select * from building", null);
        Type listType = new TypeToken<List<LatLng>>() {}.getType();

        while (res.moveToNext()) {
            if (res.getString(CAMPUS_COLUMN_INDEX).equalsIgnoreCase(SGW.getShortName())) {
                SGW.buildings.add(BuildingFactory.createBuilding(res));
            } else {
                LOY.buildings.add(BuildingFactory.createBuilding(res));
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
