package com.concordia.mcga.factories;

import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.IndoorPOI;
import java.util.ArrayList;
import java.util.List;

public class IndoorPOIFactory {
    static IndoorPOIFactory instance;

    public static IndoorPOIFactory getInstance(){
        if (instance == null){
            instance = new IndoorPOIFactory();
        }
        return instance;
    }

    private IndoorPOIFactory(){}

    public List<IndoorPOI> getIndoorPOIs(Building building, int floorNumber){
        List<IndoorPOI> returnList = new ArrayList<>();
        return returnList;
    }
}
