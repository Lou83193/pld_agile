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

        Intersection.resetIdCounter();
        List<Intersection> intersectionsData = new ArrayList<>();
        Intersection inter1 = new Intersection(45.0, 4.0);
        intersectionsData.add(inter1);
        Intersection inter2 = new Intersection( 45.0, 4.00128);
        intersectionsData.add(inter2);
        Intersection inter3 = new Intersection( 45.0009, 4.0);
        intersectionsData.add(inter3);
        Intersection inter4 = new Intersection( 45.0, 3.99872);
        intersectionsData.add(inter4);
        Intersection inter5 = new Intersection(44.9991, 4.0);
        intersectionsData.add(inter5);

        List<Segment> segmentsData = Arrays.asList(
                new Segment("Avenue Général Frère", 100, inter1, inter2),
                new Segment("Boulevard Général Frère",100,inter1,inter3),
                new Segment("Rue de la Meuse", 100, inter1, inter4),
                new Segment("Rue de la Moselle", 100, inter1, inter5)
        );
        MapData expectedMapData = new MapData(intersectionsData, segmentsData);
        assertEquals(expectedMapData.toString(),mapData.toString());
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
        assertFalse(mapData.getSegments().size()==4);
    }


    @Test
    public void testSegmentLengthZero() {
        loadMap("test/resources/loadMap_segmentLength0.xml");
        assertFalse(mapData.getSegments().size()==4);
    }


    @Test
    public void testNonexistentOrigin() {
        loadMap("test/resources/loadMap_noOriginSegment.xml");
        assertFalse(mapData.getSegments().size()==4);
    }


    @Test
    public void testIntersectionNoId() {
        loadMap("test/resources/loadMap_noId.xml");
        assertFalse(mapData.getIntersections().size()==5);
    }


    @Test
    public void testIntersectionsSameId() {
        loadMap("test/resources/loadMap_sameId.xml");
        assertFalse(mapData.getIntersections().size()==5);
        assertFalse(mapData.getIntersections().contains(new Intersection(45.0009,4.0)));
    }


    @Test
    public void testNoLatitude() {
        loadMap("test/resources/loadMap_noLat.xml");
        assertFalse(mapData.getIntersections().size()==5);
    }


    @Test
    public void testNoLongitude() {
        loadMap("test/resources/loadMap_noLong.xml");
        assertFalse(mapData.getIntersections().size()==5);
    }


    @Test
    public void testInvalidId() {
        loadMap("test/resources/loadMap_invalidId.xml");
        assertFalse(mapData.getIntersections().size()==5);
        assertFalse(mapData.getSegments().size()==4);
    }


    @Test
    public void testInvalidCoords() {
        loadMap("test/resources/loadMap_invalidId.xml");
        assertFalse(mapData.getIntersections().size()==5);
    }

}
