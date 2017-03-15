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
            ROOM_NUMBER_COLUMN_INDEX = 3;
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
        int room_number = res.getInt(ROOM_NUMBER_COLUMN_INDEX);

        // handle tile

        return new Room(centerCoordinates, name, null, room_number);
    }
}
