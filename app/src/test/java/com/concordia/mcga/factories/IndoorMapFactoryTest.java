package com.concordia.mcga.factories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.junit.Test;

/**
 * Created by arekm on 2017-02-13.
 */
public class IndoorMapFactoryTest {
    @Test
    public void testFactory() throws Exception {
        File file = new File("res/raw/hall4.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        System.out.println(reader.readLine());
    }
}
