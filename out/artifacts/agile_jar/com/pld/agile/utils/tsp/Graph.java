/*
 * Graph
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.utils.tsp;
import com.pld.agile.model.tour.Path;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	public abstract int getNbVertices();

	/**
	 * @param i 
	 * @param j 
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
	public abstract double getCost(int i, int j);

	public abstract Path getPath(int i, int j);

	/**
	 * @param i 
	 * @param j 
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
	public abstract boolean isArc(int i, int j);

	/**
	 * @param i
	 * @param j
	 * @param value
	 */
	public abstract void setCost(int i, int j, double value);


	public abstract void setPath(int i, int j, Path value);

	public abstract double getMinCost();


}
