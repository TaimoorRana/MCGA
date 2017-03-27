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
import android.widget.Toast;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.exceptions.MCGAPathFindingException;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.Escalator;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.utilities.pathfinding.MultiMapPathFinder;
import com.concordia.mcga.utilities.pathfinding.SingleMapPathFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndoorMapFragment extends Fragment {

    //Components
    private WebView leafletView;
    private LinearLayout floorButtonContainer;

    //State
    private boolean pageLoaded = false;
    private boolean pathsDrawn = false;
    private Map<Integer, Floor> floorsLoaded;
    private Floor currentFloor;
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
                //Initialize a building, normally this would be done dynamically
                initializeHBuilding();
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
        indoorPoiStack = new ArrayList<>();
        currentPathTiles = new HashMap<>();
        floorsLoaded = new HashMap<>();

        Campus.populateCampusesWithBuildings();

        return view;
    }

    private void test() {
        Building hBuilding = null;
        for (Building b : Campus.SGW.getBuildings()) {
            if (b.getShortName().equalsIgnoreCase("h")) {
                hBuilding = b;
            }
        }
        for (Integer floorNum : hBuilding.getFloorMaps().keySet()) {
            Floor floor = hBuilding.getFloorMaps().get(floorNum);
            for (Escalator escalator : floor.getEscalators()) {
                Log.d("Escalator", String.valueOf(escalator.getFloorNumber()));
            }
        }
    }

    public void generatePath(final IndoorPOI start, final IndoorPOI dest) {
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<IndoorMapTile> pathTilesJunctions = null;
                MultiMapPathFinder pf = new MultiMapPathFinder();

                try {
                    currentPathTiles = pf.shortestPath(start, dest);
                } catch (MCGAPathFindingException e) {
                    e.printStackTrace();
                }

                try {
                    pathTilesJunctions = (ArrayList<IndoorMapTile>) SingleMapPathFinder.shortestPathJunctions(currentPathTiles.get(currentFloor));

                    for (IndoorMapTile tile : pathTilesJunctions) {
                        Log.d("JCT: ", tile.toString());
                    }

                    if (pageLoaded)
                        leafletView.evaluateJavascript("drawWalkablePath(" + SingleMapPathFinder.toJSONArray(pathTilesJunctions).toString() + ")", null);
                } catch (MCGAPathFindingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void goToFloor() {

    }

    public void onFloorChange() {
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<IndoorMapTile> pathTilesJunctions = null;
                if (currentPathTiles.get(currentFloor) != null) {
                    try {
                        pathTilesJunctions = (ArrayList<IndoorMapTile>) SingleMapPathFinder.shortestPathJunctions(currentPathTiles.get(currentFloor));

                        if (pageLoaded)
                            leafletView.evaluateJavascript("drawWalkablePath(" + SingleMapPathFinder.toJSONArray(pathTilesJunctions).toString() + ")", null);
                    } catch (MCGAPathFindingException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (pageLoaded)
                        leafletView.evaluateJavascript("clearPathLayers()", null);
                }
            }
        });
    }

    public void initializeHBuilding() {
        Building hBuilding = Campus.SGW.getBuilding("H");
        floorsLoaded.put(1, hBuilding.getFloorMap(1));
        floorsLoaded.put(2, hBuilding.getFloorMap(2));
        floorsLoaded.put(4, hBuilding.getFloorMap(4));

        //This is the default first floor shown for the building
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                if (pageLoaded) {
                    leafletView.evaluateJavascript("loadMap('H2')", null);
                    leafletView.evaluateJavascript("addFloorRooms(" + floorsLoaded.get(2).getRoomsJSON().toString() + ")", null);
                }
            }
        });


        //Add Floor Buttons
        Button h1 = new Button(getContext());
        h1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h1.setText("1");
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded) {
                    currentFloor = floorsLoaded.get(1);
                    leafletView.evaluateJavascript("loadMap('H1')", null);
                    leafletView.evaluateJavascript("addFloorRooms(" + floorsLoaded.get(1).getRoomsJSON().toString() + ")", null);
                    onFloorChange();
                }
            }
        });

        Button h2 = new Button(getContext());
        h2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h2.setText("2");
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded) {
                    currentFloor = floorsLoaded.get(2);
                    leafletView.evaluateJavascript("loadMap('H2')", null);
                    leafletView.evaluateJavascript("addFloorRooms(" + floorsLoaded.get(2).getRoomsJSON().toString() + ")", null);
                    onFloorChange();
                }
            }
        });

        Button h4 = new Button(getContext());
        h4.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h4.setText("4");
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded) {
                    currentFloor = floorsLoaded.get(4);
                    leafletView.evaluateJavascript("loadMap('H4')", null);
                    leafletView.evaluateJavascript("addFloorRooms(" + floorsLoaded.get(4).getRoomsJSON().toString() + ")", null);
                    onFloorChange();
                }
            }
        });

        floorButtonContainer.addView(h4);
        floorButtonContainer.addView(h2);
        floorButtonContainer.addView(h1);
    }

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
        } else if (indoorPoiStack.size() == 1) {
            Toast.makeText(getContext(), "Dest Room: " + room.getName(), Toast.LENGTH_SHORT).show();
        }

        indoorPoiStack.add(room);

        if (indoorPoiStack.size() == 2) {
            generatePath(indoorPoiStack.get(0), indoorPoiStack.get(1));
        }
    }
}
