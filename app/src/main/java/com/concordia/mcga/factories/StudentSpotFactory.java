package com.concordia.mcga.factories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.StudentSpot;
import com.concordia.mcga.utilities.pathfinding.TiledMap;

public class StudentSpotFactory {
    public final static int NAME_COLUMN_INDEX = 1, RATING_COLUMN_INDEX = 2,
            COORDINATE_COLUMN_INDEX = 3, ADDRESS_COLUMN_INDEX = 4, DESCRIPTION_COLUMN_INDEX = 5;

    public StudentSpotFactory() {
    }

    public StudentSpot createStudentSpot() {
        return null;
    }
}