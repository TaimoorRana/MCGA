package com.concordia.mcga.activities;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;


@Config(constants = BuildConfig.class, sdk = 22)
@RunWith(RobolectricTestRunner.class)
public class SplashActivityTest {

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        SplashActivity activity = Robolectric.setupActivity(SplashActivity.class);
        View imageView = activity.findViewById(R.id.imageView);
        assertNotNull(imageView);
    }

}