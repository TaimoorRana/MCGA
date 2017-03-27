package com.concordia.mcga.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Building;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(DatabaseConnector.class)
public class BuildingPopulateRoomsTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void testFactory() throws Exception  {
        String name = "Builderooni";
        String shortName = "BD";

        PowerMockito.mockStatic(DatabaseConnector.class);
        Cursor cursor = Mockito.mock(Cursor.class);
        DatabaseConnector connector = Mockito.mock(DatabaseConnector.class);
        SQLiteDatabase db = Mockito.mock(SQLiteDatabase.class);
        Building building = new Building(new LatLng(0.0, 0.0), name, shortName,
                new MarkerOptions());

        // Iterate through the loop twice. Should get two rooms in the end
        Mockito.when(cursor.moveToNext()).thenAnswer(
                new Answer() {
                    private int count = 0;

                    public Object answer(InvocationOnMock invocation) {
                        if (count++ < 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

        Mockito.when(cursor.getString(7)).thenReturn(name);
        Mockito.when(connector.getDb()).thenReturn(db);
        Mockito.when(db.rawQuery("select * from room", null)).thenReturn(cursor);

        Mockito.when(DatabaseConnector.getInstance()).thenReturn(connector);

        // Populate fake rooms and verify that two exist
        building.populateRooms();
        assertTrue(building.getRooms().size() == 2);
    }
}