package com.concordia.mcga.adapters;

import android.view.View;
import android.widget.TextView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.StudentSpot;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StudentSpotAdapterTest {
    private StudentSpotAdapter adapter;
    private final LatLng adapterLocation = new LatLng(1.0, 5.0);
    private final LatLng spotLocation = new LatLng(0.0, 0.0);

    @Before
    public void setUp() throws Exception {
        List<StudentSpot> fakeSpots = new ArrayList<>();
        StudentSpot fakeSpot = Mockito.mock(StudentSpot.class);
        when(fakeSpot.getMapCoordinates()).thenReturn(spotLocation);
        fakeSpots.add(fakeSpot);

        adapter = new StudentSpotAdapter(RuntimeEnvironment.application, fakeSpots, adapterLocation);
    }

    @Test
    public void testAdapterCreation() throws Exception {
        assertNotNull(adapter);
    }

    @Test
    public void testDistanceCalculation() throws Exception {
        View view = adapter.getView(0, null, null);
        TextView label = (TextView) view.findViewById(R.id.spotDistance);

        Method distanceStringer = StudentSpotAdapter.class.getDeclaredMethod("getFormattedDistance",
                LatLng.class, LatLng.class);
        distanceStringer.setAccessible(true);
        String expectedDistanceText = (String) distanceStringer.invoke(adapter, adapterLocation, spotLocation);

        assertEquals(expectedDistanceText, label.getText());
    }

}