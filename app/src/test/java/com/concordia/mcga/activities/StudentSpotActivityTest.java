package com.concordia.mcga.activities;

import android.app.Activity;

import com.concordia.mcga.models.StudentSpot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StudentSpotActivityTest {
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        ActivityController<StudentSpotActivity> activityController = Robolectric.buildActivity(
                StudentSpotActivity.class).create();
        activity = activityController.get();
    }

    @Test
    public void testCreatedSuccessfully() throws Exception {
        assertNotNull(activity);
    }
}