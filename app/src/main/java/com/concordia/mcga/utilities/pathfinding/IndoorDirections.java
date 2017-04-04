package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.models.IndoorMapTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 4/3/17.
 */

public class IndoorDirections {

    private static List<String> directionsList = new ArrayList<String>();
    private static List<String> imageList = new ArrayList<String>();


    // Act as a state machine
    Map<String, Integer> state = new HashMap<String, Integer>();

    int orientation;

    private static final String UNDEF_ORIENTATION = "UNDEF";
    private static final String NORTH_ORIENTATION = "NORTH";
    private static final String EAST_ORIENTATION = "EAST";
    private static final String SOUTH_ORIENTATION = "SOUTH";
    private static final String WEST_ORIENTATION = "WEST";

    ///////////////
    // Main Code //
    ///////////////


    public IndoorDirections(){
        state.put("UNDEF", 0);
        state.put("NORTH", 1);
        state.put("EAST", 2);
        state.put("SOUTH", 3);
        state.put("WEST", 4);

        orientation = state.get(UNDEF_ORIENTATION);
    }

    public static void clear(){
        directionsList.clear();
    }


    public static void setDirections(List<IndoorMapTile> tiles){
        String currentDirection = "";
        int distance = 0;

        try {
            int previousCoordinateX = tiles.get(0).getCoordinateX();
            int previousCoordinateY = tiles.get(0).getCoordinateY();

            for (int i = 1; i < tiles.size(); i++) {
                if (tiles.get(i).getCoordinateX() < previousCoordinateX){
                    distance = Math.abs(tiles.get(i).getCoordinateX() - previousCoordinateX);

                }
                else if (tiles.get(i).getCoordinateX() > previousCoordinateX){
                    distance = Math.abs(tiles.get(i).getCoordinateX() - previousCoordinateX);
                }

                else if (tiles.get(i).getCoordinateY() < previousCoordinateY){
                    distance = Math.abs(tiles.get(i).getCoordinateY() - previousCoordinateY);
                }

                else if (tiles.get(i).getCoordinateY() > previousCoordinateY){
                    distance = Math.abs(tiles.get(i).getCoordinateY() - previousCoordinateY);
                }
                else{
                    // Straight line. Problem in junction points given
                }

                // Update previous directions to the current ones
                previousCoordinateX = tiles.get(i).getCoordinateX();
                previousCoordinateY = tiles.get(i).getCoordinateY();
            }
        }catch(Exception e){
            // Do nothing
        }
    }
    
}
