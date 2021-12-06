package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.exception.SyntaxException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTests {
    private MapData mapData;
    private MapLoader mapLoader;


    @Test
    public void testMapLoader() {
        MapLoader mapLoader = new MapLoader("test/resources/loadMap_5Inter4Seg.xml", new MapData());
        assertNotNull(mapLoader.getMap());
        assertNotNull(mapLoader.getMapFilePath());
    }

    public void loadMap (String filePath){
        mapData = new MapData();
        mapLoader = new MapLoader(filePath, mapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5Intersections4Segments() {
        loadMap("test/resources/loadMap_5Inter4Seg.xml");
        assertTrue(mapData.getIntersections().size()==5);
        assertTrue(mapData.getSegments().size()==4);
    }


    @Test
    public void test4Intersection4Segments() {
        loadMap("test/resources/loadMap_4Inter4Seg.xml");
        assertFalse( mapData.getIntersections().size()==5);
        assertTrue(mapData.getSegments().size()==4);
    }


    @Test
    public void testNoMapNode() {
        loadMap("test/resources/loadMap_noMapNode.xml");
        assertThrows(SyntaxException.class, mapLoader::load);
    }


    @Test
    public void testIOException() {
        loadMap("test/resources/FILE_DOES_NOT_EXIST.xml");
        assertThrows(IOException.class, mapLoader::load);
    }


    @Test
    public void testSameIntersectionSegment() {
        loadMap("test/resources/loadMap_sameIntersectionsSegment.xml");

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.100), Map.entry("longitude", 4.001)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.101), Map.entry("longitude", 4.002)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.102), Map.entry("longitude", 4.003)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.103), Map.entry("longitude", 4.004)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 45.104), Map.entry("longitude", 4.005))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 2), Map.entry("destinationId", 1))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), mapData.toString());
        assertEquals(3, mapData.getSegments().size());
    }


    @Test
    public void testSegmentLengthZero() {
        String filePath = "test/resources/loadMap_segmentLength0.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.100), Map.entry("longitude", 4.001)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.101), Map.entry("longitude", 4.002)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.102), Map.entry("longitude", 4.003)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.103), Map.entry("longitude", 4.004)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 45.104), Map.entry("longitude", 4.005))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 3), Map.entry("destinationId", 4))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(3, actualMapData.getSegments().size());
    }


    @Test
    public void testNonexistentOrigin() {
        String filePath = "test/resources/loadMap_noOriginSegment.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.0), Map.entry("longitude", 4.00128)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.0009), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 4))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(2, actualMapData.getSegments().size());
    }


    @Test
    public void testIntersectionNoId() {
        String filePath = "test/resources/loadMap_noId.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.0), Map.entry("longitude", 4.00128)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 1), Map.entry("destinationId", 2))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(4, actualMapData.getIntersections().size());
        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    @Test
    public void testIntersectionsSameId() {
        String filePath = "test/resources/loadMap_sameId.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 1), Map.entry("latitude", 45.0), Map.entry("longitude", 4.00128)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(4, actualMapData.getIntersections().size());
        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    @Test
    public void testNoLatitude() {
        String filePath = "test/resources/loadMap_noLat.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.0009), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 1), Map.entry("destinationId", 0)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(4, actualMapData.getIntersections().size());
        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    @Test
    public void testNoLongitude() {
        String filePath = "test/resources/loadMap_noLong.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.0009), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 1), Map.entry("destinationId", 0)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(4, actualMapData.getIntersections().size());
        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    @Test
    public void testInvalidId() {
        String filePath = "test/resources/loadMap_invalidId.xml";
        MapData mapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, mapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*List<Intersection> intersections = new ArrayList<>();
        Intersection inter1 = new Intersection(45.0, 4.0);
        intersections.add(inter1);
        Intersection inter2 = new Intersection(45.0009, 4.0);
        intersections.add(inter2);
        Intersection inter3 = new Intersection(45.0, 3.99872);
        intersections.add(inter3);
        Intersection inter4 = new Intersection(44.9991, 4.0);
        intersections.add(inter4);
        System.out.println(inter1.getId());

        List<Segment> segments = Arrays.asList(
                new Segment("Avenue Général Frère", 100, inter1, inter2),
                new Segment("Rue de la Meuse", 100, inter1, inter3),
                new Segment("Rue de la Moselle", 100, inter1, inter4)
        );

        MapData expectedMapData = new MapData(intersections,segments);*/
        assertFalse(mapData.getIntersections().size()==5);
        assertFalse(mapData.getSegments().size()==4);
    }


    @Test
    public void testInvalidCoords() {
        String filePath = "test/resources/loadMap_invalidId.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);
        try {
            mapLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> intersectionsData = Arrays.asList(
                Map.ofEntries(Map.entry("id", 0), Map.entry("latitude", 45.0), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 2), Map.entry("latitude", 45.0009), Map.entry("longitude", 4.0)),
                Map.ofEntries(Map.entry("id", 3), Map.entry("latitude", 45.0), Map.entry("longitude", 3.99872)),
                Map.ofEntries(Map.entry("id", 4), Map.entry("latitude", 44.9991), Map.entry("longitude", 4.0))
        );

        List<Map<String, Object>> segmentsData = Arrays.asList(
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 1), Map.entry("destinationId", 0)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(4, actualMapData.getIntersections().size());
        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    private MapData createMapData(List<Map<String, Object>> intersectionsData, List<Map<String, Object>> segmentsData) {
        List<Intersection> intersections = new ArrayList<>();
        for (Map<String, Object> item : intersectionsData) {
            intersections.add(new Intersection((double) item.get("latitude"), (double) item.get("longitude")));
        }
        List<Segment> segments = new ArrayList<>();
        for (Map<String, Object> item : segmentsData) {
            segments.add(new Segment((String) item.get("name"), (int) item.get("length"), intersections.get((int) item.get("originId")), intersections.get((int) item.get("destinationId"))));
        }

        return new MapData(intersections, segments);
    }
}
