package com.concordia.mcga.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
/**
 * Created by root on 3/11/17.
 */

public class InstrumentationBuildingInfoTest {
    MainActivity mainActivity;
    private ActivityController<MainActivity> controller;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
    }

    // Activity creation that allows intent extras to be passed in
    private void createWithIntent(String extra) {
        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra("activity_extra", extra);
        mainActivity = controller
                .withIntent(intent)
                .create()
                .start()
                .resume()
                .visible()
                .get();
    }

    @Test
    public void BottomSheetBuildingInfoFragment_ShouldNotBeNull_True(){
        BottomSheetBuildingInfoFragment myFragment = new BottomSheetBuildingInfoFragment();
        startFragment(myFragment, AppCompatActivity.class);
        assertNotNull(myFragment);
    }


}
