package com.concordia.mcga.utilities.pathfinding;


import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.models.IndoorMapTile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class IndoorDirectionsTest {

    IndoorDirections indoorDirections;

    @Before
    public void setUp() {
        indoorDirections = new IndoorDirections();
    }

    @Test
    public void ObtainDirections_True(){
        IndoorMapTile tile1 = new IndoorMapTile(0, 0);
        IndoorMapTile tile2 = new IndoorMapTile(10, 0);
        IndoorMapTile tile3 = new IndoorMapTile(10, 12);
        IndoorMapTile tile4 = new IndoorMapTile(16, 12);
        IndoorMapTile tile5 = new IndoorMapTile(5, 32);

        List<IndoorMapTile> listTiles = new ArrayList<IndoorMapTile>();
        listTiles.add(tile1);
        listTiles.add(tile2);
        listTiles.add(tile3);
        listTiles.add(tile4);
        listTiles.add(tile5);

        IndoorDirections indoorDirections = new IndoorDirections();
        String[][] stringArray;
        stringArray = indoorDirections.getDirections(listTiles);

        assertFalse(stringArray[0][0].equals("Turn Left In 10u"));
        assertFalse(stringArray[1][0].equals("Turn Left In 12u"));
        assertFalse(stringArray[1][1].equals("left"));
        assertFalse(stringArray[2][0].equals("Turn Left In 6u"));
        assertFalse(stringArray[2][1].equals("down"));
        assertFalse(stringArray[3][0].equals("Turn Right In 11u"));
        assertTrue(stringArray[3][1].equals("left"));
    }

}
