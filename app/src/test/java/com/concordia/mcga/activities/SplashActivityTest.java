package com.concordia.mcga.activities;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class SplashActivityTest {
    @Test
    public void splashScreenCreatedTest() {
        SplashActivity splashActivity = new SplashActivity();
        Assert.assertNotNull(splashActivity);
    }
}

