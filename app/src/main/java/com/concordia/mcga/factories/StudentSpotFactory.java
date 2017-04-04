package com.concordia.mcga.factories;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.res.ResourcesCompat;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.SmallBuilding;
import com.concordia.mcga.models.StudentSpot;
import com.concordia.mcga.utilities.pathfinding.TiledMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StudentSpotFactory {
    public final static int NAME_COLUMN_INDEX = 0, RATING_COLUMN_INDEX = 1,
            COORDINATE_COLUMN_INDEX = 2, ADDRESS_COLUMN_INDEX = 3, DESCRIPTION_COLUMN_INDEX = 4,
            RESOURCE_IMAGE_COLUMN_INDEX = 5;
    private final Gson GSON;

    private static StudentSpotFactory instance;

    private List<StudentSpot> studentSpots;

    public static StudentSpotFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new StudentSpotFactory();
    }

    private StudentSpotFactory() {
        studentSpots = null;
        GSON = new Gson();
    }

    public List<StudentSpot> getStudentSpots(Resources resources) {
        if (studentSpots == null) {
            getAllStudentSpots(resources);
        }

        return studentSpots;
    }

    private void getAllStudentSpots(Resources resources) {
        studentSpots = new ArrayList<>();

        Cursor res;
        try {
            res = DatabaseConnector.getInstance().getDb().rawQuery(
                    "select * from student_spots", null);
        } catch (MCGADatabaseException e) {
            throw new Error("Database not initialized");
        }

        while (res.moveToNext()) {
            String resourceName = res.getString(RESOURCE_IMAGE_COLUMN_INDEX);
            String name = res.getString(NAME_COLUMN_INDEX);
            String description = res.getString(DESCRIPTION_COLUMN_INDEX);
            String address = res.getString(ADDRESS_COLUMN_INDEX);
            int resId = resources.getIdentifier(resourceName,"drawable","com.concordia.mcga");
            float rating = res.getFloat(RATING_COLUMN_INDEX);
            LatLng centerCoordinates = GSON.fromJson(
                    res.getString(COORDINATE_COLUMN_INDEX), LatLng.class);

            studentSpots.add(new StudentSpot(
                    centerCoordinates, name, rating, description, address, resId));
        }
        res.close();
    }
}