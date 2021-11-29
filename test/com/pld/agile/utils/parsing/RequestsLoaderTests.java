package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class RequestsLoaderTests {
    private final MapData mapData = new MapData();
    private MapLoader mapLoader = new MapLoader("test/resources/loadMap_loadRequestsBase.xml", mapData);
    private final TourData tourData = new TourData();
    private RequestLoader requestsLoader = null;

    public TourData generateRequestList (double[] array){
        List<Request> tmpReqList = new ArrayList<Request>();
        int offset = 0;
        TourData res = new TourData();
        for (int i = 0; i<(array.length/6)-1; ++i){
            Request tmpReq = new Request();
            Stop tmpPickup = null;
            Stop tmpDelivery = null;
            for (int j = offset;j<offset+1 ; j++) {
                tmpPickup = new Stop(tmpReq, new Intersection((int) array[j], array[j + 1], array[j + 2]), array[j + 3], StopType.PICKUP);
                tmpDelivery = new Stop(tmpReq, new Intersection((int) array[j + 4], array[j + 5], array[j + 6]), array[j + 7], StopType.PICKUP);
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
    //Test n°2.1
    public void test3Requests() {
        tourData.setAssociatedMap(mapData);
        requestsLoader= new RequestLoader("test/resources/loadRequests_3Requests.xml", tourData);
        mapLoader.load();
        requestsLoader.load();

        double[] expectedRequestArray = {
                0,45.0, 4.0, 360.0,
                1,45.0,4.00128,480.0,
                2,45.0009, 4.0, 480.0,
                3,45.0, 3.99872, 0.0,
                4,44.9991, 4.0, 180.0,
                1,45.0,4.00128,540.0
        };

        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();
        assertEquals(expectedResult,tourData.getRequestList().toString());
    }

    @Test
    //Test n°2.2
    public void testRedundancy() {
        tourData.setAssociatedMap(mapData);
        requestsLoader = new RequestLoader("test/resources/loadRequests_redundancy.xml", tourData);
        mapLoader.load();
        requestsLoader.load();

        double[] expectedRequestArray = {
                0,45.0,4.0,360.0,
                1,45.0,4.00128,480.0,
                2,45.0009,4.0,480.0,
                3,45.0,3.99872,40.0,
                3,45.0,3.99872,180.0,
                4,44.9991,4.0,540.0,
                3,45.0,3.99872,360.0,
                0,45.0,4.0,480.0,
        };

        String expectedResult = generateRequestList(expectedRequestArray).getRequestList().toString();;
        assertEquals(expectedResult,tourData.getRequestList().toString());
    }

    @Test
    //Test n°2.3
    public void testDepotAddressMissing() {
        tourData.setAssociatedMap(mapData);
        requestsLoader = new RequestLoader("test/resources/loadRequests_depotAddressMissing.xml", tourData);
        mapLoader.load();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //redirect the System-output (normally the console) to a variable
        System.setErr(new PrintStream(outContent));
        boolean res = requestsLoader.load();

        assertFalse(res);

        //check if your error message is in the output variable
        //TO-DO

    }

    @Test
    //Test n°2.4
    public void testPickupMissing() {
        tourData.setAssociatedMap(mapData);
        requestsLoader = new RequestLoader("test/resources/loadRequests_notStopAddress.xml", tourData);
        mapLoader.load();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //redirect the System-output (normally the console) to a variable
        System.setErr(new PrintStream(outContent));
        requestsLoader.load();
        String expectedResult = "[Request{pickup=Stop{address=Intersection{id='2', latitude=45.0009, longitude=4.0}, duration=480.0}, delivery=Stop{address=Intersection{id='3', latitude=45.0, longitude=3.99872}, duration=0.0}}, Request{pickup=Stop{address=Intersection{id='4', latitude=44.9991, longitude=4.0}, duration=180.0}, delivery=Stop{address=Intersection{id='1', latitude=45.0, longitude=4.00128}, duration=540.0}}]";
        //check if your error message is in the output variable
        //TO-DO
    }


}
