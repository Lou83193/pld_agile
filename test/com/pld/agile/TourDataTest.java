package com.pld.agile;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.parsing.RequestLoader;
import org.junit.jupiter.api.Test;

class TourDataTest {
    private String filePath = "test/resources/loadMap_loadRequestsBase";
    private MapData mapData = new MapData ();
    private MapLoader mapLoader = new MapLoader(filePath, mapData);
    private boolean success = mapLoader.load();
    private TourData tourData = new TourData();
    private RequestLoader requestLoader = new RequestLoader("test/resources/loadRequests_3Requests", tourData);


    @Test
    //Test n°3.1
    public void testComputeTour_3Requests (){
        tourData.setAssociatedMap(mapData);
        success = requestLoader.load();
    }

    @Test
    //Test n°3.2
    public void testComputeTour_redundancy (){
        tourData.setAssociatedMap(mapData);
        success = requestLoader.load();
    }

}