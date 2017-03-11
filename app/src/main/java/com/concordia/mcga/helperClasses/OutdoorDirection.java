package com.concordia.mcga.helperClasses;

import android.content.Context;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class OutdoorDirection implements DirectionCallback {

    private final int transitPathWidth = 5;
    private final int transitPathColor = 0x80ed1026;
    private final int walkingPathWidth = 3;
    private final int walkingPathColor = 0x801767e8;
    private LatLng origin, destination;
    private String serverKey = "AIzaSyBQrTXiam-OzDCfSgEct6FyOQWlDWFXp6Q";
    private List<Polyline> polylines;
    private Marker originMarker, destinationMarker;
    private Leg leg;
    private GoogleMap map;
    private Context context;
    private String transportMode;

    public OutdoorDirection() {
        polylines = new ArrayList<>();
    }
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            originMarker = map.addMarker(new MarkerOptions().position(origin));
            destinationMarker = map.addMarker(new MarkerOptions().position(destination));
            Route route = direction.getRouteList().get(0);
            leg = route.getLegList().get(0);
            List<Step> stepList = leg.getStepList();
            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(context, stepList, transitPathWidth, transitPathColor, walkingPathWidth, walkingPathColor);
            for (PolylineOptions polylineOption : polylineOptionList) {
                polylines.add(map.addPolyline(polylineOption));
            }
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
                .transportMode(TransportMode.TRANSIT)
                .unit(Unit.METRIC)
                .execute(this);
    }

    public String getDistance(){
        return leg.getDistance().getText();
    }

    public String getDuration(){
        return leg.getDuration().getText();
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public List<Polyline> getPolylines() {
        return polylines;
    }

    public Marker getOriginMarker() {
        return originMarker;
    }

    public void setOriginMarker(Marker originMarker) {
        this.originMarker = originMarker;
    }

    public Marker getDestinationMarker() {
        return destinationMarker;
    }

    public void setDestinationMarker(Marker destinationMarker) {
        this.destinationMarker = destinationMarker;
    }

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPolyline(List<Polyline> polylines) {
        this.polylines = polylines;
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
        for (Polyline polyline : polylines
                ) {
            polyline.remove();
        }
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
