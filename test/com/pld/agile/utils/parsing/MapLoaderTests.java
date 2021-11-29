package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTests {

    @Test
    // Test nb 1.1
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

        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    @Disabled("Disabled until removal of disconnected intersections from model on parsing is implemented")
    @Test
    // Test nb 1.2
    public void test4Intersection4Segments() {
        String filePath = "test/resources/loadMap_4Inter4Seg.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        boolean res = mapLoader.load();
        assertTrue(res);

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.100), Map.entry("longitude", 4.001)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.101), Map.entry("longitude", 4.002)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.102), Map.entry("longitude", 4.003)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.103), Map.entry("longitude", 4.004)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 45.104), Map.entry("longitude", 4.005))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 2), Map.entry("destinationId", 1))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(4, actualMapData.getIntersections().size());
    }


    @Disabled("Disabled until exception throw when no map tag is present is implemented")
    @Test
    // Test nb 1.3
    public void testNoMapNode() {
        String filePath = "test/resources/loadMap_noMapNode.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);

//        assertThrows(MapSyntaxException.class, mapLoader::load); // TODO: create MapSyntaxException
    }


    @Disabled("Disabled until IO exception throw when file inaccessible is implemented")
    @Test
    // Test nb 1.4
    public void testIOException() {
        String filePath = "test/resources/FILE_DOES_NOT_EXIST.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);

        assertThrows(IOException.class, mapLoader::load);
    }


    @Disabled("Disabled until removal of segment with same origin and destination is implemented")
    @Test
    // Test nb 1.5
    public void testSameIntersectionSegment() {
        String filePath = "test/resources/loadMap_sameIntersectionsSegment.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        boolean res = mapLoader.load();
        assertTrue(res);

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.100), Map.entry("longitude", 4.001)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.101), Map.entry("longitude", 4.002)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.102), Map.entry("longitude", 4.003)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.103), Map.entry("longitude", 4.004)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 45.104), Map.entry("longitude", 4.005))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 2), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 2), Map.entry("destinationId", 1))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(3, actualMapData.getSegments().size());
    }


    @Disabled("Disabled until removal of segment with length zero is implemented")
    @Test
    // Test nb 1.6
    public void testSegmentLengthZero() {
        String filePath = "test/resources/loadMap_segmentLength0.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        boolean res = mapLoader.load();
        assertTrue(res);

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.100), Map.entry("longitude", 4.001)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.101), Map.entry("longitude", 4.002)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.102), Map.entry("longitude", 4.003)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.103), Map.entry("longitude", 4.004)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 45.104), Map.entry("longitude", 4.005))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 0), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 3), Map.entry("destinationId", 4))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(3, actualMapData.getSegments().size());
    }


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
}
