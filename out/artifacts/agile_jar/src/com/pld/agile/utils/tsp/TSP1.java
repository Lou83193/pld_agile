/*
 * TSP1
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import com.pld.agile.model.tour.TourData;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

	public TSP1(TourData tourData){
		addObserver(tourData);
	}

	@Override
	protected double bound(Integer currentVertex, Collection<Integer> unvisited) {
		return 0;
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
