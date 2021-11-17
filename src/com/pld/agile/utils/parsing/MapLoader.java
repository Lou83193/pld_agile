package com.pld.agile.utils.parsing;

import com.pld.agile.model.Intersection;
import com.pld.agile.model.MapData;
import com.pld.agile.model.Segment;
import javafx.util.Pair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    /**
     * Coordinates the entire parsing process - only method to call to fill the provided map.
     * @return boolean true if map has been successfully filled, false if the provided xml file was invalid
     */
    public boolean load() {
        try {
            generateDocument();
        } catch (DocumentException e) {
            // invalid XML file
            return false;
        }
        // DOM can be handled
        Element element = mapXmlDocument.getRootElement();
        List<Node> intersectionNodes = mapXmlDocument.selectNodes("/map/intersection");
        List<Node> segmentNodes = mapXmlDocument.selectNodes("/map/segment");

        HashMap<Integer, Intersection> intersectionsById = new HashMap<>();  // used to create segments
        HashMap<Pair<Double, Double>, Intersection> intersectionsByCoord = new HashMap<>(); // returned in the MapData map
        for (Node intersectionNode : intersectionNodes) {
            Element intersectionElement = (Element) intersectionNode;
            int id = Integer.parseInt(intersectionElement.attributeValue("id"));
            double lat = Double.parseDouble(intersectionElement.attributeValue("latitude"));
            double lon = Double.parseDouble(intersectionElement.attributeValue("longitude"));
            Intersection i = new Intersection(id, lat, lon);
            intersectionsById.put(id, i);
            intersectionsByCoord.put(new Pair<>(lat, lon), i);
        }

        List<Segment> segments = new ArrayList<>();
        for (Node segmentNode : segmentNodes) {
            Element segmentElement = (Element) segmentNode;
            int idOrigin = Integer.parseInt(segmentElement.attributeValue("origin"));
            int idDest = Integer.parseInt(segmentElement.attributeValue("destination"));
            double length = Double.parseDouble(segmentElement.attributeValue("length"));
            String name = segmentElement.attributeValue("name");

            Segment s = new Segment(length, name, intersectionsById.get(idOrigin), intersectionsById.get(idDest));
            segments.add(s);
        }

        


        return true;
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
