package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;

import com.concordia.mcga.exceptions.MCGAJunctionPointException;
import com.concordia.mcga.models.IndoorMapTile;

import java.util.List;

import static android.content.ContentValues.TAG;


public class IndoorDirections {

    private static final int UNDEF_ORIENTATION = 0;
    private static final int NORTH_ORIENTATION = 1;
    private static final int EAST_ORIENTATION = 2;
    private static final int SOUTH_ORIENTATION = 3;
    private static final int WEST_ORIENTATION = 4;

    private int orientation;

    private static final int ERROR_TOLERANCE = 5;

    ///////////////
    // Main Code //
    ///////////////

    /**
     * Constructor
     */
    public IndoorDirections(){
        orientation = UNDEF_ORIENTATION;
    }

    /**
     * Get list of directions for the user
     * @param tiles
     * @return String to tell user what to do as well as an image
     */
    public String[][] getDirections(List<IndoorMapTile> tiles){
        String currentDirection = null;
        int distance = 0;
        String turn = null;
        String image = null;

        // Size -1 because we don't need directions to the first point
        String[][] returnString = new String[tiles.size() - 1][2];

        int x1, x2, y1, y2;
        try {
            // set initial coordinates once
            int previousCoordinateX = tiles.get(0).getCoordinateX();
            int previousCoordinateY = tiles.get(0).getCoordinateY();

            // Inittial orientation
            initOrientation(tiles.get(0).getCoordinateX(), tiles.get(1).getCoordinateX(), tiles.get(0).getCoordinateY(), tiles.get(1).getCoordinateY());

            for (int i = 1; i < tiles.size(); i++) {
                x1 = previousCoordinateX;
                x2 = tiles.get(i).getCoordinateX();
                y1 = previousCoordinateY;
                y2 = tiles.get(i).getCoordinateY();


                distance = measureDistance(x1, x2, y1, y2);
                try{
                    turn = getTurnWithoutOrientation(x1, x2, y1, y2);
                    image = turn;
                }
                catch(MCGAJunctionPointException e){

                }


                // Format looks like: Turn Right In 200U ; where 'U' is for Unit
                currentDirection = "Go " + turn + " In " + distance + "u";

                // Update 2D array
                returnString[i - 1][0] = currentDirection;
                returnString[i - 1][1] = image;

                // Update previous directions to the current ones
                previousCoordinateX = tiles.get(i).getCoordinateX();
                previousCoordinateY = tiles.get(i).getCoordinateY();
            }
        }catch(ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }
        catch(MCGAJunctionPointException e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }
        catch (Exception e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }

        return returnString;
    }

    /**
     * Returns directions for the user to take without orientation discriminator
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return
     * @throws MCGAJunctionPointException
     */
    private String getTurnWithoutOrientation(int x1, int x2, int y1, int y2) throws MCGAJunctionPointException{

        if ((x1 < x2) && (Math.abs(x1 - x2) > ERROR_TOLERANCE)){
            return "right";
        }

        else if ((x1 > x2) && (Math.abs(x1 - x2) > ERROR_TOLERANCE)){
            return "left";
        }

        else if ((y1 < y2) && (Math.abs(y1 - y2) > ERROR_TOLERANCE)){
            return "down";
        }

        else if ((y1 > y2) && (Math.abs(y1 - y2) > ERROR_TOLERANCE)){
            return "up";
        }
        else{
            throw new MCGAJunctionPointException("Invalid Orientation");
        }
    }


    /**
     *
     * @return images which should be shown to the user. Will display an arrow
     */
    private String getImage() throws MCGAJunctionPointException{

        // IDEALLY THIS SHOULD BE IN THE DATABASE
        switch(orientation){
            case NORTH_ORIENTATION:
                return "up";

            case SOUTH_ORIENTATION:
                return "down";

            case EAST_ORIENTATION:
                return "right";

            case WEST_ORIENTATION:
                return "left";

            case UNDEF_ORIENTATION:
                throw new MCGAJunctionPointException("Undefined point of reference exception");
        }
        return null;
    }


