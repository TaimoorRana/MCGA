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
        // FB
        MarkerOptions fbMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fb_building));
        buildings.add(new Building(new LatLng(45.494635, -73.577604), "Faubourg", "FB", fbMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.494695, -73.578040),
                        new LatLng(45.494911, -73.577785),
                        new LatLng(45.494654, -73.577219),
                        new LatLng(45.494397, -73.577521)));

        // FG
        MarkerOptions fgMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fg_building));
        buildings.add(new Building(new LatLng(45.494170, -73.578351), "Faubourg Ste-Catherine", "FG", fgMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.493822, -73.579067),
                        new LatLng(45.494695, -73.578039),
                        new LatLng(45.494452, -73.577617),
                        new LatLng(45.494388, -73.577693),
                        new LatLng(45.494428, -73.577761),
                        new LatLng(45.494392, -73.577804),
                        new LatLng(45.494372, -73.577769),
                        new LatLng(45.494187, -73.577987),
                        new LatLng(45.493626, -73.578728)));
        // GA
        MarkerOptions gaMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ga_building));
        buildings.add(new Building(new LatLng(45.494096, -73.577944), "Grey Nuns Annex", "GA", gaMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.493851, -73.578354),
                        new LatLng(45.494134, -73.578012),
                        new LatLng(45.494119, -73.577986),
                        new LatLng(45.494345, -73.577737),
                        new LatLng(45.494285, -73.577622),
                        new LatLng(45.494056, -73.577871),
                        new LatLng(45.494076, -73.577908),
                        new LatLng(45.493790, -73.578253)));

        //GN
        MarkerOptions gnMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_gn_building));
        buildings.add(new Building(new LatLng(45.493687, -73.576280), "Grey Nuns", "GN", gnMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.493908, -73.577904),
                        new LatLng(45.494239, -73.577543),
                        new LatLng(45.494268, -73.577602),
                        new LatLng(45.494379, -73.577488),
                        new LatLng(45.494397, -73.577519),
                        new LatLng(45.494683, -73.577166),
                        new LatLng(45.493833, -73.575183),
                        new LatLng(45.492341, -73.576632),
                        new LatLng(45.492969, -73.577728),
                        new LatLng(45.493507, -73.577160)));

        //MT
        MarkerOptions mtMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mt_building));
        buildings.add(new Building(new LatLng(45.494556, -73.576056), "Montefiore", "MT", mtMarkerOptions)
                .addEdgeCoordinate(new LatLng(45.494421, -73.576380),
                        new LatLng(45.494711, -73.576123),
                        new LatLng(45.494679, -73.576009),
                        new LatLng(45.494823, -73.575883),
                        new LatLng(45.494752, -73.575719),
                        new LatLng(45.494448, -73.575984),
                        new LatLng(45.494298, -73.576131)));


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
