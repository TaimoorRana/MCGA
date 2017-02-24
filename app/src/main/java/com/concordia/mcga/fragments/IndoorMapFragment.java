package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.factories.IndoorMapFactory;
import com.concordia.mcga.models.IndoorMap;
import com.concordia.mcga.utilities.pathfinding.PathFinder;
import com.concordia.mcga.utilities.pathfinding.PathFinderTile;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sylvain on 2/12/2017.
 */

public class IndoorMapFragment extends Fragment implements View.OnClickListener {

    //Components
    private WebView leafletView;
    private LinearLayout floorButtonContainer;
    private Spinner floorSelectSpinner;
    private Button testPathButton;

    //State
    private boolean pageLoaded = false;
    private boolean pathsDrawn = false;

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

        leafletView.loadUrl("file:///android_asset/leaflet.html");

        //Floor Select Spinner
        floorButtonContainer = (LinearLayout) view.findViewById(R.id.floorButtonContainer);

        //Test Path Button
        testPathButton = (Button) view.findViewById(R.id.testPathButton);
        testPathButton.setOnClickListener(this);

        return view;
    }

    public void generatePath() {
        IndoorMap H4 = IndoorMapFactory.getHall4thFloor(getContext());
        PathFinder pf = new PathFinder(H4.getMap());

        List<PathFinderTile> pathTilesList = null;
        ArrayList<PathFinderTile> pathTiles = null;
        ArrayList<PathFinderTile> pathTilesJunctions = new ArrayList<PathFinderTile>();

        try {
            //pathTilesList = pf.shortestPath(353, 57, 1120, 594);
            pathTilesList = pf.shortestPath(353, 57, 1120, 594);
            pathTiles = new ArrayList<PathFinderTile>(pathTilesList);

            /*Iterator<PathFinderTile> it = pathTiles.iterator();
            *//*while(it.hasNext()) {
                PathFinderTile pft = it.next();
                Log.d("PFT is: ", pft.toString());
            }*/

            int curX = 0;
            int curY = 0;
            for (int i = 0; i < pathTiles.size(); i++) {
                if (i == 0) {
                    PathFinderTile pft = pathTiles.get(i);
                    pathTilesJunctions.add(pft);
                    curX = pft.getCoordinateX();
                    curY = pft.getCoordinateY();
                } else {
                    PathFinderTile pft = pathTiles.get(i);
                    if (!(pft.getCoordinateX() == curX && pft.getCoordinateY() != curY) && !(pft.getCoordinateY() == curY && pft.getCoordinateX() != curX)) {
                        pathTilesJunctions.add(pft);
                        curX = pft.getCoordinateX();
                        curY = pft.getCoordinateY();
                    }
                }
            }

            JSONArray pftArray = new JSONArray();
            Iterator<PathFinderTile> it = pathTilesJunctions.iterator();
            while (it.hasNext()) {
                PathFinderTile pft = it.next();
                pftArray.put(pft.toJSON());
            }
            Log.d("PFT Arr: ", pftArray.toString());

            if (pageLoaded)
                leafletView.evaluateJavascript("generateWalkablePath(" + pftArray.toString() + ")", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeHBuilding() {
        //Show Default Floor
        leafletView.evaluateJavascript("loadMapImage('floormaps/H/4.png')", null);

        //Add Floor Buttons
        Button h1 = new Button(getContext());
        h1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h1.setText("1/2");
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded)
                    leafletView.evaluateJavascript("loadMapImage('floormaps/H/9.png')", null);
            }
        });

        Button h4 = new Button(getContext());
        h4.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indoor_floor_button, null));
        h4.setText("4");
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageLoaded)
                    leafletView.evaluateJavascript("loadMapImage('floormaps/H/4.png')", null);
            }
        });

        floorButtonContainer.addView(h1);
        floorButtonContainer.addView(h4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testPathButton:
                /*if (pathsDrawn) {
                    pathsDrawn = false;
                    leafletView.evaluateJavascript("clearLayers()", null);
                } else {
                    pathsDrawn = true;
                    leafletView.evaluateJavascript("generatePath()", null);
                }*/
                generatePath();
                break;
        }
    }
}
