package com.concordia.mcga.factories;

import android.database.Cursor;

import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.SmallBuilding;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

/**
 *  Factory used to create buildings of various types.
 */
public class BuildingFactory {
    final static int NAME_COLUMN_INDEX = 1, SHORT_NAME_COLUMN_INDEX = 2, CENTER_COORDINATE_COLUMN_INDEX = 3,
    EDGE_COORDINATES_COLUMN_INDEX = 4, RESOURCE_IMAGE_COLUMN_INDEX = 5, IS_SMALL_BUILDING_COLUMN_INDEX = 6;
    private final static Gson GSON = new Gson();

    /**
     *  Creates a building object based on the row that the cursor is currently on.
     *
     * @param res - The {@link Cursor} object, currently located on a {@link Building}'s row.
     * @return Either a {@link SmallBuilding} or {@link Building} depending on the information that is found in the current row.
     */
    public static Building createBuilding(Cursor res) {
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(res.getInt(RESOURCE_IMAGE_COLUMN_INDEX)));
        LatLng centerCoordinates = GSON.fromJson(res.getString(CENTER_COORDINATE_COLUMN_INDEX), LatLng.class);
        String name = res.getString(NAME_COLUMN_INDEX);
        String shortName = res.getString(SHORT_NAME_COLUMN_INDEX);
        List<LatLng> edgeCoordinates = (List<LatLng>) GSON.fromJson(res.getString(EDGE_COORDINATES_COLUMN_INDEX), List.class);

        // 1 means that it is a small building
        if (res.getInt(IS_SMALL_BUILDING_COLUMN_INDEX) == 1){
            return new SmallBuilding(centerCoordinates, name, shortName, markerOptions)
                    .addEdgeCoordinate(edgeCoordinates);
        }
        return new Building(centerCoordinates, name, shortName, markerOptions)
                .addEdgeCoordinate(edgeCoordinates);
    }
}
