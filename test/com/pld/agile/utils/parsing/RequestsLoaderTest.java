package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import org.junit.jupiter.api.Test;

/*
class RequestsLoaderTest {
    private String filePath = "test/resources/loadMap_loadRequestsBase";
    private MapData mapData = MapData.getInstance();
    private MapLoader mapLoader = new MapLoader(filePath, mapData);
    private boolean success = mapLoader.load();
    @Test

    //Test n°2.1
    public void testLoadRequests_3Requests() {
        TourData tourData = TourData.getInstance();
        tourData.setAssociatedMap(mapData);
        RequestsLoader requestsLoader = new RequestsLoader("test/resources/loadRequests_3Requests", tourData);
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
    public void testLoadRequests_redundancy() {
        TourData tourData = TourData.getInstance();
        tourData.setAssociatedMap(mapData);
        RequestsLoader requestsLoader = new RequestsLoader("test/resources/loadRequests_redundancy.xml", tourData);
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
    //Test n°2.3
    public void testLoadRequests_depotAddressMissing() {
        TourData tourData = TourData.getInstance();
        tourData.setAssociatedMap(mapData);
        RequestsLoader requestsLoader = new RequestsLoader("test/resources/loadRequests_depotAddressMissing.xml", tourData);
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

}
*/