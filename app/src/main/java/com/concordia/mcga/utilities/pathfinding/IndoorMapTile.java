package com.concordia.mcga.utilities.pathfinding;

import android.util.Log;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class IndoorMapTile {
    private int coordinateX;
    private int coordinateY;

    public IndoorMapTile(int x, int y) {
        this.coordinateX = x;
        this.coordinateY = y;
    }


    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    /**
     * @param tile - tile to calculate the distance to
     * @return - Straight line distance to given tile
     */
    public int calculateDistanceTo(IndoorMapTile tile) {
        return Math.abs(getCoordinateX() - tile.getCoordinateX()) + Math.abs(getCoordinateY() - tile.getCoordinateY());
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndoorMapTile that = (IndoorMapTile) o;

        return new EqualsBuilder()
            .append(coordinateX, that.coordinateX)
            .append(coordinateY, that.coordinateY)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(coordinateX)
            .append(coordinateY)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("coordinateX", coordinateX)
            .append("coordinateY", coordinateY)
            .toString();
    }
}
