package com.concordia.mcga.factories;

import static com.concordia.mcga.factories.RoomFactory.ROOM_ICON_COLUMN_INDEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.database.Cursor;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.models.Room.RoomIcon;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(RoomFactory.class)
public class RoomFactoryTest {
    private Gson gson = new Gson();

    @Test
    public void createRoom() throws Exception {
        Cursor res = Mockito.mock(Cursor.class);
        LatLng latLng = new LatLng(0, 0);
        String name = "Testo";
        String roomNumber = "12";

        Mockito.when(res.getString(RoomFactory.CENTER_COORDINATE_COLUMN_INDEX)).thenReturn(gson.toJson(latLng));
        Mockito.when(res.getString(RoomFactory.NAME_COLUMN_INDEX)).thenReturn(name);
        Mockito.when(res.getString(RoomFactory.ROOM_NUMBER_COLUMN_INDEX)).thenReturn(roomNumber);
        Mockito.when(res.getString(ROOM_ICON_COLUMN_INDEX)).thenReturn(RoomIcon.NONE.toString());

        Room result = RoomFactory.createRoom(res);

        assertNotNull(result);
        assertEquals(latLng, result.getMapCoordinates());
        assertEquals(name, result.getName());
        assertEquals(roomNumber, result.getRoomNumber());
    }
}

