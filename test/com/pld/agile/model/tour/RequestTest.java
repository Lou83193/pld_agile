/*
 * RequestTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    public void testEmptyRequests(){
        Request emptyRequest = new Request();
        assertNull(emptyRequest.getPickup());
        assertNull(emptyRequest.getDelivery());
    }

    @Test
    public void testRequests(){
        Request emptyRequest = new Request();
        Stop pickup = new Stop(emptyRequest,new Intersection(45,85),852,StopType.PICKUP);
        Stop delivery = new Stop(emptyRequest,new Intersection(963,98),46,StopType.DELIVERY);
        Request request = new Request(pickup,delivery);

        assertEquals(pickup,request.getPickup());
        assertEquals(delivery,request.getDelivery());

    }

}