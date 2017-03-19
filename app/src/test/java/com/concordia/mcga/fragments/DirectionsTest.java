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
import com.concordia.mcga.lib.BuildingBottomSheetInfo;

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
@Config(constants = BuildConfig.class,sdk = 21)
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
    private BuildingBottomSheetInfo behavior;

    // simple buttons
    private ImageButton nextButton, previousButton, expandButton;

    private BottomSheetDirectionsFragment myFragment;
    private ActivityController<MainActivity> controller;

    ///////////////////////////////////////////
    // SETUP FRAGMENT IN THE MAIN ACTIVITY
    ///////////////////////////////////////////


    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        myFragment = new BottomSheetDirectionsFragment();
        startFragment(myFragment, AppCompatActivity.class);

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
    public void BottomSheetDirectionsFragment_ShouldNotBeNull_True(){
        assertNotNull(myFragment);
    }

    @Test
    public void list_ShouldNotBeNull_True(){
        assertNotNull(list);
    }

    @Test
    public void adapter_ShouldNotBeNull_True(){
        assertNotNull(adapter);
    }

    @Test
    public void DisplayedDirectionsList_ShouldNotBeNull_True(){
        assertNotNull(displayedDirectionsList);
    }

    @Test
    public void DisplayedDirectionsImage_ShouldNotBeNull_True(){
        assertNotNull(displayedDirectionsImage);
    }

    @Test
    public void CompleteDirectionsList_ShouldNotBeNull_True(){
        assertNotNull(completeDirectionsList);
    }

    @Test
    public void CompleteDirectionsImages_ShouldNotBeNull_True(){
        assertNotNull(completeDirectionsImage);
    }

    @Test
    public void CurrentDirection_ShouldNotBeNull_True(){
        assertNotNull(currentDirection);
    }

    @Test
    public void View_ShouldNotBeNull_True(){
        assertNotNull(view);
    }

    @Test
    public void BottomSheet_ShouldNotBeNull_True(){
        assertNotNull(bottomSheet);
    }

    @Test
    public void CoordinatorLayout_ShouldNotBeNull_True(){
        assertNotNull(coordinatorLayout);
    }

    @Test
    public void Behavior_ShouldNotBeNull_True(){
        assertNotNull(behavior);
    }

    @Test
    public void NextButton_ShouldNotBeNull_True(){
        assertNotNull(nextButton);
    }

    @Test
    public void PreviousButton_ShouldNotBeNull_True(){
        assertNotNull(previousButton);
    }

    @Test
    public void ExpandButton_ShouldNotBeNull_True(){
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
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Down", "down");
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
        myFragment.addDirection("Up", "up");
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
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Down", "down");
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
        myFragment.addDirection("Up", "up");
        myFragment.updateDirections();
        int index = myFragment.getCurrentDirection();
        List<String> direction = myFragment.getCompleteDirectionsList();
        previousButton.performClick();
        int index1 = myFragment.getCurrentDirection();
        assertEquals(index, index1);
    }

    @Test
    public void AddDirection_AddsDirectionToList_True(){
        myFragment.addDirection("Up", "up");
        myFragment.updateDirections();
        int index = myFragment.getCurrentDirection();
        List<String> array = myFragment.getCompleteDirectionsList();
        assertTrue(array.get(index).equals("Up"));

        myFragment.addDirection("Left", "left");
        myFragment.updateDirections();
        assertTrue(array.get(index + 1).equals("Left"));
    }

    @Test
    public void RemoveDirection_RemovesDirectionAtIndex_True(){
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Left", "left");
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
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Left", "left");
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
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Up", "up");
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertFalse(array.size() > 0);
    }

    @Test
    public void UpdateDirections_AddingDirectionsWithUpdating_True(){
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Up", "up");
        myFragment.updateDirections();
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertTrue(array.size() > 0);
    }

    @Test
    public void UpdateDirections_RemovingDirectionsWithoutUpdating_False(){
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Up", "up");
        myFragment.updateDirections();
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertTrue(array.size() > 0);

        myFragment.removeDirection(0);
        assertFalse(array.size() == 0);
    }

    @Test
    public void UpdateDirections_RemovingDirectionsWithUpdating_True(){
        myFragment.addDirection("Up", "up");
        myFragment.addDirection("Up", "up");
        myFragment.updateDirections();
        List<String> array = myFragment.getDisplayedDirectionsList();
        assertTrue(array.size() > 0);

        myFragment.removeDirection(0);
        myFragment.updateDirections();

        assertTrue(array.size() == 0);
    }
}
