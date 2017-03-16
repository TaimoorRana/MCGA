package com.concordia.mcga.adapters;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class POISearchAdapterTest {
    private POISearchAdapter searchAdapter;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(null);
        searchAdapter = new POISearchAdapter(context);
    }

    @Test
    public void getCounts() throws Exception {
        assertEquals(searchAdapter.getGroupCount(), 0);
    }

    @Test
    public void getGroupIsEmpty() throws Exception {
        searchAdapter.filterData("");
        assertEquals(searchAdapter.getGroupCount(), 0);
    }
}