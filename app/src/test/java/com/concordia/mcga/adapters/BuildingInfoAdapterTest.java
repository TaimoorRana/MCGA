package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

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
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class BuildingInfoAdapterTest {

    // Array Adapter
    private BuildingInformationArrayAdapter adapter = null;

    // List passed to the adapter
    private List<String[]> rowImages = new ArrayList<String[]>();

    private final int IMAGES_PER_ROW = 4;
    private String[] images = new String [IMAGES_PER_ROW];

    ListView fakeList;

    // Other variables
    Context context;
    ImageView[] imageView = new ImageView[4];
    ImageView fakeImage1, fakeImage2, fakeImage3, fakeImage4;
    View fakeGroupView, fakeChildView;


    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);

        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        fakeGroupView = Mockito.mock(View.class);
        fakeChildView = Mockito.mock(View.class);
        fakeList = Mockito.mock(ListView.class);
        fakeImage1 = Mockito.mock(ImageView.class);
        fakeImage2 = Mockito.mock(ImageView.class);
        fakeImage3 = Mockito.mock(ImageView.class);
        fakeImage4 = Mockito.mock(ImageView.class);

        when(fakeGroupView.findViewById(R.id.bottom_sheet)).thenReturn(fakeList);
        when(fakeChildView.findViewById(R.id.buildingImage1)).thenReturn(fakeImage1);
        when(fakeChildView.findViewById(R.id.buildingImage2)).thenReturn(fakeImage2);
        when(fakeChildView.findViewById(R.id.buildingImage3)).thenReturn(fakeImage3);
        when(fakeChildView.findViewById(R.id.buildingImage4)).thenReturn(fakeImage4);

        when(fakeInflater.inflate(R.layout.building_information_fragment, null, false)).thenReturn(fakeGroupView);
        when(fakeInflater.inflate(R.layout.building_info_list, null, false)).thenReturn(fakeChildView);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(fakeInflater);

        adapter = new BuildingInformationArrayAdapter(context, rowImages);
    }

    @Test
    public void Adapter_IsNotNull_True(){
        assertNotNull(adapter);
    }

    @Test
    public void AddingImages_UpdateArray_True(){
        images[0] = "up";
        images[1] = "up";
        images[2] = "up";
        images[3] = "up";

        rowImages.add(images);
        adapter.notifyDataSetChanged();
        List<String[]> dummyArray = adapter.getRowImages();
        assertTrue(dummyArray.get(0)[0].equals("up"));
    }

    @Test
    public void GetView_IsNotNull_True(){
        images[0] = "up";
        images[1] = "up";
        images[2] = "up";
        images[3] = "up";

        rowImages.add(images);
        adapter.notifyDataSetChanged();
        View view = adapter.getView(0, null, null);
        assertNotNull(view);
    }

    @Test
    public void GetView_IsEmpty_True(){
        List<String[]> array = adapter.getRowImages();
        assertTrue(array.size() == 0);
    }


}
