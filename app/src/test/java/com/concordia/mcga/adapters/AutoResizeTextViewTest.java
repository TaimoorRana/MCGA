package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AutoResizeTextViewTest {
    Context context;
    AutoResizeTextView autoResizeTextView;
    TextView textView;
    View view;

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        textView = Mockito.mock(TextView.class);
        view = Mockito.mock(View.class);

        when(view.findViewById(R.id.customListView)).thenReturn(textView);
        when(fakeInflater.inflate(R.layout.building_information_fragment, null, false)).thenReturn(view);
        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(fakeInflater);

        autoResizeTextView = new AutoResizeTextView(context);
    }

    @Test
    public void Adapter_IsNotNull_True(){
        assertNotNull(autoResizeTextView);
        assertNotNull(textView);
        assertNotNull(view);
    }

}
