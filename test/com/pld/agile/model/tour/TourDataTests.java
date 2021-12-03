/*
 * TourDataTests
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.parsing.RequestLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TourDataTests {
    private final MapData mapData = new MapData ();
    private final MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private TourData tourDataInit = new TourData();
    private RequestLoader requestLoader;
    private TourData tourData = new TourData();

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
    public void testTourData(){
        TourData tourData = new TourData();
        assertEquals(new ArrayList<>(),tourData.getRequestList());
        assertEquals(new ArrayList<>(),tourData.getTourPaths());
        assertNull(tourData.getAssociatedMap());
        assertNull(tourData.getDepartureTime());
        assertNull(tourData.getWarehouse());
    }

    @Test
    public void testUnHighlightStop(){
        requestLoader = new RequestLoader("test/resources/computeTour_notOptimalTour.xml", tourData);
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Request r:tourData.getRequestList()) {
            r.getPickup().setHighlighted(1);
            r.getDelivery().setHighlighted(1);
        }
        tourData.unHighlightStops();
        int sum = 0;
        for (Request r:tourData.getRequestList()) {
            sum += r.getPickup().getHighlighted();
            sum += r.getDelivery().getHighlighted();
        }
        assertEquals(0,sum);
    }

    @Test
    public void testDeleteRequest (){
        requestLoader = new RequestLoader("test/resources/computeTour_notOptimalTour.xml", tourData);
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tourData.deleteRequest(tourData.getRequestList().get(1));

        assertThrows(IndexOutOfBoundsException.class,()-> tourData.getRequestList().get(1));

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
        //tourDataInit.setStops();
        tourData = tourDataInit;
        tourData.computeTour();

        assertNotEquals(tourData.getStops().toString(), tourDataInit.getStops().toString());
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