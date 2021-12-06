/*
 * SegmentTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTest {

    @Test
    public void testSegment(){
        String name = "Rue Jolie";
        double length = 42;
        Intersection origin = new Intersection(852,963);
        Intersection destination = new Intersection(52,93);

        Segment segment = new Segment(name,length,origin,destination);

        assertEquals(name,segment.getName());
        assertEquals(length,segment.getLength());
        assertEquals(origin.toString(),segment.getOrigin().toString());
        assertEquals(destination.toString(),segment.getDestination().toString());
    }

}