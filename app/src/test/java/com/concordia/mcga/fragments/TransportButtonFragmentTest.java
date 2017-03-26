package com.concordia.mcga.fragments;

import com.akexorcist.googledirection.constant.TransportMode;
import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.shadows.ShadowOutdoorDirections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowOutdoorDirections.class})
public class TransportButtonFragmentTest {

    private TransportButtonFragment transportButtonFragment;

    @Before
    public void setUp() {
        transportButtonFragment = new TransportButtonFragment();
        SupportFragmentTestUtil.startVisibleFragment(transportButtonFragment);
    }

    @Test
    public void testOnCreate_initialization() {
        //FAB
        assertNotNull(transportButtonFragment.getTransportExpandFAB());
        assertNotNull(transportButtonFragment.getWalkFAB());
        assertNotNull(transportButtonFragment.getBikeFAB());
        assertNotNull(transportButtonFragment.getCarFAB());
        assertNotNull(transportButtonFragment.getPublicTransportFAB());
        assertNotNull(transportButtonFragment.getShuttleFAB());

        //Text View
        assertNotNull(transportButtonFragment.getWalkTextView());
        assertNotNull(transportButtonFragment.getBikeTextView());
        assertNotNull(transportButtonFragment.getBikeTextView());
        assertNotNull(transportButtonFragment.getCarTextView());
        assertNotNull(transportButtonFragment.getPublicTransportTextView());
        assertNotNull(transportButtonFragment.getShuttleTextView());

        //Anim
        assertNotNull(transportButtonFragment.getTransport_fab_open());
        assertNotNull(transportButtonFragment.getTransport_fab_close());
        assertNotNull(transportButtonFragment.getTransport_textview_open());
        assertNotNull(transportButtonFragment.getTransport_textview_close());

        assertTrue(transportButtonFragment.getTransportType() == TransportMode.TRANSIT);
    }

    @Test
    public void testTimeSet() {
        //Only one button is tested since they all have the same implementation
        transportButtonFragment.setBikeTime(1, 40);
        assertTrue(transportButtonFragment.getBikeTextView().getText().toString().equals("1h40m"));

        transportButtonFragment.setBikeTime(0, 40);
        assertTrue(transportButtonFragment.getBikeTextView().getText().toString().equals("40m"));

        transportButtonFragment.setBikeTime(5, 0);
        assertTrue(transportButtonFragment.getBikeTextView().getText().toString().equals("5h"));
    }

    @Test
    public void testTransportButtonToggle() {
        //Make sure its not expanded
        assertFalse(transportButtonFragment.isExpanded());

        //Expand it
        transportButtonFragment.toggle();

        assertTrue(transportButtonFragment.isExpanded());
        assertFalse(transportButtonFragment.getTransportExpandFAB().getTag().equals(R.drawable.ic_close_black_24dp));

        //Retract it again
        transportButtonFragment.toggle();
        assertFalse(transportButtonFragment.isExpanded());
    }

    @Test
    public void testDisableShuttleOption() {
        //Make sure it's initially active
        assertTrue(transportButtonFragment.isShuttleVisible());
        assertTrue(transportButtonFragment.getShuttleFAB().isClickable());

        //Disable it
        transportButtonFragment.disableShuttle(true);

        assertFalse(transportButtonFragment.isShuttleVisible());
        assertFalse(transportButtonFragment.getShuttleFAB().isClickable());
    }

    @Test
    public void testTransportOptionClick() {
        transportButtonFragment.getWalkFAB().performClick();
        assertTrue(transportButtonFragment.getTransportType().equals(TransportMode.WALKING));
    }

    @Test
    public void testDisplayAllTransportTime() {
        transportButtonFragment.displayAllTransportTimes();
        assertTrue(transportButtonFragment.getCarTextView().getText() == "1 minute");
        assertTrue(transportButtonFragment.getPublicTransportTextView().getText() == "2 minutes");
        assertTrue(transportButtonFragment.getBikeTextView().getText() == "3 minutes");
        assertTrue(transportButtonFragment.getWalkTextView().getText() == "4 minutes");
    }
}
