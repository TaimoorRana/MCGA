package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)

public class DirectionArrayAdapterTest {
    // Array Adapter
    private DirectionsArrayAdapter adapter = null;
    Context context;
    private List<String> directionsText = new ArrayList<String>();
    private List<String> directionsImage = new ArrayList<String>();
    View fakeGroupView, fakeChildView;
    ListView fakeList;
    ImageView fakeImage;
    TextView fakeText;

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);

        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        fakeGroupView = Mockito.mock(View.class);
        fakeChildView = Mockito.mock(View.class);
        fakeList = Mockito.mock(ListView.class);
        fakeImage = Mockito.mock(ImageView.class);
        fakeText = Mockito.mock(TextView.class);

        when(fakeGroupView.findViewById(R.id.list1)).thenReturn(fakeList);
        when(fakeChildView.findViewById(R.id.customListView)).thenReturn(fakeText);
        when(fakeChildView.findViewById(R.id.imageView)).thenReturn(fakeImage);

        when(fakeInflater.inflate(R.layout.bottom_sheet_content, null, false)).thenReturn(fakeGroupView);
        when(fakeInflater.inflate(R.layout.list_text, null, false)).thenReturn(fakeChildView);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(fakeInflater);

        adapter = new DirectionsArrayAdapter(context, directionsText, directionsImage);
    }

    @Test
    public void Adapter_IsNull_True(){
        assertNotNull(adapter);
    }

    @Test
    public void GetView_IsNotNull_True(){
        directionsText.add("up");
        directionsImage.add("up");
        adapter.notifyDataSetChanged();

        directionsText.add("down");
        directionsImage.add("down");
        adapter.notifyDataSetChanged();

        directionsText.add("left");
        directionsImage.add("left");
        adapter.notifyDataSetChanged();

        directionsText.add("right");
        directionsImage.add("right");
        adapter.notifyDataSetChanged();

        View view = adapter.getView(0, null, null);
        assertNotNull(view);
    }
}
