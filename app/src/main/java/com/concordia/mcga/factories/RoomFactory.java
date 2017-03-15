package com.concordia.mcga.factories;

import android.database.Cursor;
import com.concordia.mcga.models.Room;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

/**
 *  Factory used to create rooms from the database.
 */
public class RoomFactory {
    final static int NAME_COLUMN_INDEX = 1, CENTER_COORDINATE_COLUMN_INDEX = 2,
            FLOOR_NUMBER_COLUMN_INDEX = 3, ROOM_NUMBER_COLUMN_INDEX = 4,
            INDOOR_COORDINATE_X_COLUMN_INDEX = 5, INDOOR_COORDINATE_Y_COLUMN_INDEX = 6;
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
        int floor_number = res.getInt(FLOOR_NUMBER_COLUMN_INDEX);
        int room_number = res.getInt(ROOM_NUMBER_COLUMN_INDEX);
        int indoor_coordinate_x = res.getInt(INDOOR_COORDINATE_X_COLUMN_INDEX);
        int indoor_coordinate_y = res.getInt(INDOOR_COORDINATE_Y_COLUMN_INDEX);

        return new Room(centerCoordinates, name, floor_number, indoor_coordinate_x,
                indoor_coordinate_y, room_number);
    }
}
