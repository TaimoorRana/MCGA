package com.concordia.mcga.factories;

import android.database.Cursor;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.ConnectedPOI;
import com.concordia.mcga.models.Elevator;
import com.concordia.mcga.models.Escalator;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.models.Staircase;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory used to create rooms from the database.
 */
public class ConnectedPOIFactory {

    enum ConnectedPOIDiscriminator {
        ESCALATOR,
        ELEVATOR,
        STAIRCASE
    }

    public final static int ID_COLUMN_INDEX = 0, NAME_COLUMN_INDEX = 1, BUILDING_COLUMN_INDEX = 2, FLOOR_NUMBER_COLUMN_INDEX = 3, DISCRIMINATOR_COLUMN_INDEX = 4;
    public final static int LINK_CONNECTEDPOI_ID_INDEX = 0, LINK_INDOORPOI_ID_INDEX = 1;

    /**
     * Creates a room object based on the row that the cursor is currently on.
     *
     * @param res - The {@link Cursor} object, currently located on a {@link ConnectedPOI}'s row.
     * @return A {@link ConnectedPOI} matching the ConnectedPOI pointed to by the cursor.
     */
    public static ConnectedPOI createConnectedPOI(Cursor res, Building building) {

        int connectedPoiId = res.getInt(ID_COLUMN_INDEX);
        String name = res.getString(NAME_COLUMN_INDEX);
        int floor_number = res.getInt(FLOOR_NUMBER_COLUMN_INDEX);

        String discriminator = res.getString(DISCRIMINATOR_COLUMN_INDEX);
        ConnectedPOIDiscriminator poiDiscriminator = ConnectedPOIDiscriminator.valueOf(discriminator);

        Map<Integer, IndoorPOI> indoorPOIs = new HashMap<>();

        try {
            res = DatabaseConnector.getInstance().getDb().rawQuery("select * from connected_poi_links WHERE connectedPoiId = " + connectedPoiId, null);
        } catch (MCGADatabaseException e) {
            throw new Error("Database not initialized");
        }
        while (res.moveToNext()) {
            Room room = RoomFactory.createRoomById(res.getInt(LINK_INDOORPOI_ID_INDEX));
            room.setFloor(building.getFloorMap(room.getFloorNumber()));
            indoorPOIs.put(room.getFloorNumber(), room);
        }

        switch (poiDiscriminator) {
            case ESCALATOR:
                Escalator escalator = new Escalator(floor_number);
                escalator.setIndoorPOIs(indoorPOIs);
                return escalator;
            case ELEVATOR:
                Elevator elevator = new Elevator(floor_number);
                elevator.setIndoorPOIs(indoorPOIs);
                return elevator;
            case STAIRCASE:
                Staircase staircase = new Staircase(floor_number);
                staircase.setIndoorPOIs(indoorPOIs);
                return staircase;
        }
        return null;
    }
}
