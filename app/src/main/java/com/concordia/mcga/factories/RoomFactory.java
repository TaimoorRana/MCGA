package com.concordia.mcga.factories;

import android.database.Cursor;
import android.util.Log;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.Room;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 *  Factory used to create rooms from the database.
 */
public class RoomFactory {
    public final static int NAME_COLUMN_INDEX = 1, CENTER_COORDINATE_COLUMN_INDEX = 2, INDOORMAPTILE_COLUMN_INDEX = 3,
            ROOM_NUMBER_COLUMN_INDEX = 4, FLOOR_NUMBER_COLUMN_INDEX = 6, POLYGON_COORDINATE_COLUMN_INDEX = 7, ROOM_ICON_COLUMN_INDEX = 8;
    private final static Gson GSON = new Gson();

    /**
     *  Creates a room object based on the row that the cursor is currently on.
     *
     * @param res - The {@link Cursor} object, currently located on a {@link Room}'s row.
     * @return A {@link Room} matching the room pointed to by the cursor.
     */
    public static Room createRoom(Cursor res) {
        String name = res.getString(NAME_COLUMN_INDEX);

        LatLng centerCoordinates = GSON.fromJson(res.getString(CENTER_COORDINATE_COLUMN_INDEX),
                LatLng.class);

        Type indoorTileType = new TypeToken<IndoorMapTile>() {}.getType();
        IndoorMapTile indoorMapTile = (IndoorMapTile) GSON.fromJson(res.getString(INDOORMAPTILE_COLUMN_INDEX), indoorTileType);


        String room_number = res.getString(ROOM_NUMBER_COLUMN_INDEX);
        int floor_number = res.getInt(FLOOR_NUMBER_COLUMN_INDEX);

        Type latlngType = new TypeToken<List<LatLng>>() {}.getType();
        List<LatLng> polygonCoordinates = (List<LatLng>) GSON.fromJson(res.getString(POLYGON_COORDINATE_COLUMN_INDEX), latlngType);

        String room_icon = res.getString(ROOM_ICON_COLUMN_INDEX);
        Room.RoomIcon roomIcon = Room.RoomIcon.valueOf(room_icon);

        return new Room(centerCoordinates, name, indoorMapTile, room_number, floor_number, polygonCoordinates, roomIcon);
    }

    public static Room createRoomById(int roomId) {
        Cursor res = null;
        Room room = null;
        try {
            res = DatabaseConnector.getInstance().getDb().rawQuery("select * from room WHERE _id = " + roomId, null);
            if (res.moveToFirst()) {
                room = createRoom(res);
            }
        } catch (MCGADatabaseException e) {
            throw new Error("Database not initialized");
        } finally {
            res.close();
        }
        return room;
    }
}
