package com.concordia.mcga.factories;

import android.content.Context;
import android.content.res.Resources;
import com.concordia.mcga.models.IndoorMap;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by arekm on 2017-02-13.
 */
@RunWith(MockitoJUnitRunner.class)
public class IndoorMapFactoryTest {
    @Test
    public void testGetHall4thFloor() throws Exception {
        // Test Data
        Context context = Mockito.mock(Context.class);

        // Mock
        Resources mockResources = Mockito.mock(Resources.class);
        Mockito.when(context.getResources()).thenReturn(mockResources);
        Mockito.when(mockResources.openRawResource(Mockito.anyInt())).thenReturn(new ByteArrayInputStream("3,4\n4,5".getBytes(
            StandardCharsets.UTF_8)));

        // Execute
        IndoorMap result = IndoorMapFactory.getHall4thFloor(context);

        // Verify
        Mockito.verify(mockResources).openRawResource(Mockito.anyInt());
        Mockito.verify(context).getResources();
        Assert.assertNotNull(result.getMap().getTile(3, 4));
        Assert.assertNotNull(result.getMap().getTile(4, 5));
    }
}
