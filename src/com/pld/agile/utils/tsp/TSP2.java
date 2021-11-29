/*
 * TSP2
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import java.util.Collection;
import java.util.Iterator;

public class TSP2 extends TemplateTSP {
    @Override
    protected double bound(Integer currentVertex, Collection<Integer> unvisited) {
        return (unvisited.size()+1)*g.getMinCost();
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

}