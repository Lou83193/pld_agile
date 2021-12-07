/*
 * IntersectionTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.map;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IntersectionTest {

    @Test
    public void testIntersection(){
        double lat = 46;
        double lon = 42;
        List<Segment> originOf = new ArrayList<>();

        Intersection intersection = new Intersection(lat,lon);
        assertEquals(lat,intersection.getLatitude());
        assertEquals(lon,intersection.getLongitude());
        assertEquals(originOf,intersection.getOriginOf());
    }
    @Test
    public void testFindSegmentToSuccess() {
        Intersection inter1 = new Intersection(42,42);
        Intersection inter2 = new Intersection(46,79);
        Intersection inter3 = new Intersection(89,39);
        Intersection inter4 = new Intersection(89,52);

        List<Segment> segments = new ArrayList<>();
        Segment seg1To2 = new Segment("seg1",40,inter1,inter2);
        segments.add(seg1To2);
        Segment seg1To3 = new Segment("seg2",65,inter1,inter3);
        segments.add(seg1To3);
        Segment seg1To4 = new Segment("seg3",89,inter1,inter4);
        segments.add(seg1To4);
        inter1.setOriginOf(segments);

        assertEquals(seg1To3.toString(),inter1.findSegmentTo(inter3).toString());

    }

    @Test
    public void testFindSegmentToFail() {
        Intersection inter1 = new Intersection(42,42);
        Intersection inter2 = new Intersection(46,79);
        Intersection inter3 = new Intersection(89,39);
        Intersection inter4 = new Intersection(89,52);

        List<Segment> segments = new ArrayList<>();
        Segment seg1To2 = new Segment("seg1",40,inter1,inter2);
        segments.add(seg1To2);
        Segment seg1To3 = new Segment("seg2",65,inter1,inter3);
        segments.add(seg1To3);
        Segment seg1To4 = new Segment("seg3",89,inter1,inter4);
        segments.add(seg1To4);
        inter1.setOriginOf(segments);

        assertNull(inter2.findSegmentTo(inter3));

    }
}