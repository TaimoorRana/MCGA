package com.concordia.mcga.helperClasses;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DatabaseHelperTest {

    @Test
    public void getInstanceWithoutContext() throws Exception {
        assertEquals(null, DatabaseHelper.getInstance());
    }

}