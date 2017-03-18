package com.concordia.mcga.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import java.util.Vector;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class BuildingBottomSheetInfoTest {

    BuildingBottomSheetInfo bottomSheet;
    Activity controller;
    Context context;
    View fakeGroupView, fakeChildView;
    CoordinatorLayout fakeCoordinatorLayout;
    ListView fakeList;
    View view;
    @Before
    public void setUp() {
        view = Mockito.mock(View.class);
        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);

        fakeCoordinatorLayout = Mockito.mock(CoordinatorLayout.class);
        fakeGroupView = Mockito.mock(View.class);
        fakeChildView = Mockito.mock(View.class);
        fakeList = Mockito.mock(ListView.class);

        when(fakeInflater.inflate(R.layout.building_information_fragment, null, false)).thenReturn(fakeGroupView);
        when(fakeInflater.inflate(R.layout.building_information_fragment, null, false)).thenReturn(fakeChildView);
        when(fakeGroupView.findViewById(R.id.list1)).thenReturn(fakeList);
        when(fakeGroupView.findViewById(R.id.coordinatorlayout)).thenReturn(fakeCoordinatorLayout);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        controller =  Robolectric.buildActivity(MainActivity.class).withIntent(intent).create().get();;
        context = controller.getApplicationContext();
        bottomSheet = new BuildingBottomSheetInfo(context, null);
    }

    @Test
    public void BottomSheet_ShouldNotBeNull_True(){
        assertNotNull(bottomSheet);
    }

    @Test
    public void savedState_shouldUpdate_True(){
        bottomSheet.setState(BuildingBottomSheetInfo.STATE_ANCHOR_POINT);
        int state = bottomSheet.getState();
        Parcelable mState = bottomSheet.onSaveInstanceState(fakeCoordinatorLayout, fakeChildView);
        int state2 = bottomSheet.getState();

        assertTrue(state == state2);

        bottomSheet.onRestoreInstanceState(fakeCoordinatorLayout, fakeChildView, mState);

        int state3 = bottomSheet.getmLastStableState();

        assertTrue(state2 == state3);

        bottomSheet.setState(BuildingBottomSheetInfo.STATE_HIDDEN);
        state2 = bottomSheet.getState();
        state3 = bottomSheet.getmLastStableState();
        assertTrue(state2 == state3);
    }

    @Test
    public void CoordinatorLayout_ShouldNotBeNull_True(){
        assertNotNull(fakeCoordinatorLayout);
    }

    @Test
    public void onLayoutchild_ShouldNotBeNull_True(){
        assertTrue(bottomSheet.onLayoutChild(fakeCoordinatorLayout, fakeChildView, 0));
    }


    @Test
    public void touchEvent_isDetected_False(){
        MotionEvent event = MotionEvent.obtain(200, 300, MotionEvent.ACTION_DOWN, 15.0f, 10.0f, 0);
        fakeChildView.setVisibility(View.INVISIBLE);
        event = MotionEvent.obtain(200, 300, MotionEvent.ACTION_UP, 15.0f, 10.0f, 0);
        assertFalse(bottomSheet.onTouchEvent(fakeCoordinatorLayout, fakeChildView, event));

    }

    @Test
    public void scrollVelocity_isUpdatedAfterEvent_True(){
        int[] consumed = {0, 300};
        bottomSheet.onNestedPreScroll(fakeCoordinatorLayout, fakeChildView, view, 0, 0, consumed);
        assertTrue(bottomSheet.getmNestedScrolled());
    }

    @Test
    public void mNestedScroll_isFalseWhenNotScrolling_True(){
        bottomSheet.onStopNestedScroll(fakeCoordinatorLayout, fakeChildView, view);
        assertFalse(bottomSheet.getmNestedScrolled());
    }

    @Test
    public void setPeekHeight_updatesHeightAndOffset_true(){
        bottomSheet.setPeekHeight(700);
        assertTrue(bottomSheet.getPeekHeight() == 700);
        assertTrue(bottomSheet.getmMaxOffset() == fakeCoordinatorLayout.getHeight() - 700);
    }

    @Test
    public void addBottomSheetCallBack_UpdateBottomSheetCallBack_True(){
        bottomSheet.addBottomSheetCallback(null);
        Vector<BuildingBottomSheetInfo.BottomSheetCallback> vector = bottomSheet.getmCallBack();
        assertTrue(vector.get(0) == null);

        bottomSheet.addBottomSheetCallback(new BuildingBottomSheetInfo.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BuildingBottomSheetInfo.STATE_COLLAPSED:
                        break;

                    case BuildingBottomSheetInfo.STATE_EXPANDED:
                        break;

                    default:
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        assertTrue(vector.get(1) != null);
    }

    @Test
    public void setState_UpdatesTopPositionOfbottomSheet_true(){
        int top, offset;
        bottomSheet.setState(BuildingBottomSheetInfo.STATE_COLLAPSED);
        top = bottomSheet.getTop_position();
        offset = bottomSheet.getmMaxOffset();
        assertTrue(top == offset);

        bottomSheet.setState(BuildingBottomSheetInfo.STATE_EXPANDED);
        top = bottomSheet.getTop_position();
        offset = bottomSheet.getmMinOffset();
        assertTrue(top == offset);
    }
}
