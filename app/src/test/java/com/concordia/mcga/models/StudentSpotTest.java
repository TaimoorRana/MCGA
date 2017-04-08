package com.concordia.mcga.models;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentSpotTest {
    private StudentSpot studentSpot;

    private static final double DELTA = 1e-15;

    private final LatLng SPOT_COORDINATES = new LatLng(1.0, 2.0);
    private final String SPOT_NAME = "fakeSpot";
    private final float SPOT_RATING = 3.0f;
    private final String SPOT_DESCRIPTION = "fakeDescription";
    private final String SPOT_ADDRESS = "fakeAddress";
    private final int SPOT_RES_ID = 42;

    @Before
    public void setUp() throws Exception {
        studentSpot = new StudentSpot(SPOT_COORDINATES, SPOT_NAME, SPOT_RATING, SPOT_DESCRIPTION,
                SPOT_ADDRESS, SPOT_RES_ID);
    }

    @Test
    public void getRating() throws Exception {
        assertEquals(SPOT_RATING, studentSpot.getRating(), DELTA);
    }

    @Test
    public void setRating() throws Exception {
        studentSpot.setRating(1.0f);
        assertEquals(1.0f, studentSpot.getRating(), DELTA);

    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(SPOT_DESCRIPTION, studentSpot.getDescription());

    }

    @Test
    public void setDescription() throws Exception {
        String alternativeDescription = "Have you ever retired a human by mistake?";
        studentSpot.setDescription(alternativeDescription);
        assertEquals(alternativeDescription, studentSpot.getDescription());

    }

    @Test
    public void getAddress() throws Exception {
        assertEquals(SPOT_ADDRESS, studentSpot.getAddress());

    }

    @Test
    public void setAddress() throws Exception {
        String alternativeAddress = "This is not an address";
        studentSpot.setAddress(alternativeAddress);
        assertEquals(alternativeAddress, studentSpot.getAddress());
    }

    @Test
    public void getResId() throws Exception {
        assertEquals(SPOT_RES_ID, studentSpot.getResId());
    }

    @Test
    public void setResId() throws Exception {
        studentSpot.setResId(42);
        assertEquals(42, studentSpot.getResId());
    }

}