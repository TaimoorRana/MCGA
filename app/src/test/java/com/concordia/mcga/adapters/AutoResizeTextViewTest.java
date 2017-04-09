package com.concordia.mcga.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AutoResizeTextViewTest {
    Context context;
    AutoResizeTextView autoResizeTextView;
    TextView textView;
    View view;
    Activity controller;


    @Before
    public void setUp() throws Exception {
        LayoutInflater fakeInflater = Mockito.mock(LayoutInflater.class);
        textView = Mockito.mock(TextView.class);
        view = Mockito.mock(View.class);

        when(view.findViewById(R.id.customListView)).thenReturn(textView);

        when(fakeInflater.inflate(R.layout.building_information_fragment, null, false)).thenReturn(view);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        controller =  Robolectric.buildActivity(MainActivity.class).withIntent(intent).create().get();
        context = controller.getApplicationContext();
        autoResizeTextView = new AutoResizeTextView(context);
    }

    @Test
    public void Adapter_IsNotNull_True(){
        assertNotNull(autoResizeTextView);
        assertNotNull(textView);
        assertNotNull(view);
    }

    @Test
    public void setText_TextIsSet_True(){
        String text = "This is new text from setText with BufferType EDITABLE.";
        String setText;
        autoResizeTextView.setText(text,  TextView.BufferType.EDITABLE);
        setText = autoResizeTextView.getText().toString();
        assertTrue(text.equals(setText));
    }

    @Test
    public void setTextSize_SizeUpdated_True(){
        float size = 20;
        float readSize = 0;
        autoResizeTextView.setTextSize(size);
        readSize = autoResizeTextView.getTextSize();

        assertEquals(size, readSize);
    }

    @Test
    public void setTextLines_LinesUpdated_True(){
        int lines;
        autoResizeTextView.setMaxLines(3);
        lines = autoResizeTextView.getMaxLines();
        assertEquals(lines, 3);

        autoResizeTextView.setSingleLine();
        lines = autoResizeTextView.getMaxLines();
        assertEquals(1, lines);

        autoResizeTextView.setSingleLine(false);
        lines = autoResizeTextView.getMaxLines();
        assertEquals(-1, lines);

        autoResizeTextView.setLines(4);
        lines = autoResizeTextView.getMaxLines();
        assertEquals(4, lines);
    }

    @Test
    public void SetTextScaledPixels_TextSizeUpdated_True(){
        float textSize = autoResizeTextView.getTextSize();
        autoResizeTextView.setTextSize(20);
        autoResizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15.f);
        float newSize = autoResizeTextView.getTextSize();
        assertFalse(textSize == newSize);
    }

    @Test
    public void SizeTester_IsLowerThan0_True(){
        RectF mAvailableSpaceRect = new RectF();
        int height = 100;
        int width = 100;

        mAvailableSpaceRect.right = height;
        mAvailableSpaceRect.bottom = width;

        autoResizeTextView.setText("Text");

        // text size too small. Should always yield -1 because of the ontextchange listener
        int startSize = 100;
        int result = autoResizeTextView.getmSizeTester(startSize, mAvailableSpaceRect);
        assertEquals(result, -1);

    }
}
