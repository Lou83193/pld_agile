/*
 * TSP3
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import com.pld.agile.model.tour.TourData;

import java.util.Collection;
import java.util.Iterator;

public class TSP3 extends TemplateTSP {

    public TSP3(TourData tourData){
        addObserver(tourData);
    }

    @Override
    protected double bound(Integer currentVertex, Collection<Integer> unvisited) {
        double l = Double.MAX_VALUE;
        double sumLi = 0;
        // L
        for(Integer s : unvisited) {
            if(l > g.getCost(currentVertex, s)) l = g.getCost(currentVertex, s);
        }
        // Sum of Li
        for(Integer u : unvisited) {
            double li = g.getCost(u, 0);
            for(Integer v : unvisited) {
                if(u != v && (li > g.getCost(u, v))) li = g.getCost(u, v);
            }
            sumLi += li;
        }
        return l + sumLi;
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

}
