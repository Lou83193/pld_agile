package com.pld.agile.model.tour;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.parsing.RequestLoader;
import org.junit.jupiter.api.Test;

public class TourDataTests {
    private final MapData mapData = new MapData ();
    private boolean success;
    private final TourData tourDataOriginal = new TourData();
    private final RequestLoader requestLoader = new RequestLoader("test/resources/loadRequests_3Requests.xml", tourDataOriginal);
    private TourData tourData;

    @Test
    //Test n°3.1
    public void test3Requests (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        success = requestLoader.load();

        //to complete

        if(tourData.toString().compareTo(tourDataOriginal.toString())==0){
            System.out.println("Success");
        }else{
            System.out.println("Fail");
        }
    }

    @Test
    //Test n°3.2
    public void testRedundancy (){
        tourDataOriginal.setAssociatedMap(mapData);
        tourData = tourDataOriginal;
        success = requestLoader.load();

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
        success = requestLoader.load();
    }


}