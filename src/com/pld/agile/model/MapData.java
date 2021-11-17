package com.pld.agile.model;

import java.util.HashMap;

public class MapData {
    /**
     * Map of all intersections, key is the Intersection ID.
     */
    //todo: key is lat long
    HashMap <Integer, Intersection> IntersectionList = new HashMap();

    /**
     * Map of all segments, key is ...
     */
    //todo: list
    HashMap <Integer, Segment> SegmentList = new HashMap();
}
