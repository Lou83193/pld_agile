/*
 * StopTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import org.junit.jupiter.api.Test;

import java.util.DuplicateFormatFlagsException;

import static org.junit.jupiter.api.Assertions.*;

class StopTest {

    @Test
    public void testStop (){
        Request request = new Request();
        Intersection address = new Intersection(52,98);
        long duration = 845;

        Stop stop = new Stop(request,address,duration,StopType.PICKUP);

        assertNull(stop.getRequest().getDelivery());
        assertEquals(address,stop.getAddress());
        assertEquals(duration,stop.getDuration());
        assertEquals(StopType.PICKUP,stop.getType());
    }

}