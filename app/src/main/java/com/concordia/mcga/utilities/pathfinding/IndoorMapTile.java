package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class IndoorMapTile {

    public static final IndoorMapTile MAX_COST = new IndoorMapTile(Integer.MIN_VALUE, Integer.MIN_VALUE);

    static {
        MAX_COST.setDistFromEnd(Integer.MAX_VALUE);
    }

    private Type tileType;
    private int distFromStart;
    private int distFromEnd;
    private int coordinateX;
    private int coordinateY;
    private IndoorMapTile parent;

    public IndoorMapTile(int x, int y) {
        this.coordinateX = x;
        this.coordinateY = y;
    }

    public Type getTileType() {
        return tileType;
    }

    public void setTileType(Type tileType) {
        this.tileType = tileType;
    }

    public int getCost() {
        return distFromEnd + distFromStart;
    }

    public int getDistFromStart() {
        return distFromStart;
    }

    public void setDistFromStart(int distFromStart) {
        this.distFromStart = distFromStart;
    }

    public int calculateDistFromStart() {
        if (coordinateY == parent.coordinateY && parent.parent.coordinateY == parent.coordinateY){
            return parent.distFromStart + 1;
        }
        if (coordinateX == parent.coordinateX && parent.parent.coordinateX == parent.coordinateX){
            return parent.distFromStart + 1;
        }
        return parent.distFromStart + 2;
    }

    public int getDistFromEnd() {
        return distFromEnd;
    }

    public void setDistFromEnd(int distFromEnd) {
        this.distFromEnd = distFromEnd;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public IndoorMapTile getParent() {
        return parent;
    }

    public void setParent(IndoorMapTile parent) {
        this.parent = parent;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("lat", this.coordinateY);
            json.put("lng", this.coordinateX);
        } catch (JSONException e) {
            Log.e("IndoorMapTile Error", Log.getStackTraceString(e));
        }
        return json;
    }

    @Override
    public String toString() {
        return "IndoorMapTile{" +
                "coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndoorMapTile that = (IndoorMapTile) o;

        return new EqualsBuilder()
                .append(distFromStart, that.distFromStart)
                .append(distFromEnd, that.distFromEnd)
                .append(coordinateX, that.coordinateX)
                .append(coordinateY, that.coordinateY)
                .append(tileType, that.tileType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tileType)
                .append(distFromStart)
                .append(distFromEnd)
                .append(coordinateX)
                .append(coordinateY)
                .toHashCode();
    }

    public int calculateDistanceTo(IndoorMapTile tile) {
        return Math.abs(coordinateX - tile.coordinateX) + Math.abs(coordinateY - tile.coordinateY);
    }

    enum Type {START, DESTINATION}
}
