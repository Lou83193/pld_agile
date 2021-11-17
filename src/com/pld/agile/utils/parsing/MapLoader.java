package com.pld.agile.utils.parsing;

import com.pld.agile.model.Intersection;
import com.pld.agile.model.MapData;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
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
            // fichier XML invalide
            return false;
        }
        // le DOM est manipulable
        Element element = mapXmlDocument.getRootElement();
        List<Node> intersectionNodes = mapXmlDocument.selectNodes("/map/intersection");
        List<Node> segmentNodes = mapXmlDocument.selectNodes("/map/segment");

        HashMap<Integer, Intersection> intersections = new HashMap<>();
        for (Node intersectionNode : intersectionNodes) {
            Element intersectionElement = (Element) intersectionNode;
            int id = Integer.parseInt(intersectionElement.attributeValue("id"));
            float lat = Float.parseFloat(intersectionElement.attributeValue("latitude"));
            float lon = Float.parseFloat(intersectionElement.attributeValue("longitude"));
            intersections.put(id, new Intersection(id, lat, lon));
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
