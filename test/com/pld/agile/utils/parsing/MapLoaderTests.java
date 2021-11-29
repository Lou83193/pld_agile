package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTests {

    private MapData createMapData(List<Map<String, Object>> intersectionsData, List<Map<String, Object>> segmentsData) {
        List<Intersection> intersections = new ArrayList<>();
        for (Map<String, Object> item : intersectionsData) {
            intersections.add(new Intersection((int) item.get("id"), (double) item.get("latitude"), (double) item.get("longitude")));
        }
        List<Segment> segments = new ArrayList<>();
        for (Map<String, Object> item : segmentsData) {
            segments.add(new Segment((String) item.get("name"), (int) item.get("length"), intersections.get((int) item.get("originId")), intersections.get((int) item.get("destinationId"))));
        }

        return new MapData(intersections, segments);
    }

    @Test
    // Test n°1.1
    public void test5Intersections4Segments() {
        String filePath = "test/resources/loadMap_5Inter4Seg.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        boolean res = mapLoader.load();
        assertTrue(res);

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.0), Map.entry("longitude", 4.00128)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.0009), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 4))
        );

        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        String expected = expectedMapData.toString();
        assertEquals(expected, actualMapData.toString());
    }


    @Test
    //Test n°1.4
    public void testNoMapNode() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //redirect the System-output (normally the console) to a variable
        System.setErr(new PrintStream(outContent));

        String filePath = "test/resources/loadMap_noMapNode.xml";
        MapData mapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, mapData);
        boolean res = mapLoader.load();
        assertFalse(res);
        //check if the beginning of the error message is the one expected
        assertEquals("org.dom4j.DocumentException: Error on line 3 of document", outContent.toString().substring(0, 56));
    }

}
