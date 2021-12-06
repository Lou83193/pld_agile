/*
 * TourDataTests
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.controller.Controller;
import com.pld.agile.controller.LoadedRequestsState;
import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.parsing.RequestLoader;

import com.pld.agile.utils.tsp.CompleteGraph;
import com.pld.agile.utils.tsp.Graph;
import com.pld.agile.utils.tsp.TSP3;
import com.pld.agile.view.Window;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
            //compute tour
            List<Path> paths = new ArrayList<>();

            paths.add(new Path(tourData.getWarehouse(),tourData.getStopsList().get(1)));
            paths.add(new Path(tourData.getStopsList().get(1),tourData.getStopsList().get(2)));
            paths.add(new Path(tourData.getStopsList().get(2),tourData.getStopsList().get(3)));
            paths.add(new Path(tourData.getStopsList().get(3),tourData.getStopsList().get(4)));
            paths.add(new Path(tourData.getStopsList().get(4),tourData.getStopsList().get(5)));
            paths.add(new Path(tourData.getStopsList().get(5),tourData.getStopsList().get(6)));
            paths.add(new Path(tourData.getStopsList().get(6),tourData.getWarehouse()));
            tourData.setTourPaths(paths);

            Graph stopsGraph = new CompleteGraph(tourData.getStopsList().size());
            for (int i = 0; i<tourData.getStopsList().size();i++) {
                for (int j = 0; j<tourData.getStopsList().size(); j++){
                    if(i!=j){
                        Path path = new Path(tourData.getStopsList().get(i),tourData.getStopsList().get(j));
                        stopsGraph.setPath(i, j,path);
                        stopsGraph.setCost(i, j, path.getLength());
                    }
                }
            }
            tourData.setStopsGraph(stopsGraph);
        } catch (Exception e) {
        }
    }


    @Test
    public void testTourData(){
        TourData tourData = new TourData();
        assertEquals(new ArrayList<>(),tourData.getStopsList());
        assertEquals(new ArrayList<>(),tourData.getTourPaths());
        assertNull(tourData.getAssociatedMap());
        assertNull(tourData.getDepartureTime());
        assertNull(tourData.getWarehouse());
    }

    @Test
    public void testDeleteRequest (){
        tourData.deleteRequest(tourData.getStopsList().get(1).getRequest());
        assertEquals(5,tourData.getStopsList().size());
    }

    @Test
    public void testAddRequest () {
        Intersection pickupAddress = mapData.getIntersections().get(3);
        Intersection deliveryAddress = mapData.getIntersections().get(4);
        Request newRequest = new Request ();
        Stop pickup = new Stop (newRequest,pickupAddress,0,StopType.PICKUP);
        Stop delivery = new Stop (newRequest,deliveryAddress,0,StopType.DELIVERY);
        newRequest.setPickup(pickup);
        newRequest.setDelivery(delivery);

        //tourData.getRequestList().add(newRequest);
        try {
            tourData.getStopsList().add(pickup);
            tourData.getStopsList().add(delivery);
            tourData.recomputeStopIDs();

            tourData.addLatestRequest();
        } catch (Exception e) {}

        //stopsList
        assertEquals(newRequest.getDelivery().getId(),tourData.getStopsList().get(tourData.getStopsList().size()-1).getId());
    }

    @Test
    public void testConstructNewRequest(){
        Intersection pickupAddress = mapData.getIntersections().get(3);
        Intersection deliveryAddress = mapData.getIntersections().get(4);
        Request newRequest = new Request ();
        Stop pickup = new Stop (newRequest,pickupAddress,0,StopType.PICKUP);
        Stop delivery = new Stop (newRequest,deliveryAddress,0,StopType.DELIVERY);
        newRequest.setDelivery(delivery);
        newRequest.setPickup(pickup);

        tourData.constructNewRequest1(pickupAddress);
        tourData.constructNewRequest2(deliveryAddress);

        tourData.recomputeStopIDs();

        assertEquals(newRequest.toString(),tourData.getStopsList().get(tourData.getStopsList().size()-1).getRequest().toString());

    }

    @Test
    public void testStopIsShiftable() {
        Stop stop = tourData.getStopsList().get(2);
        assertTrue(tourData.stopIsShiftable(stop,1));
        assertFalse(tourData.stopIsShiftable(stop,-1));
    }

    @Test
    public void testShiftStopOrder() {
        Stop stop = tourData.getTourPaths().get(3).getDestination();
        assertFalse(tourData.shiftStopOrder(stop,-1));
        assertTrue(tourData.shiftStopOrder(stop,1));

    }

    @Test
    public void testStopComputingTour() {
        TourData tourDataEmpty = new TourData();
        assertFalse(tourDataEmpty.stopComputingTour());
        tourData.setTourComputingThread(new Thread());
        assertTrue(tourData.stopComputingTour());
    }

}