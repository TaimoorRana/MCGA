package com.concordia.mcga.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.fragments.BottomSheetBuildingInfoFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import static android.support.v7.appcompat.R.styleable.View;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.robolectric.util.FragmentTestUtil.startFragment;

/**
 * Created by root on 3/12/17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class BuildingInfoAdapterTest {

    // Array Adapter
    private BuildingInformationArrayAdapter adapter = null;

    // List passed to the adapter
    private ArrayList<String> images = new ArrayList<String>();


    // Other variables
    Context context;
    ImageView[] imageView = new ImageView[4];


    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(null);
        adapter = new BuildingInformationArrayAdapter(context, images);
    }

    @Test
    public void Adapter_IsNotNull_True(){
        assertNotNull(adapter);
    }

    @Test
    public void AddingImages_UpdateArray_True(){
        images.add("up");
        adapter.notifyDataSetChanged();
        ArrayList<String> dummyArray = adapter.getInformation();
        assertTrue(dummyArray.get(0).equals("up"));
    }

}
