package com.concordia.mcga.models;

import com.concordia.mcga.activities.R;

public class Transportation {

    enum TransportType {
        WALK, BIKE, CAR, PUBLIC_TRANSPORT, SHUTTLE
    }

    private TransportType type;
    private int iconID;

    public Transportation(TransportType type, int iconID) {
        this.type = type;
        this.iconID = iconID;
    }

    public TransportType getType() {
        return type;
    }

    public int getIconID() {
        return iconID;
    }

    public static Transportation WALK = new Transportation(TransportType.WALK, R.drawable.ic_directions_walk_black_24dp);
    public static Transportation BIKE = new Transportation(TransportType.BIKE, R.drawable.ic_directions_bike_black_24dp);
    public static Transportation CAR = new Transportation(TransportType.CAR, R.drawable.ic_directions_car_black_24dp);
    public static Transportation PUBLIC_TRANSPORT = new Transportation(TransportType.PUBLIC_TRANSPORT, R.drawable.ic_directions_transit_black_24dp);
    public static Transportation SHUTTLE = new Transportation(TransportType.SHUTTLE, R.drawable.ic_stingers_icon);
}
