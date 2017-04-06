package com.concordia.mcga.helperClasses;

import android.content.Context;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Link{OutdoorPath} connects to the Google Directions API in out to find paths between POI
 * Please refer to http://www.akexorcist.com/2015/12/google-direction-library-for-android-en.html
 */
public class OutdoorPath implements DirectionCallback {
    private final int transitPathWidth = 5;
    private final int transitPathColor = 0x80ed1026; // transparent red
    private final int walkingPathWidth = 3;
    private final int walkingPathColor = 0x801767e8; // transparent blue
    private String serverKey;
    private LatLng origin, destination;
    private List<Polyline> polylines;
    private List<Step> steps;
    private List<String> instructions;
    private Leg leg;
    private GoogleMap map;
    private Context context;
    private String transportMode;

    public OutdoorPath() {
        polylines = new ArrayList<>();
        steps = new ArrayList<>();
        instructions = new ArrayList<>();
        transportMode = TransportMode.TRANSIT; // default transport mode
    }

    /**
     * Upon successful response from Google Direction Server, Create a path between the origin and destion
     *
     * @param direction raw directions in JSON format are wrapped with the Direction class for ease of use
     * @param rawBody   raw directions in JSON format
     */
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            leg = route.getLegList().get(0);
            steps = leg.getStepList();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Log.e("Path Error:", "Unable to get directions");
    }

    /**
     * Makes a https request to get a direction from origin to destination with a specified transport mode.
     */
    public void requestDirection() {
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(transportMode)
                .transitMode(TransitMode.BUS)
                .transitMode(TransitMode.SUBWAY)
                .unit(Unit.METRIC)
                .execute(this);
    }

    /**
     * Draws the path on the map
     */
    public void drawPath() {
        if (origin == null || destination == null) {
            return;
        }

        ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(
                context,
                steps,
                transitPathWidth,
                transitPathColor,
                walkingPathWidth,
                walkingPathColor);
        for (PolylineOptions polylineOption : polylineOptionList) {
            polylines.add(map.addPolyline(polylineOption));
        }
    }

    /**
     * Deletes Markers and polylines.
     * sets origin and destionation to null
     */
    public void deleteDirection() {
        if (polylines != null) {
            for (Polyline polyline : polylines) {
                polyline.remove();
            }
        }
    }

    /**
     * @return list of instructions to get from origin to destination
     */
    public List<String> getInstructions() {
        for (Step step : steps) {
            instructions.add(step.getHtmlInstruction());
        }
        return instructions;
    }

    /**
     * @return Route total duration in "x hours y min" format
     */
    public String getDuration() {
        if (leg == null) return "";
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

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }


    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    @Override
    public String toString() {
        return "OutdoorPath{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", transportMode='" + transportMode + '\'' +
                '}';
    }
}
