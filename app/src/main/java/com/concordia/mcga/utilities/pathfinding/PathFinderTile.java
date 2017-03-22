package com.concordia.mcga.utilities.pathfinding;

import android.support.annotation.NonNull;

import com.concordia.mcga.models.IndoorMapTile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Object acts as a wrapper for {@link IndoorMapTile}. Holds
 * additional information about the tiles that are specifically used
 * by the pathfinder algorithms
 */
public class PathFinderTile implements Comparable<PathFinderTile> {

    enum Type {START, DESTINATION}

    public static final PathFinderTile MAX_COST = new PathFinderTile();

    static {
        MAX_COST.setDistFromEnd(Integer.MAX_VALUE);
    }

    private IndoorMapTile indoorMapTile;
    private Type tileType;
    private int distFromStart;
    private int distFromEnd;
    private PathFinderTile parent;

    private PathFinderTile(){}

    public PathFinderTile(IndoorMapTile indoorMapTile) {
        this.indoorMapTile = indoorMapTile;
    }

    public Type getTileType() {
        return tileType;
    }

    public void setTileType(Type tileType) {
        this.tileType = tileType;
    }

    public IndoorMapTile getIndoorMapTile() {
        return indoorMapTile;
    }

    public void setIndoorMapTile(IndoorMapTile indoorMapTile) {
        this.indoorMapTile = indoorMapTile;
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

    public PathFinderTile getParent() {
        return parent;
    }

    public void setParent(PathFinderTile parent) {
        this.parent = parent;
    }

    /**
     * @param tile tile to calculate the distance to
     * @return the units of distance between both tiles. Units are in X or Y direction.
     *         Does not take diagonal distance.
     */
    public int calculateDistanceTo(PathFinderTile tile) {
        return Math.abs(indoorMapTile.getCoordinateX() - tile.indoorMapTile.getCoordinateX()) + Math
            .abs(indoorMapTile.getCoordinateY() - tile.indoorMapTile.getCoordinateY());
    }

    public int calculateDistFromStart() {
        return parent.distFromStart + 1;
    }

    public int getDistFromEnd() {
        return distFromEnd;
    }

    public void setDistFromEnd(int distFromEnd) {
        this.distFromEnd = distFromEnd;
    }

    @Override
    public int compareTo(@NonNull PathFinderTile o) {
        int compare = this.getCost() - o.getCost();
        if (compare == 0 && !this.equals(o)){
            compare = -1;
        }
        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PathFinderTile that = (PathFinderTile) o;

        return new EqualsBuilder()
                .append(indoorMapTile, that.indoorMapTile)
                .append(tileType, that.tileType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(indoorMapTile)
                .append(tileType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("indoorMapTile", indoorMapTile)
                .append("tileType", tileType)
                .append("distFromStart", distFromStart)
                .append("distFromEnd", distFromEnd)
                .toString();
    }
}
