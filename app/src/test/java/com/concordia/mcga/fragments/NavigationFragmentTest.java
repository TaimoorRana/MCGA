package com.concordia.mcga.fragments;

// Junit Import
import android.support.design.BuildConfig;
import android.app.Activity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static org.junit.Assert.*;


@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)

public class NavigationFragmentTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testIfGPSOn() throws Exception{

    }

    @Test
    public void locateMe_locatesMe() throws Exception { //Problematic void that takes no argument and return nothing

    }


}

