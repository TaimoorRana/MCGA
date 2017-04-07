package com.concordia.mcga.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.factories.BuildingFactory;
import com.concordia.mcga.fragments.IndoorMapFragment;
import com.concordia.mcga.fragments.NavigationFragment;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.concordia.mcga.helperClasses.OutdoorDirections;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.POI;
import com.concordia.mcga.utilities.pathfinding.GlobalPathFinder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    enum NavigationState {
        START_INDOOR_BUILDING,
        OUTDOOR,
        DEST_INDOOR_BUILDING
    }
    private NavigationState currentState;
    private DrawerLayout drawerLayout;
    private GlobalPathFinder finder;
    private NavigationFragment navigationFragment;
    private Handler handler;
    private Object monitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BuildingFactory.setResources(getApplicationContext().getResources());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationFragment = new NavigationFragment();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, navigationFragment, "MAIN_NAV");
        fragmentTransaction.commit();

        initDatabase();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    }
                    else {
                        menuItem.setChecked(true);
                    }

                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {
                        case R.id.next_class:
                            Toast.makeText(getApplicationContext(), "Next Class", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.shuttle_schedule:
                            Toast.makeText(getApplicationContext(), "Shuttle Schedule", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.settings:
                            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.student_spots:
                            Toast.makeText(getApplicationContext(), "Student Spots", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.about:
                            Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            Toast.makeText(getApplicationContext(), "Error - Navigation Drawer", Toast.LENGTH_SHORT).show();
                            return false;
                    }
                }
            }
        );

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(), "Done finding", Toast.LENGTH_SHORT).show();

                if(null != finder.getStartBuildingDirections()){
                    loadStartIndoor(null);
                } else {
                    loadOutdoor(null);
                }
            }
        };
    }

    public void createToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initDatabase() {
        DatabaseConnector.setupDatabase(this);
        DatabaseConnector myDbHelper;
        try {
            myDbHelper = DatabaseConnector.getInstance();
        } catch (MCGADatabaseException e) {
            throw new Error("Database incorrectly initialized");
        }
        try {
            myDbHelper.createDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }

    public void testGeneration(View v) throws MCGADatabaseException {
        Floor testMap = Campus.SGW.getBuilding("H").getFloorMap(4);
        testMap.populateTiledMap();
        testMap.getIndoorPOIs().get(0);
        generateDirections(testMap.getIndoorPOIs().get(3), Campus.SGW.getBuildings().get(3), MCGATransportMode.WALKING);
    }
    public void generateDirections(POI start, POI dest, String mode){
        finder = new GlobalPathFinder(this, start, dest, mode);
        Thread finderThread = new Thread(finder);
        finderThread.start();
    }

    public void notifyPathfindingComplete(){
        if (finder.getOutDoorCoordinates() != null){
            Context context = getApplicationContext();
            OutdoorDirections outdoorDirections = navigationFragment.getOutdoorDirections();
            LatLng[] coords = finder.getOutDoorCoordinates();
            outdoorDirections.setOrigin(coords[0]);
            outdoorDirections.setSelectedTransportMode(finder.getMode());
            outdoorDirections.setDestination(coords[1]);
            outdoorDirections.setContext(context);
            outdoorDirections.setServerKey(getResources().getString(R.string.google_maps_key));
            outdoorDirections.setMap(navigationFragment.getMap());
            outdoorDirections.requestDirections();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putSerializable("finder", finder);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void loadStartIndoor(View v) {
        currentState = NavigationState.START_INDOOR_BUILDING;
        navigationFragment.showIndoorMap();
        final IndoorMapFragment indoorMapFragment = navigationFragment.getIndoorMapFragment();
        indoorMapFragment.setCurrentPathTiles(finder.getStartBuildingDirections());
        indoorMapFragment.setCurrentFloor(finder.getStartBuildingDirections().keySet().iterator().next());
        indoorMapFragment.drawCurrentWalkablePath();
    }

    public void loadOutdoor(View v) {
        currentState = NavigationState.OUTDOOR;
        navigationFragment.showOutdoorMap();
        OutdoorDirections outdoorDirections = navigationFragment.getOutdoorDirections();
        outdoorDirections.drawPathForSelectedTransportMode();
    }

    public void loadDestIndoor(View v) {
        currentState = NavigationState.DEST_INDOOR_BUILDING;
        navigationFragment.showIndoorMap();
        final IndoorMapFragment indoorMapFragment = navigationFragment.getIndoorMapFragment();
        indoorMapFragment.setCurrentPathTiles(finder.getDestBuildingDirections());
        indoorMapFragment.setCurrentFloor(finder.getDestBuildingDirections().keySet().iterator().next());
        indoorMapFragment.drawCurrentWalkablePath();
    }
}
