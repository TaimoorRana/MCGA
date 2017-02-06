package com.concordia.mcga.activities;

import android.app.Fragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransportButtonFragmentUnitTest {
    @Spy
    TransportButtonFragment fragment;

    @Test
    public void testButtonExpansion_expansionCycle() throws Exception {
        /*Expand*/
        Assert.assertEquals(false, fragment.isExpanded());

        // Execute
        fragment.expandFAB();
        Mockito.verify(fragment).expandFAB();

        // Verify
        Assert.assertEquals(true, fragment.isExpanded());

        /*Collapse*/
        // Execute
        fragment.expandFAB();
        Mockito.verify(fragment).expandFAB();

        // Verify
        Assert.assertEquals(false, fragment.isExpanded());
    }

}
