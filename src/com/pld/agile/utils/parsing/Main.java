package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/resources/fichiersXML2020/smallMap.xml";
        MapData mapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, mapData);
        boolean success = mapLoader.load();

        System.out.println("SUCCESS : " + success);
        System.out.println(mapData);

        TourData tourData = new TourData();
        tourData.setAssociatedMap(mapData);
        RequestLoader requestsLoader = new RequestLoader("src/resources/fichiersXML2020/requestsSmall1.xml", tourData);
        success = requestsLoader.load();

        System.out.println("SUCCESS : " + success);
        System.out.println(tourData);

    }
}
