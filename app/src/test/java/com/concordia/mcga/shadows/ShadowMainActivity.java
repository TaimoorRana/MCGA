package com.concordia.mcga.factories;

import android.content.Context;

import com.concordia.mcga.activities.MainActivity;

import org.mockito.Mockito;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(MainActivity.class)
public class ShadowMainActivity {
    @Implementation
    public static Context getContext(){
        Context context = Mockito.mock(Context.class);
        return context;
    }
}
