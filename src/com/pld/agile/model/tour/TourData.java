/*
 * TourData
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.tsp.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Pair;

import java.time.LocalTime;
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
     * List of all stops
     */
    private HashMap<Integer, Stop> stopMap;
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
    private LocalTime departureTime;

    private Graph stopsGraph;

    private List<Integer> stops;

    private List<Path> tourPaths;


    /**
     * TourData constructor.
     */
    public TourData() {
        super();
        requestList = new ArrayList<>();
        associatedMap = null;
        departureTime = null;
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
     * Getter for attribute stopMap.
     * @return stopMap
     */
    public HashMap<Integer, Stop> getStopMap() {
        return stopMap;
    }

    /**
     * Setter for attribute stopMap.
     * @param stopMap Map of stops composing the tour (key = their intersection id)
     */
    public void setStopMap(HashMap<Integer, Stop> stopMap) {
        this.stopMap = stopMap;
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
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Setter for attribute departureTime
     * @param departureTime the departure time from the warehouse
     */
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Unhighlights every stop.
     */
    public void unHighlightStops() {
        warehouse.setHighlighted(false);
        for (Request request : requestList) {
            request.getPickup().setHighlighted(false);
            request.getDelivery().setHighlighted(false);
        }
    }


    public List<Integer> getStops() {
        return stops;
    }

    public Graph getStopsGraph() {
        return stopsGraph;
    }

    public List<Path> getTourPaths() { return tourPaths; }

    public void setStops() {
        stops = new ArrayList<Integer>();
        stops.add(warehouse.getAddress().getId());
        for (Request request : requestList) {
            stops.add(request.getPickup().getAddress().getId());//add pickup (odd index)
            stops.add(request.getDelivery().getAddress().getId());//add delivery (even index)
        }
        System.out.println("stops=" + stops);
    }

    public void computeTour() {
        setStops();
        dijkstra();
        tsp();
    }


    private void dijkstra() {

        int nbIntersections = associatedMap.getIntersections().size();
        int[][] predecessors = new int[stops.size()][nbIntersections];
        stopsGraph = new CompleteGraph(stops.size());

        int stopIndex = 0; // need index of currStop in the list stops to fill predecessors

        System.out.println("Dijkstra START");
        for(int currStopId : stops) {
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
            dist[currStopId] = 0; // distance to current stop is 0

            pi[currStopId] = -1; //null, starting stop won't have predecessors
            pq.add(currStopId);

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

            Stop currStop = stopMap.get(stops.get(stopIndex));

            for (int i = 0; i < stops.size(); i++) {

                if (i != stopIndex) {

                    List<Segment> pathSegments = new ArrayList<>();

                    // Add initial segment
                    Intersection initialIntersection = associatedMap.getIntersections().get(stops.get(i));
                    int predecessor = predecessors[stopIndex][stops.get(i)];
                    Intersection currIntersection = associatedMap.getIntersections().get(predecessor);
                    pathSegments.add(currIntersection.findSegmentTo(initialIntersection));

                    Stop nextStop = stopMap.get(stops.get(i));
                    Path path = new Path(currStop,nextStop);

                    // Get intermediary segments
                    while (predecessor != stops.get(stopIndex)) {
                        predecessor = predecessors[stopIndex][predecessor];
                        Intersection nextIntersection = associatedMap.getIntersections().get(predecessor);
                        pathSegments.add(nextIntersection.findSegmentTo(currIntersection));
                        currIntersection = associatedMap.getIntersections().get(predecessor);
                    }

                    // Store info in path and save it
                    Collections.reverse(pathSegments);
                    path.setSegments(pathSegments);
                    path.setLength(dist[stops.get(i)]);
                    stopsGraph.setPath(stopIndex, i, path);
                    stopsGraph.setCost(stopIndex,i, dist[stops.get(i)]);
                }

            }
            stopIndex++;

        }
        //TESTS :
        /*System.out.println("stops graph : ");
        for (int i = 0; i< stops.size(); i++) {
            for (int j = 0; j< stops.size(); j++) {
                System.out.println(stopsGraph.getCost(i,j)+" ");
            }
            System.out.println();
        }*/
        System.out.println("END Dijkstra");

    } // ---- END of dijkstra

    public void setStopTimeAndNumber(){

        LocalTime currentTime = departureTime;
        for(int i = 0; i < tourPaths.size(); i++) {

            Stop currentStop = tourPaths.get(i).getOrigin();

            currentStop.setStopNumber(i);
            currentStop.setArrivalTime(currentTime);
            currentTime = currentTime.plusSeconds(currentStop.getDuration());
            currentStop.setDepartureTime(currentTime);

            double d = tourPaths.get(i).getLength();
            int t = (int)(d/(15/3.6))+1;
            currentTime = currentTime.plusSeconds(t);

        }
        Stop currentStop = tourPaths.get(tourPaths.size()-1).getDestination();
        currentStop.setStopNumber(tourPaths.size()-1);
        currentStop.setArrivalTime(currentTime);

        notifyObservers(UpdateType.TOUR);
    }

    private void tsp() {

        // Compute TSP
        TSP tsp = new TSP3();
        long startTime = System.currentTimeMillis();
        System.out.println("TSP START");
        tsp.searchSolution(20000, stopsGraph);
        System.out.println("Solution of cost " + tsp.getSolutionCost() + " found in " + (System.currentTimeMillis() - startTime) + "ms");

        tourPaths = new ArrayList<>();
        for(int i = 0; i < stopsGraph.getNbVertices() - 1; i++) {
            tourPaths.add(stopsGraph.getPath(tsp.getSolution(i),tsp.getSolution(i+1)));
        }
        tourPaths.add(stopsGraph.getPath(tsp.getSolution(stopsGraph.getNbVertices()-1), tsp.getSolution(0)));

        setStopTimeAndNumber();
        notifyObservers(UpdateType.TOUR);

    } // ---- END of TSP

    // Branch&Bound (notes for myself)
    /* H1 = 0
    /* H2 = (nbUnvisited+1)*dMin
    /* H3 = l + sum of li
    /* Improvement = Sort unvisited by shortest cost to last visited vertex
    */

    /* Best Algo -> Limited Discrepancy Search (LDS)
    */


    public void addRequest() {
        Request newRequest = getRequestList().get(getRequestList().size()-1);
        Stop pickup = newRequest.getPickup();
        Stop delivery = newRequest.getDelivery();

        stops.add(pickup.getAddress().getId());
        stops.add(delivery.getAddress().getId());
        stopMap.put(pickup.getAddress().getId(),pickup);
        stopMap.put(delivery.getAddress().getId(),delivery);

        dijkstra();
        tourPaths.remove(tourPaths.size()-1);
        Stop lastStop = tourPaths.get(tourPaths.size()-1).getDestination();

        Integer indexLastStop = -1;
        for(int i = 0; i < stops.size();i++){
            if(stops.get(i) == lastStop.getAddress().getId()){
                indexLastStop = i;
                break;
            }
        }

        Path lastToPickup = stopsGraph.getPath(indexLastStop,stops.size()-2);
        tourPaths.add(lastToPickup);

        //tests :
        System.out.println("path1 "+  lastToPickup);
        System.out.println("origin  : "+lastToPickup.getOrigin()+" destination : "+lastToPickup.getDestination());
        for(int i=0; i<lastToPickup.getSegments().size();i++){
            System.out.println(lastToPickup.getSegments().get(i));
        }


        Path pickupToDelivery = stopsGraph.getPath(stops.size()-2,stops.size()-1);
        tourPaths.add(pickupToDelivery);
        Path deliveryToWarehouse = stopsGraph.getPath(stops.size()-1,0);
        tourPaths.add(deliveryToWarehouse);
        setStopTimeAndNumber();
    }



    /**
     * Generates a String which describes the object
     * @return type String
     */
    @Override
    public String toString() {
        return "TourData{"
                + "requestList=" + requestList
                + ", associatedMap=" + associatedMap
                + ", warehouse=" + warehouse
                + ", departureTime='" + departureTime + '\''
                + '}';
    }
}
