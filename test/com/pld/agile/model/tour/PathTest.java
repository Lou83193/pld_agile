/*
 * PathTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    public void testPath(){
        Request request = new Request();
        Stop origin = new Stop(request,new Intersection(45,85),852,StopType.PICKUP);
        Stop destination = new Stop(request,new Intersection(963,98),46,StopType.DELIVERY);
        Path path = new Path(origin,destination);

        assertEquals(origin,path.getOrigin());
        assertEquals(destination,path.getDestination());
    }

}