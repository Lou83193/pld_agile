/*
 * RequestLoader
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
     * @return boolean true if tour has been successfully filled, false if the provided xml file was invalid
     */
    public boolean load() {
        try {
            generateDocument();
        } catch (DocumentException e) {
            // invalid XML file
            e.printStackTrace();
            return false;
        }
        // DOM can be handled
        List<Node> requestNodes = tourXmlDocument.selectNodes("/planningRequest/request");
        Node warehouseNode = tourXmlDocument.selectNodes("/planningRequest/depot").get(0);

        Element warehouseElement = (Element) warehouseNode;
        String departureTime = warehouseElement.attributeValue("departureTime");
        Intersection warehouseLocation = tour.getAssociatedMap().getIntersectionsByOldID().get(warehouseElement.attributeValue("address"));
        tour.setDepartureTime(departureTime);
        tour.setWarehouse(new Stop(warehouseLocation, 0));
        
        List<Request> requestList = new ArrayList<>();
        for (Node requestNode : requestNodes) {
            Element requestElement = (Element) requestNode;
            Intersection pickupLocation = tour.getAssociatedMap().getIntersectionsByOldID().get(requestElement.attributeValue("pickupAddress"));
            double pickupDuration = Double.parseDouble(requestElement.attributeValue("pickupDuration"));
            Intersection deliveryLocation = tour.getAssociatedMap().getIntersectionsByOldID().get(requestElement.attributeValue("deliveryAddress"));
            double deliveryDuration = Double.parseDouble(requestElement.attributeValue("deliveryDuration"));
            Stop pickup = new Stop(pickupLocation, pickupDuration);
            Stop delivery = new Stop(deliveryLocation, deliveryDuration);
            requestList.add(new Request(pickup, delivery));
        }

        tour.setRequestList(requestList);

        return true;
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
