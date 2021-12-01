/*
 * RequestLoader
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.SyntaxException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalTime;

/**
 * Loads requests model entities from an XML file.
 */
public class RequestLoader {
    /**
     * Path to the XML file.
     */
    final private String requestsFilePath;
    /**
     * DOM document of the loaded XML file.
     */
    private Document tourXmlDocument;
    /**
     * The MapData model object to fill.
     */
    private TourData tour;

    /**
     * RequestsLoader constructor.
     * @param requestsFilePath the path to the xml file
     * @param tour the TourData object to fill
     */
    public RequestLoader(String requestsFilePath, TourData tour) {
        this.requestsFilePath = requestsFilePath;
        this.tour = tour;
    }

    /**
     * Coordinates the entire parsing process - only method to call to fill the provided TourData.
     */
    public void load() throws SyntaxException, IOException {
        try {
            generateDocument();
        } catch (DocumentException e) {
            // invalid XML file
            e.printStackTrace();
            throw new IOException("Invalid specified file.");
        }
        // DOM can be handled
        try {
            List<Node> requestNodes = tourXmlDocument.selectNodes("/planningRequest/request");
            Node warehouseNode = tourXmlDocument.selectNodes("/planningRequest/depot").get(0);

            List<Request> requestList = new ArrayList<>();
            HashMap<Integer, Stop> stopMap = new HashMap<>();

            Element warehouseElement = (Element) warehouseNode;
            String[] time = warehouseElement.attributeValue("departureTime").split(":");
            LocalTime departureTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
            tour.setDepartureTime(departureTime);
            Intersection warehouseLocation = tour.getAssociatedMap().getIntersectionsByOldID().get(warehouseElement.attributeValue("address"));
            Stop warehouse = new Stop(null, warehouseLocation, 0, StopType.WAREHOUSE);
            tour.setWarehouse(warehouse);
            stopMap.put(warehouse.getAddress().getId(), warehouse);

            for (Node requestNode : requestNodes) {
                Element requestElement = (Element) requestNode;
                Intersection pickupLocation = tour.getAssociatedMap().getIntersectionsByOldID().get(requestElement.attributeValue("pickupAddress"));
                long pickupDuration = Long.parseLong(requestElement.attributeValue("pickupDuration"));
                Intersection deliveryLocation = tour.getAssociatedMap().getIntersectionsByOldID().get(requestElement.attributeValue("deliveryAddress"));
                long deliveryDuration = Long.parseLong(requestElement.attributeValue("deliveryDuration"));
                Request request = new Request();
                Stop pickup = new Stop(request, pickupLocation, pickupDuration, StopType.PICKUP);
                Stop delivery = new Stop(request, deliveryLocation, deliveryDuration, StopType.DELIVERY);
                stopMap.put(pickup.getAddress().getId(), pickup);
                stopMap.put(delivery.getAddress().getId(), delivery);
                request.setPickup(pickup);
                request.setDelivery(delivery);
                requestList.add(request);
            }

            tour.setRequestList(requestList);
            tour.setStopMap(stopMap);

        } catch (Exception e) {
            // parsing exception happens when an attribute is missing or invalid
            throw new SyntaxException("Invalid XML file : invalid or missing attributes.");
        }
    }

    /**
     * Generates the DOM document from the file at the provided path.
     * @throws DocumentException when the file was invalid
     */
    private void generateDocument() throws DocumentException {
        File xmlFile = new File(requestsFilePath);
        SAXReader xmlReader = new SAXReader();
        tourXmlDocument = xmlReader.read(xmlFile);
    }

}
