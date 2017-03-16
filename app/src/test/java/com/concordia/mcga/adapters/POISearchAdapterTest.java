package com.concordia.mcga.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Constructor;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class POISearchAdapterTest {
    private POISearchAdapter searchAdapter;
    private Context context;
    private Campus fakeSgw, fakeLoyola;
    private Building fakeH, fakeX;

    @Before
    public void setUp() throws Exception {
        // Fake context
        context = Mockito.mock(Context.class);
        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        View fakeGroupView = Mockito.mock(View.class);
        View fakeChildView = Mockito.mock(View.class);
        CircleImageView fakeImage = Mockito.mock(CircleImageView.class);
        TextView fakeText = Mockito.mock(TextView.class);

        when(fakeGroupView.findViewById(R.id.groupImage)).thenReturn(fakeImage);
        when(fakeGroupView.findViewById(R.id.groupText)).thenReturn(fakeText);
        when(fakeChildView.findViewById(R.id.childText)).thenReturn(fakeText);

        when(fakeInflater.inflate(R.layout.poi_search_group_row, null)).thenReturn(fakeGroupView);
        when(fakeInflater.inflate(R.layout.poi_search_child_row, null)).thenReturn(fakeChildView);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(fakeInflater);

        // Use a bit of reflection to get access to the private constructor
        Constructor<Campus> constructor;
        constructor = Campus.class.getDeclaredConstructor(LatLng.class, String.class, String.class);
        constructor.setAccessible(true);

        // Initialize the fake objects
        fakeSgw = constructor.newInstance(new LatLng(0.0, 0.0), "FakeSGW", "FSGW");
        fakeH = new Building(new LatLng(0.0, 0.0), "FakeH", "FH", new MarkerOptions());
        fakeSgw.addBuilding(fakeH);
        fakeLoyola = constructor.newInstance(new LatLng(0.0, 0.0), "FakeLOY", "FLOY");
        fakeX = new Building(new LatLng(0.0, 0.0), "FakeX", "FX", new MarkerOptions());
        fakeLoyola.addBuilding(fakeX);

        searchAdapter = new POISearchAdapter(context, fakeSgw, fakeLoyola);
    }

    @Test
    public void checkValidIds() throws Exception {
        assertTrue(searchAdapter.hasStableIds());

        int position = 12;
        int second_position = 34;

        assertEquals(searchAdapter.getChildId(position, second_position), (long)second_position);
        assertEquals(searchAdapter.getGroupId(position), (long)position);
    }

    @Test
    public void checkFilter() throws Exception {
        searchAdapter.filterData("H");

        assertEquals(searchAdapter.getChildrenCount(0), 1);
        assertNotNull(searchAdapter.getGroup(0));
        assertEquals(searchAdapter.getChild(0, 0), fakeH);

        assertEquals(searchAdapter.getGroupCount(), 1);

        searchAdapter.filterData("X");

        assertEquals(searchAdapter.getChildrenCount(0), 1);
        assertNotNull(searchAdapter.getGroup(0));
        assertEquals(searchAdapter.getChild(0, 0), fakeX);

        assertEquals(searchAdapter.getGroupCount(), 1);

        searchAdapter.filterData("");
        assertEquals(searchAdapter.getGroupCount(), 0);
    }

    @Test
    public void checkViews() throws Exception {
        searchAdapter.filterData("SGW");
        View view = searchAdapter.getGroupView(0, false, null, null);
        assertNotNull(view);

        searchAdapter.filterData("LOY");
        view = searchAdapter.getGroupView(0, false, null, null);
        assertNotNull(view);

        view = searchAdapter.getChildView(0, 0, false, null, null);
        assertNotNull(view);
    }
}