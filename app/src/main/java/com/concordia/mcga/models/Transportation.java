package com.concordia.mcga.models;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.helperClasses.MCGATransportMode;

public class Transportation {


    public static Transportation WALKING = new Transportation(MCGATransportMode.WALKING, R.drawable.ic_directions_walk_black_24dp);
    public static Transportation BICYCLING = new Transportation(MCGATransportMode.BICYCLING, R.drawable.ic_directions_bike_black_24dp);
    public static Transportation DRIVING = new Transportation(MCGATransportMode.DRIVING, R.drawable.ic_directions_car_black_24dp);
    public static Transportation TRANSIT = new Transportation(MCGATransportMode.TRANSIT, R.drawable.ic_directions_transit_black_24dp);
    public static Transportation SHUTTLE = new Transportation(MCGATransportMode.SHUTTLE, R.drawable.ic_stingers_icon);
    private String type;
    private int iconID;

    private Transportation(String type, int iconID) {
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

