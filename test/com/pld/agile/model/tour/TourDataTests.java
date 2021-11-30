package com.pld.agile.model.tour;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.Window;
import org.junit.jupiter.api.Test;

public class TourDataTests {
    private final MapData mapData = new MapData ();
    private MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private final TourData tourDataOriginal = new TourData();
    private final RequestLoader requestLoader = new RequestLoader("test/resources/computeTour_notOptimalTour.xml", tourDataOriginal);
    private TourData tourData;

    @Test
    //Test n°3.1
    public void testNotOptimalTour (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        mapLoader.load();
        requestLoader.load();

        tourData.setStops();



    }

    @Test
    //Test n°3.2
    public void testRedundancy (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        //success = requestLoader.load();

        //to complete

        if(tourData.toString().compareTo(tourDataOriginal.toString())==0){
            System.out.println("Success");
        }else{
            System.out.println("Fail");
        }
    }

    @Test
    //Test n°3.3
    public void testTimeOut (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        //success = requestLoader.load();


    }


}