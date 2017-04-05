package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.models.IndoorMapTile;

import java.util.List;

public class IndoorDirections {

    private static final int UNDEF_ORIENTATION = 0;
    private static final int NORTH_ORIENTATION = 1;
    private static final int EAST_ORIENTATION = 2;
    private static final int SOUTH_ORIENTATION = 3;
    private static final int WEST_ORIENTATION = 4;

    private int orientation = UNDEF_ORIENTATION;
    ///////////////
    // Main Code //
    ///////////////

    /**
     * Constructor
     */
    public IndoorDirections(){
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

            for (int i = 1; i < tiles.size(); i++) {
                x1 = previousCoordinateX;
                x2 = tiles.get(i).getCoordinateX();
                y1 = previousCoordinateY;
                y2 = tiles.get(i).getCoordinateY();

                turn = getTurn(x1, x2, y1, y2);
                distance = MeasureDistance(x1, x2, y1, y2);
                image = getImage();

                // Format looks like: Turn Right In 200U ; where 'U' is for Unit
                currentDirection = "Turn " + turn + " In " + distance + "U";

                // Update 2D array
                returnString[i - 1][0] = currentDirection;
                returnString[i - 1][1] = image;

                // Update previous directions to the current ones
                previousCoordinateX = tiles.get(i).getCoordinateX();
                previousCoordinateY = tiles.get(i).getCoordinateY();
            }
        }catch(Exception e){
            // Do nothing
        }

        return returnString;
    }

    /**
     *
     * @return images which should be shown to the user. Will display an arrow
     */
    private String getImage(){
        // IDEALLY THIS SHOULD BE IN THE DATABASE
        String img = null;
        switch(orientation){
            case NORTH_ORIENTATION:
                img = "up";
                break;

            case SOUTH_ORIENTATION:
                img = "down";
                break;

            case EAST_ORIENTATION:
                img = "right";
                break;

            case WEST_ORIENTATION:
                img = "left";
                break;

            default:
                break;
        }

        return img;
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
    private int MeasureDistance(int x1, int x2, int y1, int y2){
        int distance = 0;
        if (x2 < x1){
            distance = Math.abs(x1 - x2);

        }
        else if (x2 > x1){
            distance = Math.abs(x1 - x2);
        }

        else if (y2 < y1){
            distance = Math.abs(y1 - y2);
        }

        else if (y2 > y1){
            distance = Math.abs(y1 - y2);
        }
        else{
            // Straight line. Problem in junction points given
        }
        return distance;
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

        // going right
        if (x1 < x2){
            // Facing north
            if (orientation == NORTH_ORIENTATION){
                goClockWise();
                goRight = 1;
            }
            //Facing south
            else{
                goCounterClockWise();
            }
        }

        // going left
        else if (x1 > x2){
            // Facing north
            if (orientation == NORTH_ORIENTATION){
                goCounterClockWise();
            }
            //Facing south
            else{
                goClockWise();
                goRight = 1;
            }
        }

        // going down
        else if (y1 < y2){
            // Facing East
            if (orientation == EAST_ORIENTATION){
                goClockWise();
                goRight = 1;
            }
            // Facing West
            else{
                goCounterClockWise();
            }
        }

        // going up
        else if (y1 > y2){
            // Facing East
            if (orientation == EAST_ORIENTATION){
                goCounterClockWise();
            }
            // Facing West
            else{
                goClockWise();
                goRight = 1;
            }
        }
        else{
            //this should never happen
        }
        return goRight;
    }

    /**
     * Changes the orientation of the user by 90 degrees clockwise
     */
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

    /**
     * changes the orientation of the user by 90 degrees counterclockwise
     */
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
