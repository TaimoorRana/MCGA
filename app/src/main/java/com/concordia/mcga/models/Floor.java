package com.concordia.mcga.models;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.factories.IndoorMapFactory;
import com.concordia.mcga.utilities.pathfinding.TiledMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private Building building;
    private TiledMap map;
    private int floorNumber;
    private List<IndoorPOI> indoorPOIs;
    private List<Elevator> elevators;
    private List<Staircase> staircases;
    private List<Escalator> escalators;

    public Floor() {
    }

    public Floor(Building building, int floorNumber) {
        this.building = building;
        this.floorNumber = floorNumber;
        this.indoorPOIs = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.staircases = new ArrayList<>();
        this.escalators = new ArrayList<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public List<IndoorPOI> getIndoorPOIs() {
        return indoorPOIs;
    }

    public JSONArray getRoomsJSON() {
        JSONArray indoorPOIArray = new JSONArray();
        for (IndoorPOI poi : getIndoorPOIs()) {
            if (poi instanceof Room) {
                Room room = (Room) poi;
                indoorPOIArray.put(room.toJson());
            }
        }
        return indoorPOIArray;
    }

    public void setIndoorPOIs(List<IndoorPOI> indoorPOIs) {
        this.indoorPOIs = indoorPOIs;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public List<Staircase> getStaircases() {
        return staircases;
    }

    public List<Escalator> getEscalators() {
        return escalators;
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public void addStaircase(Staircase staircase) {
        staircases.add(staircase);
    }

    public void addEscalator(Escalator escalator) {
        escalators.add(escalator);
    }

    public void populateTiledMap() throws MCGADatabaseException {
        TiledMap map = null;

        map = IndoorMapFactory.getInstance().createTiledMap(building, floorNumber);
        IndoorMapFactory.getInstance().insertWalkablePaths(building, floorNumber, map);

        setMap(map);
    }

    public void clearTiledMap() {
        map = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Floor floor = (Floor) o;

        return new EqualsBuilder()
                .append(floorNumber, floor.floorNumber)
                .append(building, floor.building)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(building)
                .append(floorNumber)
                .toHashCode();
    }
}