    /**
     * Tell user to turn left or right depending on the way they are facing
     * @param x1 previous x coordinate
     * @param x2 current x coordinate
     * @param y1 previous y coordinate
     * @param y2 current y coordinate
     * @return String 'Left' or 'Right'
     */
    private String getTurn(int x1, int x2, int y1, int y2){
        String turn;
        if (getNextOrientation(x1, x2, y1, y2) == 1){
            turn = "Right";
        }
        else{
            turn = "Left";
        }
        return turn;
    }

    /**
     * Measures distance to the next joint point
     * @param x1 previous x coordinate
     * @param x2 current x coordinate
     * @param y1 previous y coordinate
     * @param y2 current y coordinate
     * @return distance measured in undefined unit 'U'
     */
    private int measureDistance(int x1, int x2, int y1, int y2){

        if ((x2 != x1) && (Math.abs(x1 - x2) > ERROR_TOLERANCE)){
            return Math.abs(x1 - x2);
        }
        else if ((y2 != y1) && (Math.abs(y1 - y2) > ERROR_TOLERANCE)){
            return Math.abs(y1 - y2);
        }
        return 0;
    }


    /**
     * Will retrieve the next orientation of the user
     * @param x1 previous x coordinate
     * @param x2 current x coordinate
     * @param y1 previous y coordinate
     * @param y2 current y coordinate
     * @return 1 if the user must go right or 0 if he must go left at the join point
     */
    private int getNextOrientation(int x1, int x2, int y1, int y2){
        int goRight = 0;
        try {
            // going right
            if (x1 < x2) {
                // Facing north
                if (orientation == NORTH_ORIENTATION) {
                    goClockWise();
                    goRight = 1;
                }
                //Facing south
                else {
                    goCounterClockWise();
                }
            }

            // going left
            else if (x1 > x2) {
                // Facing north
                if (orientation == NORTH_ORIENTATION) {
                    goCounterClockWise();
                }
                //Facing south
                else {
                    goClockWise();
                    goRight = 1;
                }
            }

            // going down
            else if (y1 < y2) {
                // Facing East
                if (orientation == EAST_ORIENTATION) {
                    goClockWise();
                    goRight = 1;
                }
                // Facing West
                else {
                    goCounterClockWise();
                }
            }

            // going up
            else if (y1 > y2) {
                // Facing East
                if (orientation == EAST_ORIENTATION) {
                    goCounterClockWise();
                }
                // Facing West
                else {
                    goClockWise();
                    goRight = 1;
                }
            }
        }
        catch (MCGAJunctionPointException e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }
        return goRight;
    }

    /**
     * Changes the orientation of the user by 90 degrees clockwise
     */
    private void goClockWise() throws MCGAJunctionPointException{
        switch (orientation){
            case UNDEF_ORIENTATION:
                throw new MCGAJunctionPointException("Junction Point Invalid Exception");

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
        }
    }

    /**
     * changes the orientation of the user by 90 degrees counterclockwise
     */
    private void goCounterClockWise() throws MCGAJunctionPointException{
        switch (orientation){
            case UNDEF_ORIENTATION:
                throw new MCGAJunctionPointException("Junction Point Invalid Exception");

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
        }
    }

    /**
     * Since orientation is originally undefined, we need to define it for the first point
     * @param x1 previous x coordinate
     * @param x2 current x coordinate
     * @param y1 previous y coordinate
     * @param y2 current y coordinate
     */
    private void initOrientation(int x1, int x2, int y1, int y2) throws MCGAJunctionPointException{
        if (x1 < x2)
            orientation = EAST_ORIENTATION;

        else if (x1 > x2)
            orientation = WEST_ORIENTATION;

        else if (y1 < y2)
            orientation = SOUTH_ORIENTATION;

        else if (y1 > y2)
            orientation = NORTH_ORIENTATION;
        else{
            throw new MCGAJunctionPointException("Orientation is Undefined Exception");
        }
    }
}
