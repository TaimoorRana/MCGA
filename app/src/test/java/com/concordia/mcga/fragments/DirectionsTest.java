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
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startFragment;

/**
 * Created by Charmander on 3/12/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class DirectionsTest {
    // Layout elements
    private TextView bottomSheetTextView;
    private ListView list;

    // Custom ArrayList adapter
    private DirectionsArrayAdapter adapter;

    // Arrays store directions and image information
    private ArrayList<String> displayedDirectionsList =  new ArrayList<String>();
    private ArrayList<String> displayedDirectionsImage = new ArrayList<String>();

    // Arrays store directions and images that the user may view
    private ArrayList<String> completeDirectionsImage = new ArrayList<String>();
    private ArrayList<String> completeDirectionsList = new ArrayList<String>();

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

}
