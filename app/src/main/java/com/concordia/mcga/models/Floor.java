package com.concordia.mcga.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.utilities.pathfinding.TiledMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private final static int X_COORDINATE_INDEX = 2, Y_COORDINATE_INDEX = 3,
            MAP_HEIGHT_INDEX = 2, MAP_WIDTH_INDEX = 3;
    private Building building;
    private TiledMap map;
    private int floorNumber;
    private List<IndoorPOI> indoorPOIs;
    private List<Elevator> elevators;
    private List<Staircase> staircases;
    private List<Escalator> escalators;

    public Floor() {
    }

    public Floor(Building building, int floorNumber) {
        this.building = building;
        this.floorNumber = floorNumber;
        this.indoorPOIs = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.staircases = new ArrayList<>();
        this.escalators = new ArrayList<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public List<IndoorPOI> getIndoorPOIs() {
        return indoorPOIs;
    }

    public JSONArray getRoomsJSON() {
        JSONArray indoorPOIArray = new JSONArray();
        for (IndoorPOI poi : getIndoorPOIs()) {
            if (poi instanceof Room) {
                Room room = (Room) poi;
                indoorPOIArray.put(room.toJson());
            }
        }
        return indoorPOIArray;
    }

    public void setIndoorPOIs(List<IndoorPOI> indoorPOIs) {
        this.indoorPOIs = indoorPOIs;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public List<Staircase> getStaircases() {
        return staircases;
    }

    public List<Escalator> getEscalators() {
        return escalators;
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public void addStaircase(Staircase staircase) {
        staircases.add(staircase);
    }

    public void addEscalator(Escalator escalator) {
        escalators.add(escalator);
    }

    public void populateTiledMap() {
        SQLiteDatabase db;
        TiledMap map = null;
        try {
            db = DatabaseConnector.getInstance().getDb();
            map = createTiledMap(building, floorNumber, db);
            insertWalkablePaths(building, floorNumber, db, map);

        } catch (MCGADatabaseException e) {
            e.printStackTrace();
        }

        setMap(map);
    }

    public void clearTiledMap() {
        map = null;
    }


    private void insertWalkablePaths(Building building, int floorNumber, SQLiteDatabase db,
                                     TiledMap map) throws MCGADatabaseException {
        Cursor walkablePathCursor = db
                .rawQuery(
                        "SELECT building, floor, x_coordinate, y_coordinate FROM walkable_paths WHERE building = ? AND floor = ?",
                        new String[]{building.getShortName(), String.valueOf(floorNumber)});

        while (walkablePathCursor.moveToNext()) {
            map.makeWalkable(new IndoorMapTile(walkablePathCursor.getInt(X_COORDINATE_INDEX),
                    walkablePathCursor.getInt(Y_COORDINATE_INDEX)));
        }
        walkablePathCursor.close();
    }

    private TiledMap createTiledMap(Building building, int floorNumber, SQLiteDatabase db)
            throws MCGADatabaseException {
        Cursor indoorMapCursor = db
                .rawQuery(
                        "SELECT building, floor, map_height, map_width FROM indoor_maps WHERE building = ? AND floor = ?",
                        new String[]{building.getShortName(), String.valueOf(floorNumber)});
        TiledMap map;
        if (indoorMapCursor.moveToNext()) {
            map = new TiledMap(indoorMapCursor.getInt(MAP_WIDTH_INDEX),
                    indoorMapCursor.getInt(MAP_HEIGHT_INDEX));
            indoorMapCursor.close();
        } else {
            indoorMapCursor.close();
            throw new MCGADatabaseException("No Floor Record Found");
        }
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Floor floor = (Floor) o;

        return new EqualsBuilder()
                .append(floorNumber, floor.floorNumber)
                .append(building, floor.building)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(building)
                .append(floorNumber)
                .toHashCode();
    }
}
