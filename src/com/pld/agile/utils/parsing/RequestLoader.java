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
import java.util.List;
import java.time.LocalTime;
import java.util.Map;

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
            Map<String, Intersection> interectionsByOldId = tour.getAssociatedMap().getIntersectionsByOldID();

            List<Node> requestNodes = tourXmlDocument.selectNodes("/planningRequest/request");
            Node warehouseNode = tourXmlDocument.selectNodes("/planningRequest/depot").get(0);

            List<Stop> stopList = new ArrayList<>();
            Stop.resetIdCounter();

            Element warehouseElement = (Element) warehouseNode;
            String[] time = warehouseElement.attributeValue("departureTime").split(":");
            LocalTime departureTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
            tour.setDepartureTime(departureTime);
            if (!interectionsByOldId.containsKey(warehouseElement.attributeValue("address"))) {
                // the warehouse isn't on the map
                throw new SyntaxException("The warehouse is on an adress that doesn't exist on the loaded map.");
            }
            Intersection warehouseLocation = interectionsByOldId.get(warehouseElement.attributeValue("address"));
            Stop warehouse = new Stop(null, warehouseLocation, 0, StopType.WAREHOUSE);
            tour.setWarehouse(warehouse);
            stopList.add(warehouse);

            for (Node requestNode : requestNodes) {
                Element requestElement = (Element) requestNode;
                if (interectionsByOldId.containsKey(requestElement.attributeValue("pickupAddress")) && interectionsByOldId.containsKey(requestElement.attributeValue("deliveryAddress"))) {
                    // we only take a request into account if both its stops are on known intersections
                    Intersection pickupLocation = interectionsByOldId.get(requestElement.attributeValue("pickupAddress"));
                    long pickupDuration = Long.parseLong(requestElement.attributeValue("pickupDuration"));
                    Intersection deliveryLocation = interectionsByOldId.get(requestElement.attributeValue("deliveryAddress"));
                    long deliveryDuration = Long.parseLong(requestElement.attributeValue("deliveryDuration"));
                    Request request = new Request();
                    Stop pickup = new Stop(request, pickupLocation, pickupDuration, StopType.PICKUP);
                    Stop delivery = new Stop(request, deliveryLocation, deliveryDuration, StopType.DELIVERY);
                    request.setPickup(pickup);
                    request.setDelivery(delivery);
                    stopList.add(pickup);
                    stopList.add(delivery);

                }
            }

            tour.setStopsList(stopList);
        } catch(SyntaxException e) {
            throw e;
        } catch (Exception e) {
            // parsing exception happens when an attribute is missing or invalid
            e.printStackTrace();
            throw new SyntaxException("Invalid XML file : invalid or missing attributes.");
        }

        if (tour.getStopsList().size() == 0) {
            throw new SyntaxException("Invalid file - couldn't use it to load a request (all might be out loaded map).");
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
