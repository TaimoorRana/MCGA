package com.concordia.mcga.helperClasses;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

public class GPSManager {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Activity activity;

    public GPSManager(LocationManager locationManager, LocationListener locationListener, Activity activity) {
        this.locationManager = locationManager;
        this.locationListener = locationListener;
        this.activity = activity;
    }

    public LatLng getLocation() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertGPS(activity);
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        } else {
            //Enable Network Provider updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 2,
                    locationListener);
            //Force Network provider due to GPS problems with different phone brands
            Location location = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double latitude = location.getLatitude(); //Getting latitude of the current location
                double longitude = location.getLongitude(); // Getting longitude of the current location
                return new LatLng(latitude, longitude); // Creating a LatLng object for the current location
            } else {
                return null;
            }
        }
    }

    /**
     * Build alert dialog on fragment's activity
     * Shown iff (gpsmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) is false
     * Prompt user to enable the GPS
     * If user presses "Enable GPS",  minimize application and prompt user to GPS Android window
     */
    public static boolean alertGPS(final Activity activity) { //GPS detection method
        AlertDialog.Builder build = new AlertDialog.Builder(activity);
        build
                .setTitle("GPS Detection Services")
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activity.startActivity(i);
                            }
                        });
        build.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = build.create();
        alert.show();
        return true;
    }
}
