package com.concordia.mcga.factories;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.StudentSpot;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
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

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(DatabaseConnector.class)
public class StudentSpotFactoryTest {
    private final String SPOT_COORDINATES = "{\"latitude\":0.1,\"longitude\":2.3,\"mVersionCode\":1}";
    private final String SPOT_NAME = "fakeRetrievedSpot";
    private final float SPOT_RATING = 5.0f;
    private final String SPOT_DESCRIPTION = "fakeRetrievedDescription";
    private final String SPOT_ADDRESS = "fakeRetrievedAddress";
    private static final double DELTA = 1e-15;

    private StudentSpotFactory factory;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception  {
        PowerMockito.mockStatic(DatabaseConnector.class);
        Cursor cursor = Mockito.mock(Cursor.class);
        DatabaseConnector connector = Mockito.mock(DatabaseConnector.class);
        SQLiteDatabase db = Mockito.mock(SQLiteDatabase.class);

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

        Mockito.when(cursor.getString(StudentSpotFactory.NAME_COLUMN_INDEX))
                .thenReturn(SPOT_NAME);
        Mockito.when(cursor.getFloat(StudentSpotFactory.RATING_COLUMN_INDEX))
                .thenReturn(SPOT_RATING);
        Mockito.when(cursor.getString(StudentSpotFactory.ADDRESS_COLUMN_INDEX))
                .thenReturn(SPOT_ADDRESS);
        Mockito.when(cursor.getString(StudentSpotFactory.DESCRIPTION_COLUMN_INDEX))
                .thenReturn(SPOT_DESCRIPTION);
        Mockito.when(cursor.getString(StudentSpotFactory.COORDINATE_COLUMN_INDEX))
                .thenReturn(SPOT_COORDINATES);

        Mockito.when(connector.getDb()).thenReturn(db);
        Mockito.when(db.rawQuery("select * from student_spots", null)).thenReturn(cursor);

        Mockito.when(DatabaseConnector.getInstance()).thenReturn(connector);

        factory = StudentSpotFactory.getInstance();
    }

    @Test
    public void testFactoryPopulated() throws Exception {
        // Populate fake rooms and verify that two exist
        Resources mockContext = Mockito.mock(Resources.class);
        List<StudentSpot> spots = factory.getStudentSpots(mockContext);

        StudentSpot spot = spots.get(0);

        assertEquals(spot.getAddress(), SPOT_ADDRESS);
        assertEquals(spot.getRating(), SPOT_RATING, DELTA);
        assertEquals(spot.getName(), SPOT_NAME);
        assertEquals(spot.getDescription(), SPOT_DESCRIPTION);
        assertEquals(spot.getMapCoordinates(), new LatLng(0.1, 2.3)); // see SPOT_COORDINATES
    }
}