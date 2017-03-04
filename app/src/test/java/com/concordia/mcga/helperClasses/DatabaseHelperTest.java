package com.concordia.mcga.helperClasses;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DatabaseHelperTest {

    @Test(expected = MCGADatabaseException.class)
    public void getInstanceWithoutContext() throws Exception {
        DatabaseHelper.getInstance()
    }

}
