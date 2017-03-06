package com.concordia.mcga.adapters;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class POISearchAdapterTest {
    POISearchAdapter searchAdapter;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(null);
        searchAdapter = new POISearchAdapter(context);
    }

    @Test
    public void getGroupCount() throws Exception {
        assertEquals(searchAdapter.getGroupCount(), 0);
    }

    @Test
    public void getChildrenCount() throws Exception {
        assertEquals(searchAdapter.getChildrenCount(POISearchAdapter.LOY_INDEX), 0);
        assertEquals(searchAdapter.getChildrenCount(POISearchAdapter.SGW_INDEX), 0);
    }


    @Test
    public void getGroupIsEmpty() throws Exception {
        assertTrue(searchAdapter.getGroupIsEmpty(POISearchAdapter.SGW_INDEX));
        assertTrue(searchAdapter.getGroupIsEmpty(POISearchAdapter.LOY_INDEX));
    }
}