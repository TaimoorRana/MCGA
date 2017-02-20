package com.concordia.mcga.helperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Part of the code was taken from https://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 */

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "development.db";
    private static final String BUILDING_TABLE = "building";
    private static final String ID = "id",
            NAME = "name",
            SHORT_NAME = "short_name",
            CENTER_COORDINATE = "center_coordinate",
            EDGE_COORDINATES = "edge_coordinates";
    public static SQLiteDatabase db;
    private static Gson gson;
    //The Android's default system path of your application database.
    private static String DATABASE_PATH = "/data/data/com.example.mcga.mcga/databases/";
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        this.myContext = context;
        gson = new Gson();
    }

    public static void insertBuilding(String name, String shortName, LatLng centerCoordinate, List<LatLng> edgeCoordinateList, int resource_image_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(SHORT_NAME, shortName);
        contentValues.put(CENTER_COORDINATE, gson.toJson(centerCoordinate));
        contentValues.put(EDGE_COORDINATES, gson.toJson(edgeCoordinateList));
        contentValues.put("resource_image_id", resource_image_id);
        db.insert(BUILDING_TABLE, null, contentValues);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + BUILDING_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, short_name TEXT, center_coordinate TEXT, edge_coordinates TEXT, resource_image_id INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }


}
