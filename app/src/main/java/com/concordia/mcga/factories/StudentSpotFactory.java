package com.concordia.mcga.factories;

import android.content.res.Resources;
import android.database.Cursor;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.StudentSpot;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StudentSpotFactory {
    public final static int NAME_COLUMN_INDEX = 0;
    public final static int RATING_COLUMN_INDEX = 1;
    public final static int COORDINATE_COLUMN_INDEX = 2;
    public final static int ADDRESS_COLUMN_INDEX = 3;
    public final static int DESCRIPTION_COLUMN_INDEX = 4;
    public final static int RESOURCE_IMAGE_COLUMN_INDEX = 5;
    private final Gson gson;

    private static StudentSpotFactory instance;

    private StudentSpotFactory() {
        studentSpots = null;
        gson = new Gson();
    }

    private List<StudentSpot> studentSpots;

    public static StudentSpotFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new StudentSpotFactory();
    }

    public List<StudentSpot> getStudentSpots(Resources resources) {
        if (studentSpots == null) {
            getAllStudentSpots(resources);
        }

        return studentSpots;
    }

    /**
     * Creates student spots from database. Should only ever be called once from singleton getter
     * @param resources Android resources to properly get student spot image id
     */
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
            int resId = resources.getIdentifier(resourceName,"mipmap","com.concordia.mcga");
            float rating = res.getFloat(RATING_COLUMN_INDEX);
            LatLng centerCoordinates = gson.fromJson(
                    res.getString(COORDINATE_COLUMN_INDEX), LatLng.class);

            studentSpots.add(new StudentSpot(
                    centerCoordinates, name, rating, description, address, resId));
        }
        res.close();
    }
}