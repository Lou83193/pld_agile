package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.SyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class RequestLoaderTests {
    private final MapData mapData = new MapData();
    private final MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private TourData tourData = new TourData();
    private RequestLoader requestLoader = null;

    public void loadMap (String filePath){
        try {
            mapLoader.load();
            tourData.setAssociatedMap(mapData);
            requestLoader = new RequestLoader(filePath,tourData);
        } catch (Exception e) {}
    }


    @Test
    public void test3Requests() {
        loadMap("test/resources/loadRequests_3Requests.xml");
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stop.resetIdCounter();
        Intersection.resetIdCounter();
        Intersection start = new Intersection(45,4.0);
        Intersection inter2 = new Intersection(45,4.00128);
        Intersection inter3 = new Intersection(45.0009,4.0);
        Intersection inter4 = new Intersection(45.0,3.99872);
        Intersection inter5 = new Intersection(44.9991,4.0);
        Request req1 = new Request();
        Request req2 = new Request();
        Request req3 = new Request();
        Stop warehouse = new Stop(null,start,0,StopType.WAREHOUSE);
        Stop stop1 =  new Stop(req1,start,360,StopType.PICKUP);
        Stop stop2 =  new Stop(req1,inter2,480,StopType.DELIVERY);
        Stop stop3 =  new Stop(req2,inter3,480,StopType.PICKUP);
        Stop stop4 =  new Stop(req2,inter4,40,StopType.DELIVERY);
        Stop stop5 =  new Stop(req3,inter5,180,StopType.PICKUP);
        Stop stop6 =  new Stop(req3,inter2,680,StopType.DELIVERY);

        List<Stop> stops = new ArrayList<>();
        stops.add(warehouse);
        stops.add(stop1);
        stops.add(stop2);
        stops.add(stop3);
        stops.add(stop4);
        stops.add(stop5);
        stops.add(stop6);

        assertEquals(stops.toString(),tourData.getStopsList().toString());
    }

    @Test
    public void testNotDepotAddress() {
        loadMap("test/resources/loadRequests_notDepotAddress.xml");
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    public void testDepotMissing() {
        loadMap("test/resources/loadRequests_depotMissing.xml");
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    public void testDepotAddressMissing() {
        loadMap("test/resources/loadRequests_depotAddressMissing.xml");
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    public void testDepartureTimeMissing() {
        loadMap("test/resources/loadRequests_departureTimeMissing.xml");
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    public void testNotStopAddress() {
        loadMap("test/resources/loadRequests_notStopAddress.xml");
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stop.resetIdCounter();
        Intersection.resetIdCounter();
        Intersection start = new Intersection(45,4.0);
        Intersection inter2 = new Intersection(45,4.00128);
        Intersection inter3 = new Intersection(45.0009,4.0);
        Intersection inter4 = new Intersection(45.0,3.99872);
        Intersection inter5 = new Intersection(44.9991,4.0);
        Request req2 = new Request();
        Request req3 = new Request();
        Stop warehouse = new Stop(null,start,0,StopType.WAREHOUSE);
        Stop stop3 =  new Stop(req2,inter3,480,StopType.PICKUP);
        Stop stop4 =  new Stop(req2,inter4,40,StopType.DELIVERY);
        Stop stop5 =  new Stop(req3,inter5,180,StopType.PICKUP);
        Stop stop6 =  new Stop(req3,inter2,540,StopType.DELIVERY);

        List<Stop> stops = new ArrayList<>();
        stops.add(warehouse);
        stops.add(stop3);
        stops.add(stop4);
        stops.add(stop5);
        stops.add(stop6);

        assertEquals(stops.toString(),tourData.getStopsList().toString());
    }

    @Test
    public void testStopAddressMissing() {
        loadMap("test/resources/loadRequests_stopAddressMissing.xml");
        System.out.println(tourData.getStopsList());
        assertTrue(tourData.getStopsList().isEmpty());
    }

    @Test
    public void testStopDurationMissing() {
        requestLoader = new RequestLoader("test/resources/loadRequests_stopDurationMissing.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    public void testNotStopDuration() {
        requestLoader = new RequestLoader("test/resources/loadRequests_notStopDuration.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    public void testNoPlanningRequestNode() {
        requestLoader = new RequestLoader("test/resources/loadRequests_noPlanningRequestNode.xml", tourData);
        assertThrows(Exception.class, requestLoader::load);
    }

    @Test
    public void testNoFile() {
        requestLoader = new RequestLoader("test/resources/loadRequests_noFile.xml", tourData);
        assertThrows(IOException.class, requestLoader::load);
    }

    @Test
    public void testNoRequestNode() {
        loadMap("test/resources/loadRequests_noRequestNode.xml");
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(tourData.getStopsList().size()>1);
    }

}
