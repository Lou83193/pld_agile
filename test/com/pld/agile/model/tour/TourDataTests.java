package com.pld.agile.model.tour;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.parsing.RequestLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TourDataTests {
    private final MapData mapData = new MapData ();
    private final MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private TourData tourDataInit = new TourData();
    private RequestLoader requestLoader;
    private TourData tourData = new TourData();;

    @BeforeEach
    public void loadMap (){
        try {
            mapLoader.load();
            tourDataInit.setAssociatedMap(mapData);
            tourData.setAssociatedMap(mapData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    //Test n°3.1
    public void testNotOptimalTour (){
        requestLoader = new RequestLoader("test/resources/computeTour_notOptimalTour.xml", tourDataInit);
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tourDataInit.setStops();
        tourData = tourDataInit;
        tourData.computeTour();

        assertFalse(tourData.getStops().toString().equals(tourDataInit.getStops().toString()));

    }
/*
    @Test
    //Test n°3.2
    public void testTimeOut (){
        tourDataInit.setAssociatedMap(mapData);
        tourData = tourDataInit;
        requestLoader.load();

        //trouver un fichier qui génère un time out ?
    }
*/

}