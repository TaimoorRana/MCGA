package com.concordia.mcga.fragments;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.StudentSpotAdapter;
import com.concordia.mcga.factories.StudentSpotFactory;
import com.concordia.mcga.models.StudentSpot;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class StudentSpotFragment extends Fragment {
    private List<StudentSpot> spots;
    private StudentSpotAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate layout view
        View view = inflater.inflate(R.layout.student_spot_fragment, container, false);

        // Get location
        LocationManager gpsManager = (LocationManager) getActivity().
                getSystemService(LOCATION_SERVICE);
        LocationListener gpsListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {}
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        LatLng myLocation = getLocation(gpsManager, gpsListener);

        if (myLocation != null) {
            // Populate adapter
            Activity activity = getActivity();
            spots = StudentSpotFactory.getInstance().getStudentSpots(activity.getResources());

            adapter = new StudentSpotAdapter(activity, spots, myLocation);
            ListView list = (ListView) view.findViewById(R.id.spotList);
            list.setAdapter(adapter);

            return view;
        }
        else {
            return new View(getActivity());
        }
    }

    /**
     * Get the location from Android.
     * @param gpsmanager Android Location manager
     * @param gpsListen Android location listener
     * @return LatLng indicating position at time of polling
     */
    private LatLng getLocation(LocationManager gpsmanager, LocationListener gpsListen) {
        Activity activity = getActivity();
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        } else {
            gpsmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 2, gpsListen); //Enable Network Provider updates
            Location location = gpsmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //Force Network provider due to GPS problems with different phone brands
            if (location != null) {
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
            else {
                return null;
            }
        }
    }

}