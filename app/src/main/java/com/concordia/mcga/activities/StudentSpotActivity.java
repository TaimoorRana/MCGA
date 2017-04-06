package com.concordia.mcga.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.concordia.mcga.adapters.StudentSpotAdapter;
import com.concordia.mcga.factories.StudentSpotFactory;
import com.concordia.mcga.models.StudentSpot;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Comparator;
import java.util.List;

public class StudentSpotActivity extends AppCompatActivity {
    private List<StudentSpot> spots;
    private StudentSpotAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_spot_list);

        // Back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.spotToolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get location
        LocationManager gpsManager = (LocationManager) this.
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
            spots = StudentSpotFactory.getInstance().getStudentSpots(this.getResources());

            adapter = new StudentSpotAdapter(this, spots, myLocation);
            adapter.sort(new Comparator<StudentSpot>() {
                @Override
                public int compare(StudentSpot o1, StudentSpot o2) {
                    return Double.compare(o1.getLastKnownDistance(), o2.getLastKnownDistance());
                }
            });

            final ListView list = (ListView) findViewById(R.id.spotList);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentSpot spot = (StudentSpot)list.getItemAtPosition(position);
                    Intent intent = new Intent();
                    intent.putExtra("spot", new Gson().toJson(spot));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });


        }
    }

    /**
     * Get the location from Android.
     * @param gpsManager Android Location manager
     * @param gpsListener Android location listener
     * @return LatLng indicating position at time of polling
     */
    private LatLng getLocation(LocationManager gpsManager, LocationListener gpsListener) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        } else {
            gpsManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 2, gpsListener); //Enable Network Provider updates
            Location location = gpsManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //Force Network provider due to GPS problems with different phone brands
            if (location != null) {
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
            else {
                return null;
            }
        }
    }
}