package com.concordia.mcga.fragments;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.adapters.DirectionsArrayAdapter;
import com.concordia.mcga.lib.BottomSheet;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DirectionsTest {
    // Layout elements
    private TextView bottomSheetTextView;
    private ListView list;

    // Custom ArrayList adapter
    private DirectionsArrayAdapter adapter;

    // Arrays store directions and image information
    private List<String> displayedDirectionsList =  new ArrayList<String>();
    private List<String> displayedDirectionsImage = new ArrayList<String>();

    // Arrays store directions and images that the user may view
    private List<String> completeDirectionsImage = new ArrayList<String>();
    private List<String> completeDirectionsList = new ArrayList<String>();

    // Counter keeps track of the index of the current direction
    private int currentDirection = 0;

    // View
    private View view;
    private View bottomSheet;
    private CoordinatorLayout coordinatorLayout;

    // Bottomsheet
    private BottomSheet behavior;

    // simple buttons
    private ImageButton nextButton, previousButton, expandButton;

    private BottomSheetDirectionsFragment myFragment;
    private ActivityController<MainActivity> controller;

    MarkerOptions markerOptions;
    Building testBuilding;
    Floor floor;

    private final static int FLOOR_NUMBER = 4;
    private final static int OUTDOOR_FLAG = 1, INDOOR_FLAG = 0;
    ///////////////////////////////////////////
    // SETUP FRAGMENT IN THE MAIN ACTIVITY
    ///////////////////////////////////////////


    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        myFragment = new BottomSheetDirectionsFragment();
        startFragment(myFragment, AppCompatActivity.class);
        markerOptions = new MarkerOptions();
        testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
        floor = new Floor(testBuilding, FLOOR_NUMBER);

        bottomSheetTextView = myFragment.getBottomSheetTextView();
        list = myFragment.getList();
        adapter = myFragment.getAdapter();
        displayedDirectionsList = myFragment.getDisplayedDirectionsList();
        displayedDirectionsImage = myFragment.getDisplayedDirectionsImage();
        completeDirectionsImage = myFragment.getCompleteDirectionsImage();
        completeDirectionsList = myFragment.getCompleteDirectionsList();
        currentDirection = myFragment.getCurrentDirection();
        view = myFragment.getView();
        bottomSheet = myFragment.getBottomSheet();
        coordinatorLayout = myFragment.getCoordinatorLayout();
        behavior = myFragment.getBehavior();
        nextButton = myFragment.getNextButton();
        previousButton = myFragment.getPreviousButton();
        expandButton = myFragment.getExpandButton();
    }

    @Test
    public void UIElements_ShouldNotBeNull_True(){
        assertNotNull(list);
        assertNotNull(myFragment);
        assertNotNull(adapter);
        assertNotNull(displayedDirectionsList);
        assertNotNull(displayedDirectionsImage);
        assertNotNull(completeDirectionsList);
        assertNotNull(completeDirectionsImage);
        assertNotNull(currentDirection);
        assertNotNull(view);
        assertNotNull(bottomSheet);
        assertNotNull(coordinatorLayout);
        assertNotNull(behavior);
        assertNotNull(nextButton);
        assertNotNull(previousButton);
        assertNotNull(expandButton);
    }


    //State Expanded = 4
    // State collapsed = 5
    @Test
    public void ClickBottomSheetButton_ExpandsWhenCollapsed_True(){
        myFragment.collapse();
        expandButton.performClick();
        assertEquals(myFragment.getState(), 4);
    }

    @Test
    public void ClickBottomSheetButton_CollapseWhenExpanded_True(){
        myFragment.expand();
        expandButton.performClick();
        assertEquals(myFragment.getState(), 5);
    }

    @Test
    public void ClickBottomSheetButton_ExpandsWhenExpanded_False(){
        myFragment.expand();
        expandButton.performClick();
        assertNotEquals(myFragment.getState(), 4);
    }

    @Test
    public void ClickBottomSheetButton_CollapseWhenCollapsed_False(){
        myFragment.collapse();
        expandButton.performClick();
        assertNotEquals(myFragment.getState(), 5);
    }

    @Test
    public void ClickNextDirectionButton_ShowsNextDirectionWhenAvailable_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Down", "down", INDOOR_FLAG, floor);
        myFragment.updateDirections();

        int index = myFragment.getCurrentDirection();
       List<String> direction = myFragment.getCompleteDirectionsList();

        assertTrue(direction.get(index).equals("Up"));

        nextButton.performClick();
        index = myFragment.getCurrentDirection();
        assertTrue(direction.get(index).equals("Down"));
    }

    @Test
    public void ClickNextDirectionButton_ShowNextDirectionWhenNotAvailable_False(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.updateDirections();

        int index = myFragment.getCurrentDirection();
        List<String> direction = myFragment.getCompleteDirectionsList();

        assertTrue(direction.get(index).equals("Up"));

        nextButton.performClick();
        int index1 = myFragment.getCurrentDirection();
        assertEquals(index, index1);
    }

    @Test
    public void ClickPreviousDirectionButton_ShowsPreviuousDirectionWhenAvailable_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Down", "down", INDOOR_FLAG, floor);
        myFragment.updateDirections();
        List<String> direction = myFragment.getCompleteDirectionsList();
        nextButton.performClick();
        int index = myFragment.getCurrentDirection();
        assertTrue(index == 1);
        previousButton.performClick();
        index = myFragment.getCurrentDirection();
        assertTrue(index == 0);
    }

    @Test
    public void ClickPreviousDirectionButton_ShowPreviousDirectionWhenNotAvailable_False(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.updateDirections();
        int index = myFragment.getCurrentDirection();
        List<String> direction = myFragment.getCompleteDirectionsList();
        previousButton.performClick();
        int index1 = myFragment.getCurrentDirection();
        assertEquals(index, index1);
    }

    @Test
    public void AddDirection_AddsDirectionToList_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.updateDirections();
        int index = myFragment.getCurrentDirection();
        List<String> array = myFragment.getCompleteDirectionsList();
        assertTrue(array.get(index).equals("Up"));

        myFragment.addDirection("Left", "left", INDOOR_FLAG, floor);
        myFragment.updateDirections();
        assertTrue(array.get(index + 1).equals("Left"));
    }

    @Test
    public void RemoveDirection_RemovesDirectionAtIndex_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Left", "left", INDOOR_FLAG, floor);
        myFragment.updateDirections();

        int index = myFragment.getCurrentDirection();
        List<String> array = myFragment.getCompleteDirectionsList();
        assertTrue(array.get(index).equals("Up"));

        myFragment.removeDirection(0);
        myFragment.updateDirections();

        assertTrue(array.get(index).equals("Left"));
    }

    @Test
    public void ClearDirections_SetsArrayToSize0_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Left", "left", INDOOR_FLAG, floor);
        myFragment.updateDirections();

        List<String> array = myFragment.getCompleteDirectionsList();
        List<String> array2 = myFragment.getCompleteDirectionsImage();
        assertTrue(array.size() > 0);
        assertTrue(array2.size() > 0);

        myFragment.clearDirections();

        assertTrue(array.size() == 0);
        assertTrue(array2.size() == 0);
        assertTrue(myFragment.getCurrentDirection() == 0);
    }

    @Test
    public void UpdateDirections_AddingDirectionsWithoutUpdating_False(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertFalse(array.size() > 0);
    }

    @Test
    public void UpdateDirections_AddingDirectionsWithUpdating_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Up", "up", FLOOR_NUMBER, floor);
        myFragment.updateDirections();
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertTrue(array.size() > 0);
    }

    @Test
    public void UpdateDirections_RemovingDirectionsWithoutUpdating_False(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.updateDirections();
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertTrue(array.size() > 0);

        myFragment.removeDirection(0);
        assertFalse(array.size() == 0);
    }

    @Test
    public void UpdateDirections_RemovingDirectionsWithUpdating_True(){
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.addDirection("Up", "up", INDOOR_FLAG, floor);
        myFragment.updateDirections();
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertTrue(array.size() > 0);

        myFragment.removeDirection(0);
        myFragment.updateDirections();

        assertTrue(array.size() == 0);
    }

    @Test
    public void UpdateDirections_FromList_True(){
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

        List<Floor> orderedFloors = new ArrayList<>();
        orderedFloors.add(floor);
        orderedFloors.add(floor);
        orderedFloors.add(floor);
        orderedFloors.add(floor);
        orderedFloors.add(floor);

        myFragment.addJunctionPoints(listTiles, orderedFloors);

        List<String> direction = myFragment.getCompleteDirectionsList();
        List<String> img = myFragment.getCompleteDirectionsList();

        assertNotEquals(direction.get(0), "Turn Left In 10u");
    }
}
