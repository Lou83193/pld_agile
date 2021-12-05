/*
 * TourDataTests
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.exception.PathException;
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


public class TourDataTest {
    private final MapData mapData = new MapData ();
    private final MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private RequestLoader requestLoader;
    private TourData tourData = new TourData();

    @BeforeEach
    public void loadMap (){
        try {
            mapLoader.load();
            tourData.setAssociatedMap(mapData);
            requestLoader = new RequestLoader("test/resources/computeTour_notOptimalTour.xml", tourData);
            requestLoader.load();
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

    /*
    @Test
    public void testUnHighlightStop(){
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
    */

    @Test
    public void testDeleteRequest (){
        try {
            tourData.computeTour();
        } catch (Exception e) {}
        tourData.deleteRequest(tourData.getRequestList().get(1));
        assertEquals(2,tourData.getRequestList().size());
    }
    @Test
    public void testAddRequest () {
        try {
            tourData.computeTour();
        } catch (Exception e) {}
        Intersection pickupAddress = mapData.getIntersections().get(3);
        Intersection deliveryAddress = mapData.getIntersections().get(4);
        Request newRequest = new Request ();
        Stop pickup = new Stop (newRequest,pickupAddress,0,StopType.PICKUP);
        Stop delivery = new Stop (newRequest,deliveryAddress,0,StopType.DELIVERY);
        newRequest.setDelivery(delivery);
        newRequest.setPickup(pickup);

        tourData.getRequestList().add(newRequest);
        try {
            tourData.addRequest(newRequest);
        } catch (Exception e) {}

        //stops
        assertEquals(newRequest.getDelivery().getAddress().getId(),tourData.getStops().get(tourData.getStops().size()-1));
        //stopsMap
        assertEquals(newRequest.getDelivery(),tourData.getStopMap().get(newRequest.getDelivery().getAddress().getId()));
    }
    @Test
    public void testConstructNewRequest(){
        try {
            tourData.computeTour();
        } catch (Exception e) {}
        Intersection pickupAddress = mapData.getIntersections().get(3);
        Intersection deliveryAddress = mapData.getIntersections().get(4);
        Request newRequest = new Request ();
        Stop pickup = new Stop (newRequest,pickupAddress,0,StopType.PICKUP);
        Stop delivery = new Stop (newRequest,deliveryAddress,0,StopType.DELIVERY);
        newRequest.setDelivery(delivery);
        newRequest.setPickup(pickup);

        tourData.constructNewRequest1(pickupAddress);
        try {
            tourData.constructNewRequest2(deliveryAddress);
        } catch (PathException e) {}

        assertEquals(newRequest.toString(),tourData.getRequestList().get(tourData.getRequestList().size()-1).toString());

    }

    @Test
    public void testStopIsShiftable() {
        try {
            tourData.computeTour();
        } catch (Exception e) {}
        Stop stop = tourData.getTourPaths().get(1).getDestination();
        assertTrue(tourData.stopIsShiftable(stop,2));
    }

    @Test
    public void testShiftStopOrder() {
        try {
            tourData.computeTour();
        } catch (Exception e) {}
        Stop stop = tourData.getTourPaths().get(1).getDestination();
        assertTrue(tourData.shiftStopOrder(stop,2));
    }

    @Test
    public void testComputeTour (){

        int[] stops = new int[(tourData.getRequestList().size()*2)+1];
        stops [0] = tourData.getWarehouse().getAddress().getId();
        for (int i = 1; i<stops.length-1;i++){
            stops[i] = tourData.getRequestList().get(i).getPickup().getAddress().getId();
            stops[i+1] = tourData.getRequestList().get(i).getDelivery().getAddress().getId();
        }
        String stopsString = "[";
        for (int i = 0; i<stops.length-2;i++){
            stopsString += stops[i]+", ";
        }
        stopsString += stops[stops.length-1]+"]";
        System.out.println("Avant: "+stopsString);
        try {
            tourData.computeTour();
        } catch (Exception e) {}
        System.out.println("Resultat: "+tourData.getStops());
        assertNotEquals(tourData.getStops().toString(),stopsString);
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