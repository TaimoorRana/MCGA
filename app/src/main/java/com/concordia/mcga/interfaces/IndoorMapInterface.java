package com.concordia.mcga.interfaces;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by Sylvain on 2/12/2017.
 */

public class IndoorMapInterface {

    private WebView webView;

    public IndoorMapInterface(WebView webView) {
        this.webView = webView;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void loadFloorMap(String mapImagePath) {

    }
}
