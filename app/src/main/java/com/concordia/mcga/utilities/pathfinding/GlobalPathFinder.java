package com.concordia.mcga.utilities.pathfinding;

import com.concordia.mcga.models.Floor;
import com.concordia.mcga.models.IndoorMapTile;
import com.concordia.mcga.models.POI;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public class GlobalPathFinder implements Runnable {
    private final POI startPOI;
    private final POI destPOI;

    GlobalPathFinder(POI startPOI, POI destPOI){
        this.startPOI = startPOI;
        this.destPOI = destPOI;
    }

    @Override
    public void run() {

    }

    /**
     * Returns building directions for the indoor start building
     *
     * @return An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code> <b>Returns null if there is no start indoor directions</b>
     */
    public Map<Floor, List<IndoorMapTile>> getStartBuildingDirections() {
        return null;
    }

    /**
     * Returns building directions for the indoor destination building
     *
     * @return An ordered Map, containing {@link Floor} elements as keys, with their associated
     * ordered routes as values. <br/> e.g. <br/> {<br/> Floor1 : [PathfinderTile(1,1),
     * PathfinderTile(1,2)]<br/> Floor2 : [IndoorMapTile(2,2), IndoorMapTile(2,3)]<br/> }
     * </code> <b>Returns null if there is no dest indoor directions</b>
     */
    public Map<Floor, List<IndoorMapTile>> getDestBuildingDirections() {
        return null;
    }

    /**
     * @return A LatLng Array, where index 0 is the start of the outdoor component
     *         and index 1 is the destination of the outdoor component
     */
    public LatLng[] getOutDoorCoordinates() {
        return null;
    }
}
