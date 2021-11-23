package com.pld.agile.utils.tsp;

public class CompleteGraph implements Graph {
	int nbVertices;
	double[][] cost;
	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param nbVertices
	 */
	public CompleteGraph(int nbVertices){
		this.nbVertices = nbVertices;
		cost = new double[nbVertices][nbVertices];
		for (int i=0; i<nbVertices; i++){
		    for (int j=0; j<nbVertices; j++){
				cost[i][j] = Double.MAX_VALUE;
		    }
		}
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public double getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return -1;
		return cost[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

	@Override
	public void setCost(int i, int j, double value) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return;
		cost[i][j] = value;
	}

}
