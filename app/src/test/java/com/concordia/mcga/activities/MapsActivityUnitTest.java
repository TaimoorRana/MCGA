package com.concordia.mcga.activities;

import com.concordia.mcga.models.Campus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapsActivityUnitTest {
    @Spy
    MapsActivity activity;

    @Test
    public void testSwitchCampus_switchedToLoyola() throws Exception {
        // Test data
        activity.currentCampus = Campus.SGW;

        // Mock
        Mockito.doNothing().when(activity).updateCampus();

        // Execute
        activity.switchCampus(null);

        // Verify
        Assert.assertEquals(Campus.LOYOLA, activity.currentCampus);
        Mockito.verify(activity).updateCampus();
    }

    @Test
    public void testSwitchCampus_switchedToSGW() throws Exception {
        // Test data
        activity.currentCampus = Campus.LOYOLA;

        // Mock
        Mockito.doNothing().when(activity).updateCampus();

        // Execute
        activity.switchCampus(null);

        // Verify
        Assert.assertEquals(Campus.SGW, activity.currentCampus);
        Mockito.verify(activity).updateCampus();
    }
}
