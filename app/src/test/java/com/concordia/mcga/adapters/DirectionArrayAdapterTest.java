package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.View;

import com.concordia.mcga.activities.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
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
    public void Adapter_IsNull_True(){
        assertNotNull(adapter);
    }

    @Test
    public void GetView_IsNotNull_True(){
        View view = adapter.getView(0, null, null);
        assertNull(view);
    }
}
