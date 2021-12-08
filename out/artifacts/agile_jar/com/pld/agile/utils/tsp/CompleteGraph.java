/*
 * CompleteGraph
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;

import com.pld.agile.model.tour.Path;

public class CompleteGraph implements Graph {
	int nbVertices;

	Path [][] paths;
	double [][] costs;
	double minCost;

	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param nbVertices
	 */
	public CompleteGraph(int nbVertices) {
		this.nbVertices = nbVertices;
		paths = new Path[nbVertices][nbVertices];
		costs = new double[nbVertices][nbVertices];
		for (int i=0; i<nbVertices; i++){
		    for (int j=0; j<nbVertices; j++) {
				costs[i][j]=Double.MAX_VALUE ;
		    }
		}
		minCost = Double.MAX_VALUE;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public Path getPath(int i, int j) {
		if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices) {
			return null;
		}
		return paths[i][j];
	}

	@Override
	public double getCost(int i, int j) {
		if (i<0 || i >= nbVertices || j < 0 || j >= nbVertices) {
			return -1;
		}
		return costs[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices) {
			return false;
		}
		return i != j;
	}

	@Override
	public void setPath(int i, int j, Path value) {
		if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices) {
			return;
		}
		paths[i][j]=value;
	}

	@Override
	public void setCost(int i, int j, double value) {
		if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices) {
			return;
		}
		costs[i][j] = value;
		if(i!=j && minCost > value)
		{
			minCost = value;
		}
	}

	@Override
	public double getMinCost() {return minCost;}

}
