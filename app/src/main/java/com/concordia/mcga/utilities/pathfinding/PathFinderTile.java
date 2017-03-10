package com.concordia.mcga.utilities.pathfinding;

import android.support.annotation.NonNull;

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
        return this.getCost() - o.getCost();
    }
}
