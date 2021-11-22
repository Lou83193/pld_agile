package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
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
            e.printStackTrace();
            return false;
        }
        // DOM can be handled
        List<Node> intersectionNodes = mapXmlDocument.selectNodes("/map/intersection");

        HashMap<Long, Intersection> intersectionsById = new HashMap<>();  // used to create segments
        for (Node intersectionNode : intersectionNodes) {
            Element intersectionElement = (Element) intersectionNode;
            long id = Long.parseLong(intersectionElement.attributeValue("id"));
            double lat = Double.parseDouble(intersectionElement.attributeValue("latitude"));
            double lon = Double.parseDouble(intersectionElement.attributeValue("longitude"));
            map.updateBounds(lat, lon);
            Intersection i = new Intersection(id, lat, lon);
            intersectionsById.put(id, i);
        }

        List<Node> segmentNodes = mapXmlDocument.selectNodes("/map/segment");
        List<Segment> segments = new ArrayList<>();
        for (Node segmentNode : segmentNodes) {
            Element segmentElement = (Element) segmentNode;
            long idOrigin = Long.parseLong(segmentElement.attributeValue("origin"));
            long idDest = Long.parseLong(segmentElement.attributeValue("destination"));
            double length = Double.parseDouble(segmentElement.attributeValue("length"));
            String name = segmentElement.attributeValue("name");

            Segment s = new Segment(name, length, intersectionsById.get(idOrigin), intersectionsById.get(idDest));
            segments.add(s);
            intersectionsById.get(idOrigin).getOriginOf().add(s);
        }

        map.setIntersections(intersectionsById);
        map.setSegments(segments);

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
