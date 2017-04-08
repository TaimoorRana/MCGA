package com.concordia.mcga.helperClasses;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public interface IOutdoorPath {
    public void requestDirection();
    public LatLng getOrigin();
    public void setOrigin(LatLng origin);
    public void setDestination(LatLng destination);
    public LatLng getDestination();
    public void drawPath();
    public void setMap(GoogleMap map);
    public List<String> getInstructions();
    public void deleteDirection();
    public String getDuration();
    public void setContext();
    public String getTransportMode();
    public void setTransportMode(String transportMode);
    public void setServerKey(String serverKey);
    public int getDurationMinutes();
    public int getDurationHours();
}