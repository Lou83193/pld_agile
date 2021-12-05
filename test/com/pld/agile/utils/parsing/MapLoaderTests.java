package com.pld.agile.utils.parsing;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.exception.SyntaxException;
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
    public void test5Intersections4Segments() {
        String filePath = "test/resources/loadMap_5Inter4Seg.xml";
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
                Map.ofEntries(Map.entry("name", "Avenue Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 1)),
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 4))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
    }


    //@Disabled("Disabled until removal of disconnected intersections from mapData on parsing is implemented")
    @Test
    public void test4Intersection4Segments() {
        String filePath = "test/resources/loadMap_4Inter4Seg.xml";
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
                Map.ofEntries(Map.entry("name", "Boulevard Général Frère"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 2)),
                Map.ofEntries(Map.entry("name", "Rue de la Meuse"), Map.entry("length", 100), Map.entry("originId", 0), Map.entry("destinationId", 3)),
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 2), Map.entry("destinationId", 1))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(4, actualMapData.getIntersections().size());
    }


    @Test
    public void testNoMapNode() {
        String filePath = "test/resources/loadMap_noMapNode.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);

        assertThrows(SyntaxException.class, mapLoader::load);
    }


    @Test
    public void testIOException() {
        String filePath = "test/resources/FILE_DOES_NOT_EXIST.xml";
        MapData actualMapData = new MapData();
        MapLoader mapLoader = new MapLoader(filePath, actualMapData);

        assertThrows(IOException.class, mapLoader::load);
    }


    @Test
    public void testSameIntersectionSegment() {
        String filePath = "test/resources/loadMap_sameIntersectionsSegment.xml";
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
                Map.ofEntries(Map.entry("name", "Rue de la Moselle"), Map.entry("length", 100), Map.entry("originId", 2), Map.entry("destinationId", 1))
        );
        MapData expectedMapData = createMapData(intersectionsData, segmentsData);

        assertEquals(expectedMapData.toString(), actualMapData.toString());
        assertEquals(3, actualMapData.getSegments().size());
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


    @Disabled("Disabled until removal of segment with nonexistent origin or destination is implemented")
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


    @Disabled("Disabled until removal of intersection with no id is implemented")
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


    @Disabled("Disabled until removal of second intersection with same id is implemented")
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


    @Disabled("Disabled until removal of intersection with no latitude is implemented")
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


    @Disabled("Disabled until removal of intersection with no longitude is implemented")
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


    @Disabled("Disabled until removal of intersection with invalid id is implemented")
    @Test
    public void testInvalidId() {
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


    @Disabled("Disabled until removal of intersection with invalid coordinates is implemented")
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
            intersections.add(new Intersection((int) item.get("id"), (double) item.get("latitude"), (double) item.get("longitude")));
        }
        List<Segment> segments = new ArrayList<>();
        for (Map<String, Object> item : segmentsData) {
            segments.add(new Segment((String) item.get("name"), (int) item.get("length"), intersections.get((int) item.get("originId")), intersections.get((int) item.get("destinationId"))));
        }

        return new MapData(intersections, segments);
    }
}
