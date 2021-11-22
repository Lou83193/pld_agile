package com.pld.agile.model.tour;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.parsing.RequestLoader;
import org.junit.jupiter.api.Test;

class TourDataTest {
    private final MapData mapData = new MapData ();
    private boolean success;
    private final TourData tourDataOriginal = new TourData();
    private final RequestLoader requestLoader = new RequestLoader("test/resources/loadRequests_3Requests", tourDataOriginal);
    private TourData tourData;

    @Test
    //Test n°3.1
    public void testComputeTour_3Requests (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        success = requestLoader.load();
    }

    @Test
    //Test n°3.2
    public void testComputeTour_redundancy (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        success = requestLoader.load();
    }

}