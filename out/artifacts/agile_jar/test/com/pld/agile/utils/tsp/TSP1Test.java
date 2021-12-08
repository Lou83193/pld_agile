/*
 * TSP1Test
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import com.pld.agile.model.tour.TourData;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TSP1Test {

    @Test
    public void testBound() {
        TSP1 tsp = new TSP1(new TourData());
        assertEquals(0,tsp.bound(1, Arrays.asList(2,3,1)));
    }

}