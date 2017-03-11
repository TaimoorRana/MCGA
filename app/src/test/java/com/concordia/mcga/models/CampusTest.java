package com.concordia.mcga.models;

import android.os.IBinder;
import android.os.RemoteException;

import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.internal.zzf;
import com.google.android.gms.maps.model.internal.zzg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by taimoorrana on 2017-03-10.
 */

@RunWith(JUnit4.class)
public class CampusTest {

    MarkerOptions markerOptions = new MarkerOptions();
    Building testBuilding = new Building(new LatLng(45.495656, -73.574290), "Hall", "H", markerOptions);

    @Test
    public void getBuildingWithMarkerTest(){
        zzf z1 = new zzf() {
            @Override
            public void remove() throws RemoteException {

            }

            @Override
            public String getId() throws RemoteException {
                return "someID";
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
        zzg z2 = new zzg() {
            @Override
            public void remove() throws RemoteException {

            }

            @Override
            public String getId() throws RemoteException {
                return "someID";
            }

            @Override
            public void setPoints(List<LatLng> list) throws RemoteException {

            }

            @Override
            public List<LatLng> getPoints() throws RemoteException {
                return null;
            }

            @Override
            public void setHoles(List list) throws RemoteException {

            }

            @Override
            public List getHoles() throws RemoteException {
                return null;
            }

            @Override
            public void setStrokeWidth(float v) throws RemoteException {

            }

            @Override
            public float getStrokeWidth() throws RemoteException {
                return 0;
            }

            @Override
            public void setStrokeColor(int i) throws RemoteException {

            }

            @Override
            public int getStrokeColor() throws RemoteException {
                return 0;
            }

            @Override
            public void setFillColor(int i) throws RemoteException {

            }

            @Override
            public int getFillColor() throws RemoteException {
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
            public void setVisible(boolean b) throws RemoteException {

            }

            @Override
            public boolean isVisible() throws RemoteException {
                return false;
            }

            @Override
            public void setGeodesic(boolean b) throws RemoteException {

            }

            @Override
            public boolean isGeodesic() throws RemoteException {
                return false;
            }

            @Override
            public boolean zzb(zzg zzg) throws RemoteException {
                return false;
            }

            @Override
            public int hashCodeRemote() throws RemoteException {
                return 0;
            }

            @Override
            public void setClickable(boolean b) throws RemoteException {

            }

            @Override
            public boolean isClickable() throws RemoteException {
                return false;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        };
        testBuilding.setShortName("H");
        Marker marker = new Marker(z1);
        Polygon polygon = new Polygon(z2);

        testBuilding.setMarker(marker);
        testBuilding.setPolygon(polygon);
        Campus.SGW.addBuilding(testBuilding);

        Building result = Campus.SGW.getBuilding(marker);
        assertNotNull(result);
    }

    @Test
    public void getBuildingWithPolygonTest(){
        zzf z1 = new zzf() {
            @Override
            public void remove() throws RemoteException {

            }

            @Override
            public String getId() throws RemoteException {
                return "someID";
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
        zzg z2 = new zzg() {
            @Override
            public void remove() throws RemoteException {

            }

            @Override
            public String getId() throws RemoteException {
                return "someID";
            }

            @Override
            public void setPoints(List<LatLng> list) throws RemoteException {

            }

            @Override
            public List<LatLng> getPoints() throws RemoteException {
                return null;
            }

            @Override
            public void setHoles(List list) throws RemoteException {

            }

            @Override
            public List getHoles() throws RemoteException {
                return null;
            }

            @Override
            public void setStrokeWidth(float v) throws RemoteException {

            }

            @Override
            public float getStrokeWidth() throws RemoteException {
                return 0;
            }

            @Override
            public void setStrokeColor(int i) throws RemoteException {

            }

            @Override
            public int getStrokeColor() throws RemoteException {
                return 0;
            }

            @Override
            public void setFillColor(int i) throws RemoteException {

            }

            @Override
            public int getFillColor() throws RemoteException {
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
            public void setVisible(boolean b) throws RemoteException {

            }

            @Override
            public boolean isVisible() throws RemoteException {
                return false;
            }

            @Override
            public void setGeodesic(boolean b) throws RemoteException {

            }

            @Override
            public boolean isGeodesic() throws RemoteException {
                return false;
            }

            @Override
            public boolean zzb(zzg zzg) throws RemoteException {
                return false;
            }

            @Override
            public int hashCodeRemote() throws RemoteException {
                return 0;
            }

            @Override
            public void setClickable(boolean b) throws RemoteException {

            }

            @Override
            public boolean isClickable() throws RemoteException {
                return false;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        };
        testBuilding.setShortName("H");
        Marker marker = new Marker(z1);
        Polygon polygon = new Polygon(z2);

        testBuilding.setMarker(marker);
        testBuilding.setPolygon(polygon);
        Campus.SGW.addBuilding(testBuilding);

        Building result = Campus.SGW.getBuilding(polygon);
        assertNotNull(result);
    }
}
