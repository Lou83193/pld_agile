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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class RequestLoaderTests {
    private final MapData mapData = new MapData();
    private final MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private final TourData tourData = new TourData();
    private RequestLoader requestLoader = null;

    /*@BeforeEach
    public void loadMap (){
        try {
            mapLoader.load();
            tourData.setAssociatedMap(mapData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TourData generateRequestList (double[] array){
        List<Request> tmpReqList = new ArrayList<Request>();
        int offset = 0;
        TourData res = new TourData();
        for (int i = 0; i<(array.length/6)-1; ++i){
            Request tmpReq = new Request();
            Stop tmpPickup = null;
            Stop tmpDelivery = null;
            for (int j = offset;j<offset+1 ; j++) {
                tmpPickup = new Stop(tmpReq, new Intersection((int) array[j], array[j + 1], array[j + 2]), (long) array[j + 3], StopType.PICKUP);
                tmpDelivery = new Stop(tmpReq, new Intersection((int) array[j + 4], array[j + 5], array[j + 6]), (long) array[j + 7], StopType.PICKUP);
            }
            offset += 8;
            tmpReq.setPickup(tmpPickup);
            tmpReq.setDelivery(tmpDelivery);
            tmpReqList.add(tmpReq);
        }
        res.setRequestList(tmpReqList);
        return res;
    }
    @Test
    //Test nb 2.1
    public void test3Requests() {
        requestLoader = new RequestLoader("test/resources/loadRequests_3Requests.xml", tourData);
        try {
            requestLoader.load();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[] expectedRequestArray = {
                0,45.0, 4.0, 360.0,
                1,45.0,4.00128,480.0,
                2,45.0009, 4.0, 480.0,
                3,45.0, 3.99872, 40.0,
                4,44.9991, 4.0, 180.0,
                1,45.0,4.00128,540.0
        };

        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();
        assertEquals(expectedResult,tourData.getRequestList().toString());
    }

    @Test
    //Test nb 2.2
    public void testNotDepotAddress() {
        requestLoader = new RequestLoader("test/resources/loadRequests_notDepotAddress.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    //Test nb 2.3
    public void testDepotMissing() {
        requestLoader = new RequestLoader("test/resources/loadRequests_depotMissing.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    //Test nb 2.4
    public void testDepotAddressMissing() {
        requestLoader = new RequestLoader("test/resources/loadRequests_depotAddressMissing.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    //Test nb 2.5
    public void testDepartureTimeMissing() {
        requestLoader = new RequestLoader("test/resources/loadRequests_departureTimeMissing.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);
    }

    @Test
    //Test nb 2.6
    public void testNotStopAddress() {
        requestLoader = new RequestLoader("test/resources/loadRequests_notStopAddress.xml", tourData);
        assertThrows(SyntaxException.class, requestLoader::load);

        double[] expectedRequestArray = {
                2,45.0009,4.0,480.0,
                3,45.0,3.99872,40.0,
                4,44.9991,4.0,540.0,
                1,45.0,4.00128,480.0,
        };

        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();
        assertEquals(expectedResult,tourData.getRequestList().toString());


    }

    @Test
    //Test nb 2.7
    public void testStopAddressMissing() {
        requestLoader = new RequestLoader("test/resources/loadRequests_notStopAddress.xml", tourData);
        assertThrows(Exception.class, requestLoader::load);
        double[] expectedRequestArray = {
                0,45.0, 4.0, 360.0,
                1,45.0,4.00128,480.0,
                4,44.9991, 4.0, 180.0,
                1,45.0,4.00128,540.0
        };

        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();
        assertEquals(expectedResult,tourData.getRequestList().toString());
        assertThrows(Exception.class, requestLoader::load);

    }


    @Test
    //Test nb 2.8
    public void testStopDurationMissing() {
        requestLoader = new RequestLoader("test/resources/loadRequests_stopDurationMissing.xml", tourData);

        double[] expectedRequestArray = {
                0,45.0, 4.0, 360.0,
                1,45.0,4.00128,480.0,
                4,44.9991, 4.0, 180.0,
                1,45.0,4.00128,540.0
        };

        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();
        assertEquals(expectedResult,tourData.getRequestList().toString());
        assertThrows(Exception.class, requestLoader::load);

    }

    @Test
    //Test nb 2.9
    public void testNotStopDuration() {
        requestLoader = new RequestLoader("test/resources/loadRequests_notStopDuration.xml", tourData);

        double[] expectedRequestArray = {
                0,45.0, 4.0, 360.0,
                1,45.0,4.00128,480.0,
                4,44.9991, 4.0, 180.0,
                1,45.0,4.00128,540.0
        };

        assertThrows(Exception.class, requestLoader::load);
        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();
        assertEquals(expectedResult,tourData.getRequestList().toString());
    }

    @Test
    //Test nb 2.10
    public void testNoPlanningRequestNode() {
        requestLoader = new RequestLoader("test/resources/loadRequests_noPlanningRequestNode.xml", tourData);
        assertThrows(Exception.class, requestLoader::load);
    }

    @Test
    //Test nb 2.11
    public void testNoFile() {
        requestLoader = new RequestLoader("test/resources/loadRequests_noFile.xml", tourData);
        assertThrows(IOException.class, requestLoader::load);
    }

    @Test
    //Test nb 2.12
    public void testNoRequestNode() {
        requestLoader = new RequestLoader("test/resources/loadRequests_noRequestNode.xml", tourData);
        assertThrows(Exception.class, requestLoader::load);
    }*/

}
