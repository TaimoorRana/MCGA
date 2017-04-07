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
import android.widget.Toast;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.utilities.pathfinding.MultiMapPathFinder;
import com.concordia.mcga.utilities.pathfinding.SingleMapPathFinder;
import com.jcabi.aspects.Timeable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IndoorMapFragment extends Fragment {

    //Components
    private WebView leafletView;
    private LinearLayout floorButtonContainer;
    private ProgressBar pathProgressBar;

    //State
    private boolean pageLoaded = false;
    private boolean pathGenerating = false;

    private Map<Integer, Floor> floorsLoaded;
    private Floor currentFloor;
    private Building buildingLoaded;

    private Map<Floor, List<IndoorMapTile>> currentPathTiles;
    private ArrayList<IndoorPOI> indoorPoiStack;

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

        //Progress Bar
        pathProgressBar = (ProgressBar) view.findViewById(R.id.pathProgressBar);
        pathProgressBar.setVisibility(View.GONE);

        //Init Attributes
        indoorPoiStack = new ArrayList<>();
        currentPathTiles = new HashMap<>();
        floorsLoaded = new HashMap<>();

        return view;
    }


    public void initializeBuilding(Building building) {
        buildingLoaded = building;
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

            Button button = new Button(getContext());

            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
            button.setText(String.valueOf(floorNumber));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                currentFloor = floorsLoaded.get(floorNumber);
                leafletView.evaluateJavascript("loadMap('" + getMapId(currentFloor) + "')", null);
                leafletView.evaluateJavascript("addFloorRooms(" + currentFloor.getRoomsJSON().toString() + ")", null);
            }
        });
    }

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

    public void generatePath(final IndoorPOI start, final IndoorPOI dest) {
        Thread generatePathThread = null;

        if (!pathGenerating) {
            generatePathThread = new Thread(new GeneratePath(start, dest));
            generatePathThread.start();
        }

    }

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

    public void onFloorChange() {
        drawCurrentWalkablePath();
    }

    /*
        EVENT HANDLERS
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

        Log.d("PoiClickEvent", "Poi is: " + poiClicked);
        pushRoom((Room) poiClicked);
    }

    public void pushRoom(Room room) {
        boolean doAdd = true;
        if (indoorPoiStack.size() == 2) {
            currentPathTiles.clear();
            indoorPoiStack.clear();
            leafletView.post(new Runnable() {
                @Override
                public void run() {
                    leafletView.evaluateJavascript("clearPathLayers()", null);
                }
            });
        }

        if (indoorPoiStack.size() == 0) {
            Toast.makeText(getContext(), "Start Room: " + room.getName(), Toast.LENGTH_SHORT).show();
        } else if (indoorPoiStack.size() == 1 && !indoorPoiStack.get(0).equals(room)) {
            Toast.makeText(getContext(), "Dest Room: " + room.getName(), Toast.LENGTH_SHORT).show();
        } else {
            doAdd = false;
        }

        if (doAdd)
            indoorPoiStack.add(room);

        if (indoorPoiStack.size() == 2) {
            IndoorPOI startTemp = indoorPoiStack.get(0);
            IndoorPOI endTemp = indoorPoiStack.get(1);
            generatePath(startTemp, endTemp);
        }
    }

    public void onRoomSearch() {
        final Room room = (Room) ((MainActivity) getActivity()).getLocation();
        //showFloor(room.getFloor());
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                showFloor(room.getFloor());
                int x = room.getTile().getCoordinateX();
                int y = room.getTile().getCoordinateY();
                leafletView.evaluateJavascript("panTo(" + x + "," + y + ")", null);
            }
        });
    }


    public void setCurrentPathTiles(Map<Floor, List<IndoorMapTile>> currentPathTiles) {
        this.currentPathTiles = currentPathTiles;
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            pathProgressBar.post(new Runnable() {
                @Override
                public void run() {
                    pathProgressBar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            pathProgressBar.post(new Runnable() {
                @Override
                public void run() {
                    pathProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public String getMapId(Floor floor) {
        return floor.getBuilding().getShortName() + floor.getFloorNumber();
    }


    private class GeneratePath implements Runnable {

        private final IndoorPOI start;
        private final IndoorPOI dest;

        public GeneratePath(IndoorPOI start, IndoorPOI dest) {
            this.start = start;
            this.dest = dest;
            pathGenerating = true;

            showProgressBar(true);
            try {
                populateTiledMaps();
            } catch (MCGADatabaseException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void populateTiledMaps() throws MCGADatabaseException {
            if (this.start.getFloor().equals(this.dest.getFloor())) {
                this.start.getFloor().populateTiledMap();
            } else {
                this.start.getFloor().populateTiledMap();
                this.dest.getFloor().populateTiledMap();
            }

        }

        public void depopulateTiledMaps() {
            if (this.start.getFloor().equals(this.dest.getFloor())) {
                this.start.getFloor().clearTiledMap();
            } else {
                this.start.getFloor().clearTiledMap();
                this.dest.getFloor().clearTiledMap();
            }

        }

        @Override
        @Timeable(limit = 6, unit = TimeUnit.SECONDS)
        public void run() {
            MultiMapPathFinder pf = new MultiMapPathFinder();
            Map<Floor, List<IndoorMapTile>> pathTiles;
            Map<Floor, List<IndoorMapTile>> pathTilesJunctions = new HashMap<>();
            try {
                pathTiles = pf.shortestPath(start, dest);

                for (Map.Entry<Floor, List<IndoorMapTile>> pair : pathTiles.entrySet()) {
                    pathTilesJunctions.put(pair.getKey(), SingleMapPathFinder.shortestPathJunctions(pair.getValue()));
                }

            } catch (MCGAPathFindingException e) {
                e.printStackTrace();
            }
            setCurrentPathTiles(pathTilesJunctions);

            Log.d("Thread", "Finished generating path");

            pathTiles = null;
            pathTilesJunctions = null;
            depopulateTiledMaps();
            showProgressBar(false);
            drawCurrentWalkablePath();
        }
    }
}

