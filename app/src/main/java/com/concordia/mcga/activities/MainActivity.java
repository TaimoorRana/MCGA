package com.concordia.mcga.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.concordia.mcga.fragments.NavigationFragment;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Campus;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Campus currentCampus = Campus.LOY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BuildingFactory.setResources(getApplicationContext().getResources());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationFragment navigationFragment = new NavigationFragment();

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
                            openShuttleActivity();
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

    public Campus getCurrentCampus()
    {
        return this.currentCampus;
    }

    public void setCurrentCampus(Campus c)
    {
        this.currentCampus = c;
    }

    public void openShuttleActivity(){
        Intent intent = new Intent(this, ShuttleActivity.class);
        startActivity(intent);
    }
}
