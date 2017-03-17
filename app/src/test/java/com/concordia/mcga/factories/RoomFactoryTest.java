package com.concordia.mcga.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.database.Cursor;
import com.concordia.mcga.activities.BuildConfig;
import com.concordia.mcga.models.Room;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = ShadowBitmapDescriptorFactory.class)
public class RoomFactoryTest {
    private Gson gson = new Gson();

    @Test
    public void createRoom() throws Exception {
        Cursor res = Mockito.mock(Cursor.class);
        LatLng latLng = new LatLng(0, 0);
        String name = "Testo";
        int roomNumber = 12;

        Mockito.when(res.getString(RoomFactory.CENTER_COORDINATE_COLUMN_INDEX)).thenReturn(gson.toJson(latLng));
        Mockito.when(res.getString(RoomFactory.NAME_COLUMN_INDEX)).thenReturn(name);
        Mockito.when(res.getInt(RoomFactory.ROOM_NUMBER_COLUMN_INDEX)).thenReturn(roomNumber);

        Room result = RoomFactory.createRoom(res);

        assertTrue(result instanceof Room);
        assertEquals(latLng, result.getMapCoordinates());
        assertEquals(name, result.getName());
        assertEquals(roomNumber, result.getRoomNumber());
    }
}

