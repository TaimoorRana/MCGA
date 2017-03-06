package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class PathFinderTile {

    public static final PathFinderTile MAX_COST = new PathFinderTile(Integer.MIN_VALUE, Integer.MIN_VALUE);

    static {
        MAX_COST.setDistFromEnd(Integer.MAX_VALUE);
    }

    private Type tileType;
    private int distFromStart;
    private int distFromEnd;
    private int coordinateX;
    private int coordinateY;
    private PathFinderTile parent;

    public PathFinderTile(int x, int y) {
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

    public PathFinderTile getParent() {
        return parent;
    }

    public void setParent(PathFinderTile parent) {
        this.parent = parent;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("lat", this.coordinateY);
            json.put("lng", this.coordinateX);
        } catch (JSONException e) {
            Log.e("PathFinderTile Error", Log.getStackTraceString(e));
        }
        return json;
    }

    @Override
    public String toString() {
        return "PathFinderTile{" +
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

        PathFinderTile that = (PathFinderTile) o;

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

    public int calculateDistanceTo(PathFinderTile tile) {
        return Math.abs(coordinateX - tile.coordinateX) + Math.abs(coordinateY - tile.coordinateY);
    }

    enum Type {START, DESTINATION}
}
