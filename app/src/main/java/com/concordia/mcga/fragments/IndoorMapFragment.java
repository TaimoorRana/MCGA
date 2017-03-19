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
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.IndoorPOI;
import com.concordia.mcga.models.Room;
import com.concordia.mcga.utilities.pathfinding.SingleMapPathFinder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

public class IndoorMapFragment extends Fragment {

    //Components
    private WebView leafletView;
    private LinearLayout floorButtonContainer;
    private Button testPathButton;

    //State
    private boolean pageLoaded = false;
    private boolean pathsDrawn = false;
    private Floor currentFloor;

    //Test POIs for Demo
    IndoorPOI H423 = new IndoorPOI(null, "H423", new IndoorMapTile(353, 1326));
    IndoorPOI H436 = new IndoorPOI(null, "H436", new IndoorMapTile(1220, 594));
    IndoorPOI H433 = new IndoorPOI(null, "H433", new IndoorMapTile(354, 57));
    IndoorPOI H401 = new IndoorPOI(null, "H401", new IndoorMapTile(1972, 1616));
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

        //Indoor POI Stack
        indoorPoiStack = new ArrayList<IndoorPOI>();

        Campus.populateCampusesWithBuildings();
        test();

        return view;
    }

    private void test() {
        Building hBuilding = null;
        for (Building b : Campus.SGW.getBuildings()) {
            if(b.getShortName().equalsIgnoreCase("h")) {
                hBuilding = b;
            }
        }
        for (Integer floorNum : hBuilding.getFloorMaps().keySet()) {
            Floor floor = hBuilding.getFloorMaps().get(floorNum);
            Room room = (Room) floor.getIndoorPOIs().get(0);
            Log.d("JSON Room", room.toJson().toString());
        }
    }

    public void generatePath(final IndoorPOI start, final IndoorPOI dest) {
        leafletView.post(new Runnable() {
            @Override
            public void run() {
                Building hall = new Building(new LatLng(0,0), "Hall", "H", new MarkerOptions());
                Floor H4 = hall.getFloorMap(4);
                SingleMapPathFinder pf = new SingleMapPathFinder(H4.getMap());

                ArrayList<IndoorMapTile> pathTilesJunctions = null;

                try {
                    pathTilesJunctions = (ArrayList<IndoorMapTile>) pf.shortestPathJunctions(start.getTile(), dest.getTile());

                    Iterator<IndoorMapTile> it2 = pathTilesJunctions.iterator();
                    while (it2.hasNext()) {
                        IndoorMapTile pft2 = it2.next();
                        Log.d("JCT: ", pft2.toString());
                    }

                    if (pageLoaded)
                        leafletView.evaluateJavascript("drawWalkablePath(" + pf.toJSONArray(pathTilesJunctions).toString() + ")", null);
                } catch (MCGAPathFindingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initializeHBuilding() {
        Building hBuilding = Campus.SGW.getBuilding("H");
        //This is the default first floor shown for the building
        leafletView.evaluateJavascript("loadMap('H2')", null);

        //Add Floor Buttons
        Button h1 = new Button(getContext());
        h1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h1.setText("1");
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded) {
                    leafletView.evaluateJavascript("loadMap('H1')", null);
                }
            }
        });

        //Add Floor Buttons
        Button h2 = new Button(getContext());
        h2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h2.setText("2");
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded) {
                    leafletView.evaluateJavascript("loadMap('H2')", null);
                }
            }
        });

        final Floor h4Floor = hBuilding.getFloorMap(4);
        Button h4 = new Button(getContext());
        h4.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h4.setText("4");
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded) {
                    currentFloor = h4Floor;
                    leafletView.evaluateJavascript("loadMap('H4')", null);
                    //leafletView.evaluateJavascript("addH4Markers()", null);
                    leafletView.evaluateJavascript("addFloorRooms(" + h4Floor.getRoomsJSON().toString() +")", null);
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
