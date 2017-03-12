package com.concordia.mcga.adapters;

import android.content.Context;

import com.concordia.mcga.activities.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by Charmander on 3/12/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class DirectionArrayAdapterTest {
    // Array Adapter
    private DirectionsArrayAdapter adapter = null;
    Context context;
    private ArrayList<String> directionsText = new ArrayList<String>();
    private ArrayList<String> directionsImage = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(null);
        adapter = new DirectionsArrayAdapter(context, directionsText, directionsImage);
    }

    @Test
    public void Adapter_IsNotNull_True(){
        assertNotNull(adapter);
    }
}
