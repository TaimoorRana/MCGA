package com.concordia.mcga.factories;

import android.content.res.Resources;
import android.database.Cursor;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.SmallBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {
        com.concordia.mcga.factories.ShadowBitmapDescriptorFactory.class,
        com.concordia.mcga.factories.ShadowMainActivity.class})
public class BuildingFactoryTest {
    private Gson gson = new Gson();

    @Test
    public void createBuilding_building() throws Exception {
        // Test data
        Cursor res = Mockito.mock(Cursor.class);
        LatLng latLng = new LatLng(1, 1);
        String name = "TEST NAME";
        String shortName = "TEST SHORT NAME";

        // Mock
        Mockito.when(res.getString(BuildingFactory.CENTER_COORDINATE_COLUMN_INDEX)).thenReturn(gson.toJson(latLng));
        Mockito.when(res.getString(BuildingFactory.SHORT_NAME_COLUMN_INDEX)).thenReturn(shortName);
        Mockito.when(res.getString(BuildingFactory.NAME_COLUMN_INDEX)).thenReturn(name);
        Mockito.when(res.getInt(BuildingFactory.IS_SMALL_BUILDING_COLUMN_INDEX)).thenReturn(0);
        Resources resources = Mockito.mock(Resources.class);
        Mockito.when(resources.getIdentifier(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        BuildingFactory.setResources(resources);

        // Execute
        Building result = BuildingFactory.createBuilding(res);

        // Verify
        assertTrue(result instanceof Building);
        assertFalse(result instanceof SmallBuilding);
        assertEquals(latLng, result.getMapCoordinates());
        assertEquals(shortName, result.getShortName());
        assertEquals(name, result.getName());
    }

    @Test
    public void createBuilding_smallBuilding() throws Exception {
        // Test data
        Cursor res = Mockito.mock(Cursor.class);
        LatLng latLng = new LatLng(1, 1);
        String name = "TEST NAME";
        String shortName = "TEST SHORT NAME";

        // Mock
        Mockito.when(res.getString(BuildingFactory.CENTER_COORDINATE_COLUMN_INDEX)).thenReturn(gson.toJson(latLng));
        Mockito.when(res.getString(BuildingFactory.SHORT_NAME_COLUMN_INDEX)).thenReturn(shortName);
        Mockito.when(res.getString(BuildingFactory.NAME_COLUMN_INDEX)).thenReturn(name);
        Mockito.when(res.getInt(BuildingFactory.IS_SMALL_BUILDING_COLUMN_INDEX)).thenReturn(1);
        Resources resources = Mockito.mock(Resources.class);
        Mockito.when(resources.getIdentifier(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        BuildingFactory.setResources(resources);

        // Execute
        Building result = BuildingFactory.createBuilding(res);

        // Verify
        assertTrue(result instanceof Building);
        assertTrue(result instanceof SmallBuilding);
        assertEquals(latLng, result.getMapCoordinates());
        assertEquals(shortName, result.getShortName());
        assertEquals(name, result.getName());
    }
}

