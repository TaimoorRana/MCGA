package com.concordia.mcga.utilities.pathfinding;

import android.support.annotation.NonNull;

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

    public int calculateDistanceTo(PathFinderTile tile) {
        return Math.abs(indoorMapTile.getCoordinateX() - tile.indoorMapTile.getCoordinateX()) + Math
            .abs(indoorMapTile.getCoordinateY() - tile.indoorMapTile.getCoordinateY());
    }

    public int calculateDistFromStart() {
//        if (indoorMapTile.getCoordinateY() == parent.indoorMapTile.getCoordinateY() &&
//            parent.parent.indoorMapTile.getCoordinateY() == parent.indoorMapTile.getCoordinateY()
//            ) {
//            return parent.distFromStart + 1;
//        }
//        if (indoorMapTile.getCoordinateX() == parent.indoorMapTile.getCoordinateX()
//            && parent.parent.indoorMapTile.getCoordinateX() == parent.indoorMapTile
//            .getCoordinateX()) {
//            return parent.distFromStart + 1;
//        }
//        return parent.distFromStart + 2;
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
