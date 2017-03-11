package com.concordia.mcga.activities;

import android.os.IBinder;
import android.os.RemoteException;

import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.internal.zzf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class BuildingUnitTest {
    @Mock
    Marker marker;

    MarkerOptions markerOptions = new MarkerOptions();
    Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);
    @Test
    public void getNameTest() {
        assertEquals("Hall", testBuilding.getName());
    }

    @Test
    public void getShortNameTest() {
        assertEquals("H", testBuilding.getShortName());
    }

    @Test
    public void getMarkerOptionsTest() {
        assertEquals(markerOptions, testBuilding.getMarkerOptions());
    }

    @Test
    public void getCenterCoordinatesTest() {
        assertEquals(new LatLng(45.495656, -73.574290), testBuilding.getMapCoordinates());
    }

    @Test
    public void addEdgeCoordinateTest() {
        List<LatLng> list = new ArrayList<>();
        LatLng edge = new LatLng(45.495656, -73.574290);
        list.add(edge);
        testBuilding.addEdgeCoordinate(list);
        assertEquals(true, testBuilding.getEdgeCoordinateList().contains(edge));
    }

    @Test
    public void getPolygonOverlayOptions() {
        List<LatLng> list = new ArrayList<>();
        LatLng edge = new LatLng(45.495656, -73.574290);
        list.add(edge);
        testBuilding.addEdgeCoordinate(list);
        PolygonOptions polygonOptions = testBuilding.getPolygonOverlayOptions();
        assertEquals(0x996d171f, polygonOptions.getFillColor());
    }

    @Test
    public void getBuildingWithMarkerTest(){
        zzf z = new zzf() {
            @Override
            public void remove() throws RemoteException {

            }

            @Override
            public String getId() throws RemoteException {
                return "14";
            }

            @Override
            public void setPosition(LatLng latLng) throws RemoteException {

            }

            @Override
            public LatLng getPosition() throws RemoteException {
                return null;
            }

            @Override
            public void setTitle(String s) throws RemoteException {

            }

            @Override
            public String getTitle() throws RemoteException {
                return null;
            }

            @Override
            public void setSnippet(String s) throws RemoteException {

            }

            @Override
            public String getSnippet() throws RemoteException {
                return null;
            }

            @Override
            public void setDraggable(boolean b) throws RemoteException {

            }

            @Override
            public boolean isDraggable() throws RemoteException {
                return false;
            }

            @Override
            public void showInfoWindow() throws RemoteException {

            }

            @Override
            public void hideInfoWindow() throws RemoteException {

            }

            @Override
            public boolean isInfoWindowShown() throws RemoteException {
                return false;
            }

            @Override
            public void setVisible(boolean b) throws RemoteException {

            }

            @Override
            public boolean isVisible() throws RemoteException {
                return false;
            }

            @Override
            public boolean zzj(zzf zzf) throws RemoteException {
                return false;
            }

            @Override
            public int hashCodeRemote() throws RemoteException {
                return 0;
            }

            @Override
            public void zzal(zzd zzd) throws RemoteException {

            }

            @Override
            public void setAnchor(float v, float v1) throws RemoteException {

            }

            @Override
            public void setFlat(boolean b) throws RemoteException {

            }

            @Override
            public boolean isFlat() throws RemoteException {
                return false;
            }

            @Override
            public void setRotation(float v) throws RemoteException {

            }

            @Override
            public float getRotation() throws RemoteException {
                return 0;
            }

            @Override
            public void setInfoWindowAnchor(float v, float v1) throws RemoteException {

            }

            @Override
            public void setAlpha(float v) throws RemoteException {

            }

            @Override
            public float getAlpha() throws RemoteException {
                return 0;
            }

            @Override
            public void setZIndex(float v) throws RemoteException {

            }

            @Override
            public float getZIndex() throws RemoteException {
                return 0;
            }

            @Override
            public void zzam(zzd zzd) throws RemoteException {

            }

            @Override
            public zzd zzbsn() throws RemoteException {
                return null;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        };

        testBuilding.setShortName("H");
        Marker marker1 = new Marker(z);

        testBuilding.setMarker(marker1);
        Campus.SGW.addBuilding(testBuilding);

        Building result = Campus.SGW.getBuilding(marker1);
        assertNotNull(result);
    }


}
