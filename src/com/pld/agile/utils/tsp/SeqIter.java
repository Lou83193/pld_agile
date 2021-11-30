/*
 * SeqIter
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Integer> {
	private Integer[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	public SeqIter (Collection<Integer> unvisited, int currentVertex, Graph g) {
		nbCandidates = 0;
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited) {
			if(canBeVisited(unvisited, s))
				candidates[nbCandidates++] = s;
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Integer next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

	protected boolean canBeVisited(Collection<Integer> unvisited, Integer s) {
		if(s%2 == 1) { // s is a pickup
			return true;
		} else {
			if(unvisited.contains(s-1)) { //s corresponding pickup stop isn't already visited
				return false;
			}
			return true;
		}
	}

}
