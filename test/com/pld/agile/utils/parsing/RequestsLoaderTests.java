package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import org.junit.jupiter.api.Test;


public class RequestsLoaderTests {
    private final MapData mapData = new MapData();
    private boolean success;
    private final TourData tourData = new TourData();
    private RequestLoader requestsLoader = null;

    @Test
    //Test n°2.1
    public void test3Requests() {
        tourData.setAssociatedMap(mapData);
        requestsLoader= new RequestLoader("src/resources/fichiersXML2020/smallMap.xml", tourData);
        success = requestsLoader.load();

        if(success) {
            for (int i = 0; i < tourData.getRequestList().size();i++) {
                if (tourData.getRequestList().get(i).getPickup().getAddress() != null && tourData.getRequestList().get(i).getDelivery().getAddress() != null){
                    System.out.println("SUCCESS : " + success);
                }else {
                    System.out.println("FAIL : " + success);
                }
            }
        }
        System.out.println(tourData);
    }

    @Test
    //Test n°2.2
    public void testRedundancy() {
        requestsLoader = new RequestLoader("test/resources/loadRequests_redundancy.xml", tourData);
        success = requestsLoader.load();

        if(success) {
            for (int i = 0; i < tourData.getRequestList().size();i++) {
                if (tourData.getRequestList().get(i).getPickup().getAddress() != null && tourData.getRequestList().get(i).getDelivery().getAddress() != null){
                    System.out.println("SUCCESS : " + success);
                }else {
                    System.out.println("FAIL");
                }
            }
        }
        System.out.println(tourData);
    }

    @Test
    //Test n°2.3
    public void testDepotAddressMissing() {
        requestsLoader = new RequestLoader("test/resources/loadRequests_depotAddressMissing.xml", tourData);
        success = requestsLoader.load();

        if(success) {
            for (int i = 0; i < tourData.getRequestList().size();i++) {
                if (tourData.getWarehouse().getAddress() !=null && tourData.getRequestList().get(i).getPickup().getAddress() != null && tourData.getRequestList().get(i).getDelivery().getAddress() != null){
                    System.out.println("SUCCESS : " + success);
                }else {
                    System.out.println("FAIL");
                }
            }
        }
        System.out.println(tourData);
    }

    @Test
    //Test n°2.4
    public void testPickupMissing() {
        requestsLoader = new RequestLoader("test/resources/loadRequests_pickupMissing.xml", tourData);
        success = requestsLoader.load();

        if(success) {
            for (int i = 0; i < tourData.getRequestList().size();i++) {
                if (tourData.getRequestList().get(i).getPickup().getAddress() != null && tourData.getRequestList().get(i).getDelivery().getAddress() != null){
                    System.out.println("SUCCESS : " + success);
                }else {
                    System.out.println("FAIL");
                }
            }
        }
        System.out.println(tourData);
    }


}
