package com.concordia.mcga.activities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@Config(constants = BuildConfig.class, sdk = 22)
@RunWith(RobolectricTestRunner.class)
public class SplashActivityTest {

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        SplashActivity splashActivity = new SplashActivity();

    }

}