package com.concordia.mcga.fragments;


import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.adapters.BuildingInformationArrayAdapter;
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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;



@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BuildingInfoTest {
    MainActivity mainActivity;
    private BuildingBottomSheetInfo behavior = null;
    private ActivityController<MainActivity> controller;
    private final int IMAGES_PER_ROW = 4;

    // UI elements
    private ImageButton expandButton = null;
    private TextView bottom_sheet_title = null, address = null, closingTime = null, openingTime = null;
    private ListView list = null;


    // Main view
    private View view = null;

    // Bottom sheet view
    private CoordinatorLayout coordinatorLayout = null;
    private View bottomSheet = null;

    // The fragment itself
    private BottomSheetBuildingInfoFragment myFragment;
    private BuildingInformationArrayAdapter adapter;

    ///////////////////////////////////////////
    // SETUP FRAGMENT IN THE MAIN ACTIVITY
    ///////////////////////////////////////////


    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        myFragment = new BottomSheetBuildingInfoFragment();
        startFragment(myFragment, AppCompatActivity.class);

        // Shadow UI
        expandButton = myFragment.getExpandButton();
        bottom_sheet_title = myFragment.getBottom_sheet_title();
        address = myFragment.getAddress();
        closingTime = myFragment.getClosingTime();
        openingTime = myFragment.getOpeningTime();
        list = myFragment.getList();
        view = myFragment.getView();
        coordinatorLayout = myFragment.getCoordinatorLayout();
        bottomSheet = myFragment.getBottomSheet();
        adapter = myFragment.getAdapter();
    }


    ///////////////////////////////////////////
    // MAKE SURE ALL UI ELEMENTS ARE PRESENT
    ///////////////////////////////////////////


    @Test
    public void BottomSheetBuildingInfoFragment_ShouldNotBeNull_True(){
        assertNotNull(myFragment);
    }

    @Test
    public void ExpandButton_ShouldNotBeNull_True(){
        assertNotNull(expandButton);
    }

    @Test
    public void Address_ShouldNotBeNull_True(){
        assertNotNull(address);
    }

    @Test
    public void ClosingTime_ShouldNotBeNull_True(){
        assertNotNull(closingTime);
    }

    @Test
    public void OpeningTime_ShouldNotBeNull_True(){
        assertNotNull(openingTime);
    }

    @Test
    public void List_ShouldNotBeNull_True(){
        assertNotNull(list);
    }

    @Test
    public void View_ShouldNotBeNull_True(){
        assertNotNull(view);
    }

    @Test
    public void CoordinatorLayout_ShouldNotBeNull_True(){
        assertNotNull(coordinatorLayout);
    }

    @Test
    public void BottomSheet_ShouldNotBeNull_True(){
        assertNotNull(bottomSheet);
    }

    @Test
    public void Adapter_ShouldNotBeNull_True(){
        assertNotNull(adapter);
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
    public void SetBuildingInformation_StringsEqualsInputs_True(){
        String s1, s2, s3, s4;
        s1 = "a";
        s2 = "b";
        s3 = "c";
        s4 = "d";

        myFragment.setBuildingInformation(s1, s2, s3, s4);

        s3 = "Opening Hours: " + s3;
        s4 = "Closing Hours: " + s4;
        assertTrue(s1.equals(myFragment.getBottom_sheet_title().getText().toString()));
        assertTrue(s2.equals(myFragment.getAddress().getText().toString()));
        assertTrue(s3.equals(myFragment.getOpeningTime().getText().toString()));
        assertTrue(s4.equals(myFragment.getClosingTime().getText().toString()));
    }

    @Test
    public void isExpanded_StateIsEqual4_True(){
        myFragment.expand();
        assertEquals(4, myFragment.getState());
    }

    @Test
    public void isCollapsed_StateIsEqual5_True(){
        myFragment.collapse();
        assertEquals(5, myFragment.getState());
    }

    @Test
    public void AddImage_ArrayUpdated_True(){
        List<String[]> array = new ArrayList<String[]>();
        String[] someImages = new String[IMAGES_PER_ROW];

        someImages[0] = "up";
        someImages[1] = "up";
        someImages[2] = "up";
        someImages[3] = "up";


        myFragment.addImages(someImages);
        array = myFragment.getRowImages();
        assertEquals(array.get(0)[0], "up");
    }

    @Test
    public void UpdateImages_ArrayUpdated_True(){
        List<String[]> array = new ArrayList<String[]>();
        String[] someImages = new String[IMAGES_PER_ROW];

        someImages[0] = "up";
        someImages[1] = "up";
        someImages[2] = "up";
        someImages[3] = "up";
        myFragment.addImages(someImages);

        myFragment.updateImageRow();
        array = myFragment.getRowImages();
        assertNotNull(array.get(0));
    }

    @Test public void ClearImages_ArrayIsSize0_True(){
        List<String[]> array = new ArrayList<String[]>();

        String[] someImages = new String[IMAGES_PER_ROW];
        someImages[0] = "up";
        someImages[1] = "up";
        someImages[2] = "up";
        someImages[3] = "up";

        myFragment.addImages(someImages);
        myFragment.updateImageRow();
        myFragment.clear();

        array = myFragment.getRowImages();
        assertEquals(array.size(), 0);
    }
}
