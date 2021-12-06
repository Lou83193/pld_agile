/*
 * MapLoader
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.exception.SyntaxException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Loads map model entities from an XML file.
 */
public class MapLoader {
    /**
     * Path to the XML file.
     */
    final private String mapFilePath;
    /**
     * DOM document of the loaded XML file.
     */
    private Document mapXmlDocument;
    /**
     * The MapData model object to fill.
     */
    private MapData map;

    /**
     * @param mapFilePath the path to the xml file
     * @param map the MapData object to fill
     */
    public MapLoader(String mapFilePath, MapData map) {
        this.mapFilePath = mapFilePath;
        this.map = map;
    }

    //GETTER
    /**
     * Getter for attribute mapFilePath.
     * @return mapFilePath
     */
    public String getMapFilePath() {
        return mapFilePath;
    }

    /**
     * Getter for attribute getMapXmlDocument.
     * @return getMapXmlDocument
     */
    public Document getMapXmlDocument() {
        return mapXmlDocument;
    }

    /**
     * Getter for attribute getMapXmlDocument.
     * @return getMapXmlDocument
     */
    public MapData getMap() {
        return map;
    }

    /**
     * Adds a segment to the application model : Used to guarantee that there is only one segment of same origin & destination - makes up keep the shortest one in that case.
     * @param segments
     * @param s
     */
    private void addSegmentIfNotRedundant(List<Segment> segments, Segment s) {
        boolean shouldAdd = true;
        // we don't add segments of same origin & dest, of length 0
        shouldAdd = !((s.getOrigin().equals(s.getDestination())) || (s.getLength() == 0));
        Segment toBeRemoved = null;
        for (Segment presentSegment : s.getOrigin().getOriginOf()) { // iterate over all segments with the same origin
            if (presentSegment.getDestination().getId() == s.getDestination().getId() && presentSegment.getLength() <= s.getLength()) {
                shouldAdd = false;
            } else if (presentSegment.getDestination().getId() == s.getDestination().getId()) { // we delete the old segment of same origin & destination since the new one is better and we will add it
                toBeRemoved = presentSegment;
            }
        }

        if (toBeRemoved != null) {
            segments.remove(toBeRemoved);
            toBeRemoved.getOrigin().getOriginOf().remove(toBeRemoved);
        }

        if (shouldAdd) {
            segments.add(s);
            s.getOrigin().getOriginOf().add(s);
        }
    }

    /**
     * Checks if an ID is in a valid form.
     * @param id String
     * @return boolean validId
     */
    private boolean isValidId(String id) {
        try {
            Long.parseLong(id); // make sure it is a valid id
            return true;
        } catch (NumberFormatException e) {
            // we don't add the intersection if it has an invalid id
            return false;
        }
    }

    /**
     * Checks if a geographic coordinate is of valid type.
     * @param coord String
     * @return boolean validCoordinate
     */
    private boolean isValidCoordinate(String coord) {
        try {
            if (coord == null) return false;
            Double.parseDouble(coord);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Coordinates the entire parsing process - only method to call to fill the provided map.
     * @return boolean true if map has been successfully filled, false if the provided xml file was invalid
     */
    public void load() throws IOException, SyntaxException {
        try {
            generateDocument();
        } catch (DocumentException e) {
            // invalid XML file
            e.printStackTrace();
            throw new IOException("Invalid specified file.");
        }
        // DOM can be handled
        try {
            List<Node> intersectionNodes = mapXmlDocument.selectNodes("/map/intersection");

            map.resetBounds();
            HashMap<String, Intersection> intersectionsById = new HashMap<>();  // used to create segments
            List<Intersection> intersections = new ArrayList<>();
            Intersection.resetIdCounter();
            for (Node intersectionNode : intersectionNodes) {
                Element intersectionElement = (Element) intersectionNode;
                String id = intersectionElement.attributeValue("id");
                if (isValidId(id) && isValidCoordinate(intersectionElement.attributeValue("latitude")) && isValidCoordinate(intersectionElement.attributeValue("longitude"))) { // only added if the id is valid
                    double lat = Double.parseDouble(intersectionElement.attributeValue("latitude"));
                    double lon = Double.parseDouble(intersectionElement.attributeValue("longitude"));
                    map.updateBounds(lat, lon);
                    Intersection i = new Intersection(lat, lon);
                    if (!intersectionsById.containsKey(id)) {
                        // we don't add an intersection if it has an already existing ID
                        intersectionsById.put(id, i);
                        intersections.add(i);
                    }
                }
            }

            Set<String> intersectionIdsInGraph = new TreeSet<>(); // to remove disconnected intersections
            List<Node> segmentNodes = mapXmlDocument.selectNodes("/map/segment");
            List<Segment> segments = new ArrayList<>();
            for (Node segmentNode : segmentNodes) {
                Element segmentElement = (Element) segmentNode;
                String idOrigin = segmentElement.attributeValue("origin");
                String idDest = segmentElement.attributeValue("destination");
                double length = Double.parseDouble(segmentElement.attributeValue("length"));
                String name = segmentElement.attributeValue("name");

                if (intersectionsById.containsKey(idOrigin) && intersectionsById.containsKey(idDest)) {
                    intersectionIdsInGraph.add(idOrigin);
                    intersectionIdsInGraph.add(idDest);
                    Segment s = new Segment(name, length, intersectionsById.get(idOrigin), intersectionsById.get(idDest));
                    addSegmentIfNotRedundant(segments, s);
                }
            }

            // remove intersections that are in no segment
            List<String> intersectionsToBeRemoved = new ArrayList<>();
            for (Map.Entry<String, Intersection> entry : intersectionsById.entrySet()) {
                String intersectionId = entry.getKey();
                if (!intersectionIdsInGraph.contains(intersectionId)) {
                    // intersection in no segment
                    intersectionsToBeRemoved.add(intersectionId);
                }
            }
            for (String intersectionId : intersectionsToBeRemoved) {
                intersections.remove(intersectionsById.get(intersectionId));
                intersectionsById.remove(intersectionId);
            }

            map.setIntersectionsByOldID(intersectionsById);
            map.setIntersections(intersections);
            map.setSegments(segments);
        } catch (Exception e) {
            // parsing exception happens when an attribute is missing or invalid
            e.printStackTrace();
            throw new SyntaxException("Invalid XML file : invalid or missing attributes.");
        }

        if (map.getIntersections().size() == 0 || map.getSegments().size() == 0) {
            throw new SyntaxException("Invalid file syntax - couldn't use it to fill the map data.");
        }
    }

    /**
     * Generates the DOM document from the file at the provided path.
     * @throws DocumentException when the file was invalid
     */
    private void generateDocument() throws DocumentException {
        File xmlFile = new File(mapFilePath);
        SAXReader xmlReader = new SAXReader();
        mapXmlDocument = xmlReader.read(xmlFile);
    }


}
