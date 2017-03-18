package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.factories.ShadowBitmapDescriptorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = ShadowBitmapDescriptorFactory.class)
public class NavigationFragmentTest {
    private NavigationFragment fragment;

    @Before
    public void setUp() throws Exception {
        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        ViewGroup fakeContainer =  Mockito.mock(ViewGroup.class);
        View fakeToolbar = Mockito.mock(View.class);
        LinearLayoutCompat fakeLayout = Mockito.mock(LinearLayoutCompat.class);
        Bundle fakeBundle = Mockito.mock(Bundle.class);
        AppCompatImageButton fakeCancelButton = Mockito.mock(AppCompatImageButton.class);
        IndoorMapFragment fakeIndoors = Mockito.mock(IndoorMapFragment.class);

        Button fakeButton = Mockito.mock(Button.class);

        when(fakeLayout.findViewById(R.id.viewSwitchButton)).thenReturn(fakeButton);
        when(fakeLayout.findViewById(R.id.nav_toolbar)).thenReturn(fakeToolbar);
        when(fakeToolbar.findViewById(R.id.search_location_button)).thenReturn(fakeCancelButton);
        when(fakeToolbar.findViewById(R.id.search_destination_button)).thenReturn(fakeCancelButton);

        when(fakeInflater.inflate(R.layout.nav_main_fragment, fakeContainer, false)).thenReturn(fakeLayout);

        fragment = new NavigationFragment();
        ShadowLooper.pauseMainLooper();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();

        fragment.onCreateView(fakeInflater, fakeContainer, fakeBundle);


        //viewSwitchButton = (Button) parentLayout.findViewById(R.id.viewSwitchButton);
        SupportFragmentTestUtil.startFragment(fragment);

    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        assertNotNull(fragment);
        assertNotNull(fragment.getView());
    }

}
