package com.concordia.mcga.factories;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.support.annotation.NonNull;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.IndoorMap;
import com.concordia.mcga.utilities.pathfinding.TiledMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IndoorMapFactory {
    public static IndoorMap getHall4thFloor(Context context) {
        return getIndoorMap(context, R.raw.hall4);
    }

    @NonNull
    private static IndoorMap getIndoorMap(Context context, int rawResourceId) {
        TiledMap map = new TiledMap(350,317);
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(rawResourceId)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coordinates = line.split(",");
                map.makeWalkable(parseInt(coordinates[0]), parseInt(coordinates[1]));
            }
        } catch (IOException e){
            System.err.println(e);
        }
        IndoorMap indoorMap = new IndoorMap();
        indoorMap.setMap(map);
        return indoorMap;
    }
}
