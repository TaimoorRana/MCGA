package com.concordia.mcga.fragments;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.location.LocationListener;
import android.location.LocationManager;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.factories.ShadowBitmapDescriptorFactory;
import com.google.android.gms.maps.GoogleMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.objectweb.asm.commons.Method;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.internal.mockcreation.MockCreator;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule ;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@PrepareForTest(NavigationFragment.class)
@RunWith(PowerMockRunner.class)
public class NavigationFragmentTest {


    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void alertGpsShouldNotBeFalse() throws Exception{
        //Varialbes

        Activity activity = mock(Activity.class);
        GoogleMap map = mock(GoogleMap.class);
        LocationManager gpsmanage = mock(LocationManager.class);
        LocationListener gpsListen = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        //Mock Behavior
        PowerMockito.mockStatic(NavigationFragment.class); // mock all static methods in NavigationFragment
        PowerMockito.when(NavigationFragment.alertGPS(activity)).thenReturn(true);
        PowerMockito.when(NavigationFragment.locateMe(map, activity, gpsmanage, gpsListen)).thenReturn(true);
        NavigationFragmentTest.execute();

        //execute

        //verify
        PowerMockito.verifyStatic(Mockito.times(2));
        NavigationFragment.alertGPS(activity);


    }

}
