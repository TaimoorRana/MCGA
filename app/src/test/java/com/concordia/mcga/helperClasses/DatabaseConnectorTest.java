package com.concordia.mcga.helperClasses;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import org.junit.Test;


public class DatabaseConnectorTest {

    @Test(expected = MCGADatabaseException.class)
    public void getInstanceWithoutContext() throws Exception {
        DatabaseConnector.getInstance();
    }

}
