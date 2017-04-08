package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.utilities.pathfinding.SingleMapPathFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndoorMapFragment extends Fragment {

    //Components
    private WebView leafletView;
    private LinearLayout floorButtonContainer;

    //State
    private boolean pageLoaded = false;
    private boolean pathGenerating = false;

    private Map<Integer, Floor> floorsLoaded;
    private Floor currentFloor;
    private Building buildingLoaded;

    private Map<Floor, List<IndoorMapTile>> currentPathTiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.indoor_map_fragment, container, false);

        //Init Webview
        leafletView = (WebView) view.findViewById(R.id.leafletview);
        leafletView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                pageLoaded = true;
            }
        });

        WebSettings webSettings = leafletView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        leafletView.addJavascriptInterface(this, "Android");

        leafletView.loadUrl("file:///android_asset/leaflet.html");

        //Floor Select Spinner
        floorButtonContainer = (LinearLayout) view.findViewById(R.id.floorButtonContainer);

        //Init Attributes
        currentPathTiles = new HashMap<>();
        floorsLoaded = new HashMap<>();

        return view;
    }

    /**
     * Initializes a building, populating floor buttons and loading a default floor. The default
     * floor is always the one with the lowest floor number.
     *
     * @param building
     */
    public void initializeBuilding(Building building) {

        if (buildingLoaded == null || !buildingLoaded.equals(building)) {

            final List<Integer> floorNumbersOrdered = new ArrayList<>();

            //Load Floors
            for (Map.Entry<Integer, Floor> entry : building.getFloorMaps().entrySet()) {
                Integer key = entry.getKey();
                Floor floor = entry.getValue();

                floorNumbersOrdered.add(key);
                floorsLoaded.put(entry.getKey(), entry.getValue());
            }

            Collections.sort(floorNumbersOrdered, Collections.<Integer>reverseOrder());

            for (final Integer floorNumber : floorNumbersOrdered) {
                final Floor floor = floorsLoaded.get(floorNumber);

                final Button button = new Button(getContext());

                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
                button.setText(String.valueOf(floorNumber));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < floorButtonContainer.getChildCount(); i++) {
                            Button b = (Button) floorButtonContainer.getChildAt(i);
                            b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
                            b.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                        }

                        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button_clicked, null));
                        button.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));

                        if (pageLoaded) {
                            currentFloor = floorsLoaded.get(floorNumber);
                            leafletView.evaluateJavascript("loadMap('" + getMapId(floor) + "')", null);
                            leafletView.evaluateJavascript("addFloorRooms(" + floorsLoaded.get(floorNumber).getRoomsJSON().toString() + ")", null);
                            onFloorChange();
                        }
                    }
                });

                floorButtonContainer.addView(button);
            }

            //Set the default floor as the lowest one
            leafletView.post(new Runnable() {
                @Override
                public void run() {
                    int floorNumber = floorNumbersOrdered.get(0);

                    //Set The First Button Color
                    Button b = (Button) floorButtonContainer.getChildAt(0);
                    b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button_clicked, null));
                    b.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));

                    //Load The Floor
                    currentFloor = floorsLoaded.get(floorNumber);
                    leafletView.evaluateJavascript("loadMap('" + getMapId(currentFloor) + "')", null);
                    leafletView.evaluateJavascript("addFloorRooms(" + currentFloor.getRoomsJSON().toString() + ")", null);
                }
            });

            buildingLoaded = building;
        }

    }

    /**
     * If the building is currently loaded, will show the floor map associated to the floor passed in.
     * If the building is not currently loaded, call initializeBuilding() first.
     *
     * @param floor
     */
    public void showFloor(final Floor floor) {
        if (!buildingLoaded.getFloorMaps().containsValue(floor)) {
            return;
        }

        leafletView.post(new Runnable() {
            @Override
            public void run() {
                currentFloor = floorsLoaded.get(floor.getFloorNumber());
                leafletView.evaluateJavascript("loadMap('" + getMapId(currentFloor) + "')", null);
                leafletView.evaluateJavascript("addFloorRooms(" + currentFloor.getRoomsJSON().toString() + ")", null);
            }
        });
    }

    /**
     * Initiates a path generation thread to find a walkable path between start and dest
     *
     * @param start Start Point
     * @param dest  End Point
     */
 /*   public void generatePath(final IndoorPOI start, final IndoorPOI dest) {
        Thread generatePathThread = null;

        if (!pathGenerating) {
            generatePathThread = new Thread(new GeneratePath(start, dest));
            generatePathThread.start();
        }
        }
*/

    /**
     * Callback useful for drawing paths.
     */
    public void drawCurrentWalkablePath() {
        pathGenerating = false;
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                if (currentPathTiles.get(currentFloor) != null) {
                    if (pageLoaded)
                        leafletView.evaluateJavascript("drawWalkablePath(" + SingleMapPathFinder.toJSONArray(currentPathTiles.get(currentFloor)).toString() + ")", null);
                } else {
                    if (pageLoaded)
                        leafletView.evaluateJavascript("clearPathLayers()", null);
                }
            }
        });
    }

    /**
     * Public method to clear walkable paths on all indoor maps
     */
    public void clearWalkablePaths() {
        currentPathTiles.clear();

        leafletView.post(new Runnable() {
            @Override
            public void run() {
                if (pageLoaded)
                    leafletView.evaluateJavascript("clearPathLayers()", null);
            }
        });
    }

    public void onFloorChange() {
        drawCurrentWalkablePath();
    }

    /*
        EVENT HANDLERS
     */

    /**
     * This event is fired everytime a poi is clicked on a map.
     *
     * @param poiName The name of the poiClicked. This is provided by the caller.
     */
    @JavascriptInterface
    public void poiClicked(String poiName) {
        Log.d("PoiClickEvent", poiName);
        IndoorPOI poiClicked = null;
        for (IndoorPOI poi : currentFloor.getIndoorPOIs()) {
            if (poi.getName().equalsIgnoreCase(poiName)) {
                poiClicked = poi;
            }
        }

        final IndoorPOI threadPOI = poiClicked;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).setNavigationPOI(threadPOI);
            }
        });

        Log.d("PoiClickEvent", "Poi is: " + poiClicked);
    }

    /**
     * This event handles a room search. It will show the floor if the appropriate building is loaded
     * and will pan and zoom into that room.
     */
    public void onRoomSearch(final Room room) {
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                if (!currentFloor.equals(room.getFloor()))
                    showFloor(room.getFloor());

                int x = room.getTile().getCoordinateX();
                int y = room.getTile().getCoordinateY();
                leafletView.evaluateJavascript("panTo(" + x + "," + y + ")", null);
            }
        });
    }


    /**
     * Given a floor, this will return the mapId that identifies the map, allowing its image to be loaded into the canvas.
     *
     * @param floor
     * @return mapId - Example: H4
     */
    public String getMapId(Floor floor) {
        return floor.getBuilding().getShortName() + floor.getFloorNumber();
    }

    public void setCurrentPathTiles(Map<Floor, List<IndoorMapTile>> currentPathTiles) {
        this.currentPathTiles = currentPathTiles;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }
}

