package com.concordia.mcga.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.models.Room.RoomIcon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class POISearchAdapterTest {
    private POISearchAdapter searchAdapter;
    private Campus fakeSgw, fakeLoyola;
    private Building fakeH, fakeX;
    private String fakeHNum, fakeSNum;
    private View fakeEmptyView;

    @Before
    public void setUp() throws Exception {
        // Fake context
        Context context;

        // Fake rooms
        Room fakeR, fakeS;

        context = Mockito.mock(Context.class);
        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        View fakeGroupView = Mockito.mock(View.class);
        View fakeChildView = Mockito.mock(View.class);
        fakeEmptyView = Mockito.mock(View.class);
        CircleImageView fakeImage = Mockito.mock(CircleImageView.class);
        TextView fakeText = Mockito.mock(TextView.class);

        when(fakeGroupView.findViewById(R.id.groupImage)).thenReturn(fakeImage);
        when(fakeGroupView.findViewById(R.id.groupText)).thenReturn(fakeText);
        when(fakeChildView.findViewById(R.id.childText)).thenReturn(fakeText);

        when(fakeInflater.inflate(R.layout.poi_search_group_row, null)).thenReturn(fakeGroupView);
        when(fakeInflater.inflate(R.layout.poi_search_child_row, null)).thenReturn(fakeChildView);
        when(fakeInflater.inflate(R.layout.poi_search_empty_row, null)).thenReturn(fakeEmptyView);
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

        fakeHNum = "123";
        fakeSNum = "456";
        fakeS = new Room(new LatLng(0.0, 0.0), "FakeSRoom", new IndoorMapTile(0, 0), fakeSNum, 1, new ArrayList<LatLng>(), RoomIcon.NONE);
        fakeR = new Room(new LatLng(0.0, 0.0), "FakeR", new IndoorMapTile(0, 0), fakeHNum, 1, new ArrayList<LatLng>(),
            RoomIcon.NONE);

        // Use reflection to add room to building
        Field field = Building.class.getDeclaredField("rooms");
        field.setAccessible(true);
        field.set(fakeH, Arrays.asList(fakeR));
        field.set(fakeX, Arrays.asList(fakeS));

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

        assertEquals(searchAdapter.getChildrenCount(1), 1);
        assertNotNull(searchAdapter.getGroup(1));
        assertEquals(searchAdapter.getChild(1, 0), fakeH);

        assertEquals(searchAdapter.getGroupCount(), 2);

        searchAdapter.filterData("X");

        assertEquals(searchAdapter.getChildrenCount(1), 1);
        assertNotNull(searchAdapter.getGroup(1));
        assertEquals(searchAdapter.getChild(1, 0), fakeX);

        assertEquals(searchAdapter.getGroupCount(), 2);

        searchAdapter.filterData("");
        assertEquals(searchAdapter.getGroupCount(), 0);
    }

    @Test
    public void checkViews() throws Exception {
        searchAdapter.filterData("SGW");
        View view = searchAdapter.getGroupView(1, false, null, null);
        assertNotNull(view);

        searchAdapter.filterData("LOY");
        view = searchAdapter.getGroupView(1, false, null, null);
        assertNotNull(view);

        view = searchAdapter.getChildView(1, 0, false, null, null);
        assertNotNull(view);
    }

    @Test
    public void checkRooms() throws Exception {
        searchAdapter.filterData("FakeR");
        View view = searchAdapter.getGroupView(1, false, null, null);
        assertNotNull(view);

        Room room = (Room)searchAdapter.getChild(1, 0);
        assertEquals(room.getRoomNumber(), fakeHNum);

        searchAdapter.filterData("FakeSRoom");
        View view2 = searchAdapter.getGroupView(1, false, null, null);
        assertNotNull(view2);

        Room room2 = (Room)searchAdapter.getChild(1, 0);
        assertEquals(room2.getRoomNumber(), fakeSNum);

    }

    @Test
    public void checkMyLocationItem() throws Exception {
        searchAdapter.filterData("location");
        View view = searchAdapter.getGroupView(0, false, null, null);
        assertNotNull(view);

        View placeholderView = searchAdapter.getChildView(0, 0, false, null, null);
        assertEquals(placeholderView, fakeEmptyView);
    }

    @Test
    public void checkIsChildSelectable() throws Exception {
        boolean selectable = searchAdapter.isChildSelectable(0, 0);
        assertTrue(selectable);
    }
}
