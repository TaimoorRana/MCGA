package com.concordia.mcga.factories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.utilities.pathfinding.IndoorMapTile;
import com.concordia.mcga.utilities.pathfinding.TiledMap;

public class IndoorMapFactory {

    private final static int X_COORDINATE_INDEX = 2, Y_COORDINATE_INDEX = 3,
        MAP_HEIGHT_INDEX = 2, MAP_WIDTH_INDEX = 3;
    private static IndoorMapFactory instance;

    /**
     * @return The singleton instance of this class
     */
    public static IndoorMapFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new IndoorMapFactory();
    }

    /**
     * Used for testing purposes only
     */
    public static void setInstance(IndoorMapFactory instance) {
        IndoorMapFactory.instance = instance;
    }

    IndoorMapFactory(){}

    /**
     * @param building - {@link Building} of the desired {@link Floor}
     * @param floorNumber - floor number of the desired {@link Floor}
     * @return An {@link Floor} for the given building and floorNumber. Returns <b>null</b> if
     * the building/floorNumber combination does not exist in the database
     */
    public Floor createIndoorMap(Building building, int floorNumber) {
        SQLiteDatabase db = null;
        try {
            db = DatabaseConnector.getInstance().getDb();
            TiledMap map = createTiledMap(building, floorNumber, db);
            insertWalkablePaths(building, floorNumber, db, map);
            Floor floor = new Floor();
            floor.setFloorNumber(floorNumber);
            floor.setMap(map);
            return floor;
        } catch (MCGADatabaseException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    void insertWalkablePaths(Building building, int floorNumber, SQLiteDatabase db,
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

    TiledMap createTiledMap(Building building, int floorNumber, SQLiteDatabase db)
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
}
