package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
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
    public void getCounts() throws Exception {
        assertEquals(searchAdapter.getGroupCount(), 0);
        assertEquals(searchAdapter.getChildrenCount(POISearchAdapter.LOY_INDEX), 0);
        assertEquals(searchAdapter.getChildrenCount(POISearchAdapter.SGW_INDEX), 0);
    }

    @Test
    public void getGroupIsEmpty() throws Exception {
        searchAdapter.filterData("");
        assertTrue(searchAdapter.getGroupIsEmpty(POISearchAdapter.SGW_INDEX));
        assertTrue(searchAdapter.getGroupIsEmpty(POISearchAdapter.LOY_INDEX));
    }

    @Test
    public void getGroup() throws Exception {
        assertNull(searchAdapter.getGroup(0));
        assertNull(searchAdapter.getGroup(666));
        assertNull(searchAdapter.getGroup(-2));

        View view = searchAdapter.getGroupView(0, false, null, null);
        assertNull(view);
    }

    @Test
    public void getChild() throws Exception {
        assertNull(searchAdapter.getChild(0, 0));
        assertNull(searchAdapter.getChild(123, 321));
        assertNull(searchAdapter.getChild(-2, 0));

        View view = searchAdapter.getChildView(0, 0, false, null, null);
        assertNull(view);
    }
}