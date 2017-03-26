package com.concordia.mcga.models;

import com.akexorcist.googledirection.constant.TransportMode;
import com.concordia.mcga.activities.R;

public class Transportation {


    public static Transportation WALKING = new Transportation(TransportMode.WALKING, R.drawable.ic_directions_walk_black_24dp);
    public static Transportation BICYCLING = new Transportation(TransportMode.BICYCLING, R.drawable.ic_directions_bike_black_24dp);
    public static Transportation DRIVING = new Transportation(TransportMode.DRIVING, R.drawable.ic_directions_car_black_24dp);
    public static Transportation TRANSIT = new Transportation(TransportMode.TRANSIT, R.drawable.ic_directions_transit_black_24dp);
    public static Transportation SHUTTLE = new Transportation("shuttle", R.drawable.ic_stingers_icon);
    private String type;
    private int iconID;

    public Transportation(String type, int iconID) {
        this.type = type;
        this.iconID = iconID;
    }

    public String getType() {
        return type;
    }

    public int getIconID() {
        return iconID;
    }
}
