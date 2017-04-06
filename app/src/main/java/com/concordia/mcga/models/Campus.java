package com.concordia.mcga.models;

import android.database.Cursor;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.factories.BuildingFactory;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Campus extends POI {
    public static final Campus LOY = new Campus(new LatLng(45.458265, -73.639071), "Loyola Campus", "LOY");
    public static final Campus SGW = new Campus(new LatLng(45.495203, -73.576981), "SGW Campus", "SGW");
    private String shortName;
    private List<Building> buildings;

    /**
     * Creates a Campus Object
     * @param mapCoordinates Campus location
     * @param name Full name
     * @param shortName Abbreviated Name
     */
    private Campus(LatLng mapCoordinates, String name, String shortName) {
        super(mapCoordinates, name);
        this.shortName = shortName;
        buildings = new ArrayList<>();
    }

    /**
     *  Populates both SGW and Loyola {@link Campus} objects from the data found in the SQLite database.
     */
    public static void populateCampusesWithBuildings() {
        final int CAMPUS_COLUMN_INDEX = 7;
        Cursor res;
        try {
            if (!SGW.buildings.isEmpty() || !LOY.buildings.isEmpty())  // if the database has not been initialized || buildings already exists
            {
                return;
            }
            res = DatabaseConnector.getInstance().getDb().rawQuery("select * from building", null);
        } catch (MCGADatabaseException e) {
            throw new Error("Database not initialized");
        }
        while (res.moveToNext()) {
            Building building = BuildingFactory.createBuilding(res);
            building.populateRooms();
            building.populateFloors();
            building.populateConnectedPOIs();

            if (res.getString(CAMPUS_COLUMN_INDEX).equalsIgnoreCase(SGW.getShortName())) {
                SGW.buildings.add(building);
            } else {
                LOY.buildings.add(building);
            }
        }
        res.close();
    }

    /**
     *
     * @param object can be a marker object or polygon object to search for
     * @return return the building which contains this marker or polygon
     */
    public static Building getBuilding(Object object) {
        List<Building> allBuildings = new ArrayList<>(Campus.SGW.getBuildings());
        allBuildings.addAll(Campus.LOY.getBuildings());

        if (object instanceof Marker) {
            Marker marker = (Marker) object;
            for (Building building : allBuildings) {
                if (building.getMarker().getId().equalsIgnoreCase(marker.getId()))
                    return building;
            }
        }

        if (object instanceof Polygon) {
            Polygon polygon = (Polygon) object;
            for (Building building : allBuildings) {
                if (building.getPolygon().getId().equalsIgnoreCase(polygon.getId()))
                    return building;
            }
        }
        return null;
    }

    /**

     * @return List of buildings in this campus
     */
    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     *
     * @return The abbreviated name of this campus
     */
    public String getShortName() {
        return shortName;
    }

    public void addBuilding(Building building){
        buildings.add(building);
    }

}
