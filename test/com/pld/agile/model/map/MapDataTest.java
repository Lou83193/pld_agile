/*
 * MapDataTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.map;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapDataTest {

    @Test
    public void testMapData(){
        List<Intersection> intersections = new ArrayList<>();
        Intersection inter1 = new Intersection(42,42);
        intersections.add(inter1);
        Intersection inter2 = new Intersection(46,79);
        intersections.add(inter2);
        Intersection inter3 = new Intersection(89,39);
        intersections.add(inter3);

        List<Segment> segments = new ArrayList<>();
        Segment seg1To2 = new Segment("seg1",40,inter1,inter2);
        segments.add(seg1To2);
        Segment seg1To3 = new Segment("seg2",65,inter1,inter3);
        segments.add(seg1To3);
        Segment seg2To3 = new Segment("seg3",89,inter2,inter3);
        segments.add(seg2To3);

        MapData mapData = new MapData(intersections,segments);

        assertEquals(3,intersections.size());
        assertEquals(inter2,intersections.get(1));
        assertEquals(3,segments.size());
        assertEquals(seg1To3,segments.get(1));

    }
    @Test
    public void testUpdateBounds() {
        MapData mapData = new MapData();
        double lat = 42;
        double lon = 89;
        mapData.updateBounds(lat,lon);
        assertEquals(lon,mapData.getMaxLon());
        assertEquals(lon,mapData.getMinLon());
        assertEquals(lat,mapData.getMinLat());
        assertEquals(lat,mapData.getMaxLat());
    }

    @Test
    public void testResetBounds() {
        MapData mapData = new MapData();
        mapData.updateBounds(42,96);
        mapData.resetBounds();
        assertEquals(Integer.MIN_VALUE,mapData.getMaxLon());
        assertEquals(Integer.MAX_VALUE,mapData.getMinLon());
        assertEquals(Integer.MAX_VALUE,mapData.getMinLat());
        assertEquals(Integer.MIN_VALUE,mapData.getMaxLat());

    }

    @Test
    public void testFindClosestIntersection (){
        List<Intersection> intersections = new ArrayList<>();
        Intersection inter1 = new Intersection(42,42);
        intersections.add(inter1);
        Intersection inter2 = new Intersection(42,78);
        intersections.add(inter2);
        Intersection inter3 = new Intersection(89,39);
        intersections.add(inter3);

        List<Segment> segments = new ArrayList<>();
        Segment seg1To2 = new Segment("seg1",40,inter1,inter2);
        segments.add(seg1To2);
        Segment seg1To3 = new Segment("seg2",65,inter1,inter3);
        segments.add(seg1To3);
        Segment seg2To3 = new Segment("seg3",89,inter2,inter3);
        segments.add(seg2To3);

        MapData mapData = new MapData(intersections,segments);

        assertEquals(inter1.toString(),mapData.findClosestIntersection(new double[] {50,58}).toString());
    }
}