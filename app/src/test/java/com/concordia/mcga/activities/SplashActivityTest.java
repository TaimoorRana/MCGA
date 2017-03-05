package com.concordia.mcga.activities;
import android.os.Build;
import android.view.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;


@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class SplashActivityTest {

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        SplashActivity activity = Robolectric.setupActivity(SplashActivity.class);
        View imageView = activity.findViewById(R.id.imageView);
        assertNotNull(imageView);
    }

}