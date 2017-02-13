package com.concordia.mcga.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.concordia.mcga.interfaces.IndoorMapInterface;

/**
 * Created by Sylvain on 2/12/2017.
 */

public class IndoorMapFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //JS Interface
    private IndoorMapInterface indoorMapInterface;

    //Components
    private WebView leafletView;
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
            }
        });

        WebSettings webSettings = leafletView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        //leafletView.addJavascriptInterface(new IndoorMapInterface(leafletView), "IndoorMap");
        leafletView.loadUrl("file:///android_asset/leaflet.html");

        //Floor Select Spinner
        floorSelectSpinner = (Spinner) view.findViewById(R.id.floorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.h_floors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSelectSpinner.setAdapter(adapter);
        floorSelectSpinner.setOnItemSelectedListener(this);

        //Test Path Button
        testPathButton = (Button) view.findViewById(R.id.testPathButton);
        testPathButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getItemAtPosition(position).toString()) {
            case "H4":
                testPathButton.setVisibility(View.VISIBLE);
                if (pageLoaded)
                    leafletView.evaluateJavascript("loadMapImage('floormaps/H/4.png')", null);
                break;
            case "H5":
                testPathButton.setVisibility(View.GONE);
                if (pageLoaded)
                    leafletView.evaluateJavascript("loadMapImage('floormaps/H/5.png')", null);
                break;
            case "H9":
                testPathButton.setVisibility(View.GONE);
                if (pageLoaded)
                    leafletView.evaluateJavascript("loadMapImage('floormaps/H/9.png')", null);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testPathButton:
                if (pathsDrawn) {
                    pathsDrawn = false;
                    leafletView.evaluateJavascript("clearLayers()", null);
                } else {
                    pathsDrawn = true;
                    leafletView.evaluateJavascript("generatePath()", null);
                }
                break;
        }
    }
}
