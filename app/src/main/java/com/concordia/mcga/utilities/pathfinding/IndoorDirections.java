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






    private static final int UNDEF_ORIENTATION = 0;
    private static final int NORTH_ORIENTATION = 1;
    private static final int EAST_ORIENTATION = 2;
    private static final int SOUTH_ORIENTATION = 3;
    private static final int WEST_ORIENTATION = 4;

    private int orientation = UNDEF_ORIENTATION;
    ///////////////
    // Main Code //
    ///////////////


    public IndoorDirections(){
    }

    public static void clear(){
        directionsList.clear();
    }


    public static void setDirections(List<IndoorMapTile> tiles){
        String currentDirection = "";
        int distance = 0;
        String turn = "";

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

    private void setOrientation(int orientation){
        this.orientation = orientation;
    }


    private int getNextOrientation(int x1, int x2, int y1, int y2){
        // going right
        if (x1 < x2){
            
        }

        // going left
        else if (x1 > x2){

        }

        // going down
        else if (y1 < y2){

        }

        // going up
        else if (y1 > y2){

        }
        else{
            //this should never happen
        }
        return orientation;
    }

    private void goClockWise(){
        switch (orientation){
            case UNDEF_ORIENTATION:
                // Should not happen
                break;

            case NORTH_ORIENTATION:
                orientation = EAST_ORIENTATION;
                break;

            case EAST_ORIENTATION:
                orientation = SOUTH_ORIENTATION;
                break;

            case SOUTH_ORIENTATION:
                orientation = WEST_ORIENTATION;
                break;

            case WEST_ORIENTATION:
                orientation = NORTH_ORIENTATION;
                break;

            default:
                break;
        }
    }

    private void goCounterClockWise(){
        switch (orientation){
            case UNDEF_ORIENTATION:
                // Should not happen
                break;

            case NORTH_ORIENTATION:
                orientation = WEST_ORIENTATION;
                break;

            case EAST_ORIENTATION:
                orientation = NORTH_ORIENTATION;
                break;

            case SOUTH_ORIENTATION:
                orientation = EAST_ORIENTATION;
                break;

            case WEST_ORIENTATION:
                orientation = SOUTH_ORIENTATION;
                break;

            default:
                break;
        }
    }
}
