package com.concordia.mcga.utilities.pathfinding;

import android.content.Context;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.concordia.mcga.helperClasses.IOutdoorPath;
import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Link{OutdoorPath} connects to the Google Directions API in out to find paths between POI
 * Please refer to http://www.akexorcist.com/2015/12/google-direction-library-for-android-en.html
 */
public class OutdoorPath implements DirectionCallback, IOutdoorPath {
    private int transitPathWidth = 5;
    private int transitPathColor = 0x80ed1026; // transparent red
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
    private int durationMinutes;
    private int durationHours;
    private boolean isPathSelected;
    private int currentStep;
    private List<LatLng> allStartLatLng;

    public OutdoorPath() {
        polylines = new ArrayList<>();
        steps = new ArrayList<>();
        instructions = new ArrayList<>();
        transportMode = MCGATransportMode.TRANSIT; // default transport mode
        isPathSelected = false;
        currentStep = 0;
        allStartLatLng = new ArrayList<>();
    }

    /**
     * Upon successful response from Google Direction Server, Create a path between the origin and destination
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
            setDurationHoursAndMinutes();
            if (isPathSelected)
                drawPath();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Log.e("Path Error:", t.toString());
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
     * sets origin and destination to null
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
        instructions.clear();
        for (Step step : steps) {
            instructions.add(step.getHtmlInstruction().replaceAll("\\<[^>]*>", ""));
        }
        instructions.add("Arrived at " + leg.getEndAddress());
        return instructions;
    }

    public void clearInstructions() {
        if (instructions != null) {
            instructions.clear();
            currentStep = 0;
        }
    }

    @Override
    public LatLng getNextLatLng() {
        LatLng nextLatLng;
        // if all steps are completed, nextLatLng is the destination
        if(currentStep >= steps.size()){
            nextLatLng = destination;
            currentStep = 0;
        }else {
            nextLatLng = steps.get(currentStep).getStartLocation().getCoordination();
            currentStep++;
        }
        return nextLatLng;
    }

    /**
     * @return Route total duration in "x hours y min" format
     */
    public String getDuration() {
        if (leg == null) return "";
        return leg.getDuration().getText();
    }

    /**
     * Filters and set minutes and hours for this outdoorPath
     */
    private void setDurationHoursAndMinutes() {
        final int hoursAndMinutesListMaxSize = 2;
        String duration = getDuration();

        // Regex pattern to capture integers
        String pattern = "\\d+";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(duration);

        List<Integer> hoursAndMinutes = new ArrayList<>();
        while (m.find()) {
            hoursAndMinutes.add(Integer.valueOf(m.group()));
        }
        if (hoursAndMinutes.size() < hoursAndMinutesListMaxSize) {
            // add 0 for hours and place it in from of the list if duration is less than an hour
            hoursAndMinutes.add(0, 0);
        }
        durationHours = hoursAndMinutes.get(0);
        durationMinutes = hoursAndMinutes.get(1);
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
        if (transportMode.equalsIgnoreCase(MCGATransportMode.WALKING)) {
            transitPathColor = walkingPathColor;
            transitPathWidth = walkingPathWidth;
        }
    }


    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setPathSelected(boolean isPathSelected) {
        this.isPathSelected = isPathSelected;
    }

    @Override
    public String toString() {
        return "OutdoorPath{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", transportMode='" + transportMode + '\'' +
                '}';
    }

    public void setTransitPathColor(int transitPathColor) {
        this.transitPathColor = transitPathColor;
    }

    public List<LatLng> getAllStartLatLng(){
        for(Step step: steps){
            allStartLatLng.add(step.getStartLocation().getCoordination());
        }
        return allStartLatLng;
    }

}
