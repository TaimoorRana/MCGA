package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class BuildingInfoAdapterTest {

    // Array Adapter
    private BuildingInformationArrayAdapter adapter = null;

    // List passed to the adapter
    private ArrayList<String[]> rowImages = new ArrayList<String[]>();

    private final int IMAGES_PER_ROW = 4;
    private String[] images = new String [IMAGES_PER_ROW];

    // Other variables
    Context context;
    ImageView[] imageView = new ImageView[4];


    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(null);
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
        ArrayList<String[]> dummyArray = adapter.getRowImages();
        assertTrue(dummyArray.get(0)[0].equals("up"));
    }

    @Test
    public void GetView_IsNull_True(){
        View view = adapter.getView(0, null, null);
        assertNull(view);
    }


}
