package com.concordia.mcga.models;

import com.concordia.mcga.activities.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Campus extends POI {
    public static final Campus LOYOLA = new Campus(new LatLng(45.458563, -73.640156), "Loyola Campus", "LOY");
    public static final Campus SGW = new Campus(new LatLng(45.497100, -73.579077), "SGW Campus", "SGW");

    private String shortName;
    private ArrayList<Building> buildings;

    public Campus(LatLng mapCoordinates, String name, String shortName) {
        super(mapCoordinates, name);
        this.shortName = shortName;
        buildings = new ArrayList<>();
    }

    public void populateCampusWithBuildings() {
        if (shortName.equalsIgnoreCase("SGW")) {
            populateSGWCampusWithBuildings();
        } else {
            populateLOYCampusWithBuildings();
        }
    }

    private void populateLOYCampusWithBuildings() {
    }

    private void populateSGWCampusWithBuildings() {
        // H
        MarkerOptions hMarkerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_h_building));

        buildings.add(new Building(new LatLng(45.497289, -73.578932), "Hall", "H", hMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.497164, -73.579543),
                        new LatLng(45.497707, -73.579033),
                        new LatLng(45.497374, -73.578343),
                        new LatLng(45.496834, -73.578848)));
        // LB
        MarkerOptions lbMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_lb_building));
        buildings.add(new Building(new LatLng(45.496777, -73.577857), "J.W. McConnell", "LB", lbMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.496686, -73.578550),
                        new LatLng(45.497246, -73.578023),
                        new LatLng(45.496879, -73.577268),
                        new LatLng(45.496555, -73.577579),
                        new LatLng(45.496485, -73.577440),
                        new LatLng(45.496248, -73.577665)));
        // GM
        MarkerOptions gmMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_gm_building));
        buildings.add(new Building(new LatLng(45.495870, -73.578755), "Guy-De Maisonneuve Building", "GM", gmMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.495796, -73.579094),
                        new LatLng(45.496117, -73.578779),
                        new LatLng(45.495944, -73.578421),
                        new LatLng(45.495625, -73.578736)));
        // EV
        MarkerOptions evMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ev_building));
        buildings.add(new Building(new LatLng(45.495466, -73.577946), "Engineering, Computer Science and Visual Arts Integrated Complex", "EV", evMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.495597, -73.578763),
                        new LatLng(45.495866, -73.578497),
                        new LatLng(45.495669, -73.578072),
                        new LatLng(45.496047, -73.577709),
                        new LatLng(45.495831, -73.577252),
                        new LatLng(45.495205, -73.577918)));
        // JM
        MarkerOptions jmMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_jm_building));
        buildings.add(new Building(new LatLng(45.495241, -73.578925), "John Molson", "JM", jmMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.495358, -73.579366),
                        new LatLng(45.495518, -73.579199),
                        new LatLng(45.495440, -73.578961),
                        new LatLng(45.495186, -73.578525),
                        new LatLng(45.495005, -73.578737),
                        new LatLng(45.495038, -73.578792),
                        new LatLng(45.495007, -73.578824),
                        new LatLng(45.495166, -73.579171),
                        new LatLng(45.495222, -73.579114)));


    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
