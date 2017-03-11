package com.concordia.mcga.helperClasses;

import android.content.Context;
import android.graphics.Color;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;



public class OutdoorDirection implements DirectionCallback {

    private LatLng origin, destination;
    private String serverKey = "AIzaSyBQrTXiam-OzDCfSgEct6FyOQWlDWFXp6Q";
    private Polyline polyline;
    private Marker originMarker, destinationMarker;
    private Leg leg;
    private GoogleMap map;
    private Context context;
    private final int pathWidth = 5;
    private final int pathColor = Color.BLUE;



    private String transportMode;

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            originMarker = map.addMarker(new MarkerOptions().position(origin));
            destinationMarker = map.addMarker(new MarkerOptions().position(destination));
            Route route = direction.getRouteList().get(0);
            leg = route.getLegList().get(0);
            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
            polyline = map.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, pathWidth, pathColor));
            getDistance();
            getDuration();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
    }


    public void getDirection () {

        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(transportMode)
                .unit(Unit.METRIC)
                .execute(this);
    }

    public String getDistance(){
        return leg.getDistance().getText();
    }

    public String getDuration(){
        return leg.getDuration().getText();
    }

    public void setContext(Context context){
        this.context = context;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public String getServerKey() {
        return serverKey;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public Marker getOriginMarker() {
        return originMarker;
    }

    public Marker getDestinationMarker() {
        return destinationMarker;
    }

    public Leg getLeg() {
        return leg;
    }

    public GoogleMap getMap() {
        return map;
    }

    public Context getContext() {
        return context;
    }
    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public void setOriginMarker(Marker originMarker) {
        this.originMarker = originMarker;
    }

    public void setDestinationMarker(Marker destinationMarker) {
        this.destinationMarker = destinationMarker;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public void deleteDirection(){
        origin = null;
        destination = null;
        polyline.remove();
        originMarker.remove();
        destinationMarker.remove();
    }

    @Override
    public String toString() {
        return "OutdoorDirection{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", transportMode='" + transportMode + '\'' +
                '}';
    }
}
