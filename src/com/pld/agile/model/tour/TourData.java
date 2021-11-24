/*
 * TourData
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.tsp.CompleteGraph;
import com.pld.agile.utils.tsp.Graph;
import com.pld.agile.utils.tsp.TSP;
import com.pld.agile.utils.tsp.TSP1;
import javafx.util.Pair;

import java.util.*;

/**
 * Stores the data of a loaded requests list.
 */
public class TourData extends Observable {
    /**
     * List of requests composing the tour.
     */
    private List<Request> requestList;
    /**
     * The map on which the tour takes place.
     */
    private MapData associatedMap;
    /**
     * The warehouse (start & end) Stop.
     */
    private Stop warehouse;
    /**
     * The departure time from the warehouse.
     */
    private String departureTime;

    private Graph stopsGraph;

    private int[][] predecessors;
    // First index is algorithm index
    // Second index is app index

    private List<Integer> stops;

    private List<Integer> computedPath;
    // Index: Nth stop visited
    // Value : Algorithm index

    /**
     * TourData constructor.
     */
    public TourData() {
        super();
        requestList = new ArrayList<>();
        associatedMap = null;
        departureTime = "";
        warehouse = null;
    }

    /**
     * Getter for attribute requestList.
     * @return requestList
     */
    public List<Request> getRequestList() {
        return requestList;
    }

    /**
     * Setter for attribute requestList.
     * @param requestList List of requests composing the tour
     */
    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
        notifyObservers(UpdateType.MAP);
        notifyObservers(UpdateType.REQUESTS);
    }

    /**
     * Getter for attribute associatedMap
     * @return associatedMap
     */
    public MapData getAssociatedMap() {
        return associatedMap;
    }

    /**
     * Setter for attribute associatedMap
     * @param associatedMap the map on which the tour takes place
     */
    public void setAssociatedMap(MapData associatedMap) {
        this.associatedMap = associatedMap;
    }

    /**
     * Getter for attribute warehouse
     * @return warehouse
     */
    public Stop getWarehouse() {
        return warehouse;
    }

    /**
     * Setter for attribute warehouse
     * @param warehouse the warehouse (start & end) Stop
     */
    public void setWarehouse(Stop warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * Getter for attribute departureTime
     * @return departureTime
     */
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * Setter for attribute departureTime
     * @param departureTime the departure time from the warehouse
     */
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int[][] getPredecessors() {
        return predecessors;
    }

    public List<Integer> getStops() {
        return stops;
    }

    public List<Integer> getComputedPath() {
        return computedPath;
    }

    private void setStops(){
        stops = new ArrayList<Integer>();
        stops.add(warehouse.getAddress().getId());
        for (Request request : requestList) {
            stops.add(request.getPickup().getAddress().getId());//add pickup (odd index)
            stops.add(request.getDelivery().getAddress().getId());//add delivery (even index)
        }
        System.out.println("stops=" + stops);
    }

    public void computeTour(){
        setStops();
        dijkstra();
        tsp();
    }

    private void dijkstra() {
        int nbIntersections = associatedMap.getIntersections().size();
        predecessors = new int[stops.size()][nbIntersections];
        stopsGraph = new CompleteGraph(stops.size());

        int stopIndex = 0; // need index of currStop in the list stops to fill predecessors

        System.out.println("Dijkstra START");
        for(int currStop : stops) {
            System.out.println("On Stop : " + stops.get(stopIndex));
            // Current Stop Variables
            double [] dist = new double[nbIntersections]; //index = intersection id in map data
            int [] pi = new int[nbIntersections]; //index = intersection id in map data
            Set<Integer> settled = new HashSet<Integer>();

            PriorityQueue<Integer> pq = new PriorityQueue<>(
                    new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            if(dist[o1] < dist[o2]) {
                                return -1;
                            }
                            if(dist[o1] > dist[o2]) {
                                return 1;
                            }
                            return 0;
                        }
                    }
            );

            // Dist initialization
            for (int i = 0; i < nbIntersections; i++) {
                dist[i] = Double.MAX_VALUE;
            }
            dist[currStop] = 0; // distance to current stop is 0

            pi[currStop] = -1; //null, starting stop won't have predecessors
            pq.add(currStop);

            int nbStopCalculated = 0;

            while (nbStopCalculated != stops.size()) {

                    if(pq.isEmpty())
                        break;

                    int node = pq.remove();

                    for (Segment road : associatedMap.getIntersections().get(node).getOriginOf()) {
                        int nextNode = road.getDestination().getId();

                        if (!settled.contains(nextNode)) {

                            double distance = dist[nextNode];

                            if (distance > dist[node] + road.getLength()) {
                                distance = dist[node] + road.getLength();
                                dist[nextNode] = distance;
                                pi[nextNode] = node;
                            }

                            if (!pq.contains(nextNode)) {
                                pq.add(nextNode);
                            } else {
                                pq.remove(nextNode);
                                pq.add(nextNode);
                            }
                        }
                    }

                    settled.add(node);

                    if(stops.contains(node)) {
                        nbStopCalculated ++;
                    }


            }

            // Save computed data
            for (int i = 0; i < nbIntersections; i++) {
                predecessors[stopIndex][i] = pi[i];
            }
            for (int i = 0; i < stops.size(); i++) {
                stopsGraph.setCost(stopIndex, i, dist[stops.get(i)]);
            }
            stopIndex++;

        }
        //TESTS :
        System.out.println("stops graph : ");
        for (int i = 0; i< stops.size(); i++) {
            for (int j = 0; j< stops.size(); j++) {
                System.out.print(stopsGraph.getCost(i,j) +" ");
            }
            System.out.println();
        }
        System.out.println("END Dijkstra");
    } // ---- END of dijkstra

    private void tsp() {
        System.out.println("TSP INIT...");
        TSP tsp = new TSP1(); // No Heuristic
        long startTime = System.currentTimeMillis();
        System.out.println("TSP START");
        tsp.searchSolution(20000, stopsGraph);
        System.out.println("Solution of cost " + tsp.getSolutionCost() + " found in " + (System.currentTimeMillis() - startTime) + "ms");
        computedPath = new ArrayList<>();
        for(int i = 0; i < stopsGraph.getNbVertices(); i++) {
            computedPath.add(tsp.getSolution(i));
        }
        System.out.println(computedPath);
        notifyObservers(UpdateType.TOUR);
    } // ---- END of TSP

    // Branch&Bound (notes for myself)
    /* H0 = 0
    /* H2 = (nbUnvisited+1)*dMin
    /* H3 = l + sum of li
    /* H4 = Sort unvisited by shortest cost to last visited vertex
    */

    // Limited Discrepancy Search -> recall 3IF

    /**
     * Generates a String which describes the object
     * @return type String
     */
    @Override
    public String toString() {
        return "TourData{" +
                "requestList=" + requestList +
                ", associatedMap=" + associatedMap +
                ", warehouse=" + warehouse +
                ", departureTime='" + departureTime + '\'' +
                '}';
    }
}
