/*
 * TourData
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.model.tour;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.exception.PathException;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.tsp.*;
import javafx.application.Platform;

import java.time.LocalTime;
import java.util.*;

/**
 * Stores the data of a loaded requests list.
 */
public class TourData extends Observable implements Observer {

    /**
     * List of stops composing the tour.
     */
    private List<Stop> stopsList;
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
    /**
     * Graph containing the minimal length between each stop of the request list,
     * as well as the Path object between them. Computed with dijkstra.
     */
    private Graph stopsGraph;
    /**
     * The list of Paths composing the tour
     */
    private List<Path> tourPaths;
    /**
     * The thread computing the tour (both dijkstra and tsp)
     */
    private Thread tourComputingThread;

    /**
     * TourData constructor.
     */
    public TourData() {
        super();
        //requestList = new ArrayList<>();
        stopsList = new ArrayList<>();
        tourPaths = new ArrayList<>();
        associatedMap = null;
        departureTime = null;
        warehouse = null;
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
     * Setter for attribute tourPaths.
     * @param tourPaths list of tour's path
     */
    public void setTourPaths(List<Path> tourPaths) {
        this.tourPaths = tourPaths;
    }

    /**
     * Setter for attribute stopsGraph.
     * @param stopsGraph tour's stops graph
     */
    public void setStopsGraph(Graph stopsGraph) {
        this.stopsGraph = stopsGraph;
    }

    /**
     * Setter for attribute tourComputingThread.
     * @param tourComputingThread tour' computingThread
     */
    public void setTourComputingThread(Thread tourComputingThread) {
        this.tourComputingThread = tourComputingThread;
    }

    /**
     * Creates a new request with a pickup which is created at the given intersection,
     * stores it in the list of stops.
     * @param intersection The intersection of the pickup.
     * @return The new request.
     */
    public Request constructNewRequest1(Intersection intersection) {
        Request newRequest = new Request();
        Stop newPickup = new Stop(newRequest, intersection, 0, StopType.PICKUP);
        newRequest.setPickup(newPickup);
        stopsList.add(newPickup);
        notifyObservers(UpdateType.TOUR);
        return newRequest;
    }

    /**
     * Adds a delivery to the new request (the last one in the list) which is created at the given intersection,
     * stores it in the list of stops.
     * @param intersection The intersection of the delivery.
     * @return The new request.
     */
    public Request constructNewRequest2(Intersection intersection) {
        Request newRequest = stopsList.get(stopsList.size()-1).getRequest();
        Stop newDelivery = new Stop(newRequest, intersection, 0, StopType.DELIVERY);
        newRequest.setDelivery(newDelivery);
        stopsList.add(newDelivery);
        notifyObservers(UpdateType.TOUR);
        return newRequest;
    }

    /**
     * Removes the latest stop from the list of stops
     */
    public void deconstructNewRequest1() {
        if (stopsList.size() > 1) {
            stopsList.remove(stopsList.size() - 1);
            notifyObservers(UpdateType.TOUR);
        }
    }

    /**
     * Adds the latest request at the end of the tour (by computing dijkstra again and repopulating the tourPaths list).
     * @throws PathException If computing dijkstra with the new request caused an exception.
     */
    public void addLatestRequest() throws PathException {

        dijkstra();

        if (tourPaths.size() > 2) {
            tourPaths.remove(tourPaths.size() - 1);
            Stop lastStop = tourPaths.get(tourPaths.size() - 1).getDestination();
            Path lastToPickup = stopsGraph.getPath(lastStop.getId(), stopsList.size() - 2);
            tourPaths.add(lastToPickup);
            Path pickupToDelivery = stopsGraph.getPath(stopsList.size() - 2, stopsList.size() - 1);
            tourPaths.add(pickupToDelivery);
            Path deliveryToWarehouse = stopsGraph.getPath(stopsList.size() - 1, 0);
            tourPaths.add(deliveryToWarehouse);
        } else {
            tourPaths.remove(tourPaths.get(0));
            Path warehouseToPickup = stopsGraph.getPath(0, stopsList.size() - 2);
            tourPaths.add(warehouseToPickup);
            Path pickupToDelivery = stopsGraph.getPath(stopsList.size() - 2, stopsList.size() - 1);
            tourPaths.add(pickupToDelivery);
            Path deliveryToWarehouse = stopsGraph.getPath(stopsList.size() - 1, 0);
            tourPaths.add(deliveryToWarehouse);
        }

        updateStopsTimesAndNumbers();

    }

    /**
     * Removes a request from the tour.
     * @param request The request to be removed.
     * @return boolean success.
     */
    public void deleteRequest(Request request) {

        Stop pickup = request.getPickup();
        Stop delivery = request.getDelivery();
        Stop currentOrigin = null;
        Stop currentDestination;

        for (int i = 0; i < tourPaths.size(); i++) {
            Path path = tourPaths.get(i);

            /*
             * If we found the request which we want to remove in the previous iteration,
             * we find the new path between the previous stop and the next stop,
             * and we add it to the tourPath.
             */
            if (currentOrigin != null) {

                //Store destination and remove path to it
                currentDestination = path.getDestination();
                tourPaths.remove(path);
                if (currentDestination == delivery) {
                    currentDestination = tourPaths.get(i).getDestination();
                    path = tourPaths.get(i);
                    tourPaths.remove(path);
                }

                //Find new path
                Path newPath = stopsGraph.getPath(currentOrigin.getId(), currentDestination.getId());

                //Insert in position i
                tourPaths.add(i, newPath);
                currentOrigin = null;

            }

            /*
             * If the destination of the current path is the stop which we want to remove,
             * we store the origin of that stop and remove the path to it
             */
            if (path.getDestination().equals(pickup) || path.getDestination().equals(delivery)) {
                currentOrigin = path.getOrigin();
                tourPaths.remove(path);
                i--;
            }

        }

        // Remove from stops list, decrease Stop Id counter
        stopsList.removeIf(pickup::equals);
        stopsList.removeIf(delivery::equals);
        Stop.decreaseIdCounter(2);

        if (tourPaths.size() != 1 || tourPaths.get(0) != null) {
            updateStopsTimesAndNumbers();
        } else {
            Path emptyPath = new Path(stopsList.get(0), stopsList.get(0));
            emptyPath.setSegments(new ArrayList<>());
            emptyPath.setLength(0);
            tourPaths.add(emptyPath);
            tourPaths.remove(tourPaths.get(0));
            notifyObservers(UpdateType.TOUR);
        }

    }

    /**
     * Returns whether a stop's order is shiftable up or down.
     * A stop's order cannot be shifted up or down if it's already at the end of the stop list,
     * and a pickup cannot be shifted after a delivery and vice versa.
     * @param stop The stop to be checked.
     * @param dir The direction of the shift (up (-1) or down (+1)).
     * @return Whether the stop is shiftable in the given direction.
     */
    public boolean stopIsShiftable(Stop stop, int dir) {

        int stopIndex = 0;

        //Build a list of all the stops in the tour in order
        ArrayList<Stop> listStops = new ArrayList<>();
        listStops.add(tourPaths.get(0).getOrigin());
        for (int i = 0; i < tourPaths.size(); i++) {
            Stop currStop = tourPaths.get(i).getDestination();
            listStops.add(currStop);
            if (currStop.equals(stop)) { stopIndex = i+1; }
        }

        //Check if the stop is allowed to move in the direction
        boolean canMove = true;
        Stop neighbourStop;
        if ((stopIndex < 2 && dir < 0) || (stopIndex > listStops.size() - 3 && dir > 0)) {
            canMove = false;
        } else {
            neighbourStop = listStops.get(stopIndex + dir);
            boolean sameRequest = stop.getRequest().equals(neighbourStop.getRequest());
            if (sameRequest && ((stop.getType() == StopType.DELIVERY && dir < 0) || (stop.getType() == StopType.PICKUP && dir > 0))) {
                canMove = false;
            }
        }

        return canMove;

    }

    /**
     * Shifts a stop's order up or down.
     * @param stop The stop to be shifted.
     * @param dir The direction of the shift (up (-1) or down (+1)).
     * @return Whether the stop has indeed been shifted.
     */
    public boolean shiftStopOrder(Stop stop, int dir) {

        int stopIndex = 0;

        //Build a list of all the stops in the tour in order
        ArrayList<Stop> tourStops = new ArrayList<>();
        tourStops.add(tourPaths.get(0).getOrigin());
        for (int i = 0; i < tourPaths.size(); i++) {
            Stop currStop = tourPaths.get(i).getDestination();
            tourStops.add(currStop);
            if (currStop.equals(stop)) { stopIndex = i+1; }
        }

        if (stopIsShiftable(stop, dir)) {

            //Shift the stop up one place
            Collections.swap(tourStops, stopIndex, stopIndex + dir);

            //Reconstruct tourPaths
            tourPaths.clear();
            int n = tourStops.size()-1;
            for (int i = 0; i < n; i++) {
                int indexOrigin = tourStops.get(i).getId();
                int indexDestination = tourStops.get((i + 1) % n).getId();
                Path path = stopsGraph.getPath(indexOrigin, indexDestination);
                tourPaths.add(path);
            }

            updateStopsTimesAndNumbers();
            return true;

        }

        return false;

    }

    /**
     * Moves a stop within a tour to a new intersection.
     * @param stop The stop to be moved.
     * @param newIntersection The new intersection address of the stop.
     */
    public void moveStop(Stop stop, Intersection newIntersection) throws PathException {

        stop.setAddress(newIntersection);

        dijkstra();

        // TODO:
        // - iterate through tourPaths, find the stop
        // - set its paths to the new paths from stopsGraph (which has just been updated by dijkstra)

        updateStopsTimesAndNumbers();
    }

    public List<Stop> getStopsList() {
        return stopsList;
    }

    public void setStopsList(List<Stop> stopsList) {
        this.stopsList = stopsList;
        notifyObservers(UpdateType.REQUESTS);
    }

    /**
     * Getter for attribute tourPaths.
     * @return tourPaths a List of Paths composing the computed tour
     */
    public List<Path> getTourPaths() {
        return tourPaths;
    }

    /**
     * Computes a tour, by first computing the paths between all stops with dijkstra,
     * then finding the best tour with tsp
     * @throws PathException If computing dijkstra caused an exception.
     */
    public void computeTour() throws PathException {
        dijkstra();
        tsp();
    }

    /**
     * Stops the thread computing the tour, is only effective if tsp has returned an intermediary result
     * (in which case, that result will become effective). Otherwise, the thread is not interrupted yet.
     * @return Whether the thread has indeed been interrupted or not.
     */
    public boolean stopComputingTour() {
        if (tourPaths.size() > 0) {
            tourComputingThread.interrupt();
            notifyObservers(UpdateType.TOUR);
            return true;
        }
        return false;
    }

    /**
     * Executes the dijkstra algotihm on the list of stops and populates the stopGraph
     * by constructing the Paths between each stop.
     * @throws PathException If one of the Paths could not be constructed from the dijkstra results.
     */
    private void dijkstra() throws PathException {

        ArrayList<Integer> stops = new ArrayList<>();
        for (Stop s : stopsList) {
            stops.add(s.getAddress().getId());
        }

        int nbIntersections = associatedMap.getIntersections().size();
        int[][] predecessors = new int[stopsList.size()][nbIntersections];
        stopsGraph = new CompleteGraph(stopsList.size());

        int stopIndex = 0; // need index of currStop in the list stops to fill predecessors

        for (int currStopId : stops) {

            // Current Stop Variables
            double [] dist = new double[nbIntersections]; //index = intersection id in map data
            int [] pi = new int[nbIntersections]; //index = intersection id in map data
            Set<Integer> settled = new HashSet<>();

            PriorityQueue<Integer> pq = new PriorityQueue<>(
                (o1, o2) -> {
                    if (dist[o1] < dist[o2]) {
                        return -1;
                    }
                    if (dist[o1] > dist[o2]) {
                        return 1;
                    }
                    return 0;
                }
            );

            // Dist initialization
            for (int i = 0; i < nbIntersections; i++) {
                dist[i] = Double.MAX_VALUE;
            }
            dist[currStopId] = 0; // distance to current stop is 0

            pi[currStopId] = currStopId; //null, starting stop won't have predecessors
            pq.add(currStopId);

            int nbStopCalculated = 0;

            while (nbStopCalculated != stopsList.size()) {

                    if (pq.isEmpty()) {
                        break;
                    }

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

            Stop currStop = stopsList.get(stopIndex);
            for (int i = 0; i < stopsList.size(); i++) {

                if (i != stopIndex) {

                    List<Segment> pathSegments = new ArrayList<>();

                    // Add initial segment
                    Intersection initialIntersection = associatedMap.getIntersections().get(stops.get(i));
                    int predecessor = predecessors[stopIndex][stops.get(i)];
                    Intersection currIntersection = associatedMap.getIntersections().get(predecessor);
                    Segment segmentTo = currIntersection.findSegmentTo(initialIntersection);
                    if (!currIntersection.equals(initialIntersection) && segmentTo == null) {
                        throw new PathException("Unable to compute paths for this request");
                    }
                    pathSegments.add(segmentTo);
                    Stop nextStop = stopsList.get(i);
                    Path path = new Path(currStop, nextStop);

                    // Get intermediary segments
                    while (predecessor != stops.get(stopIndex)) {
                        predecessor = predecessors[stopIndex][predecessor];
                        Intersection nextIntersection = associatedMap.getIntersections().get(predecessor);
                        segmentTo = nextIntersection.findSegmentTo(currIntersection);
                        if (!currIntersection.equals(nextIntersection) && segmentTo == null) {
                            throw new PathException("Unable to compute paths for this request");
                        }
                        pathSegments.add(segmentTo);
                        currIntersection = associatedMap.getIntersections().get(predecessor);
                    }

                    // Store info in path and save it
                    Collections.reverse(pathSegments);
                    path.setSegments(pathSegments);
                    path.setLength(dist[stops.get(i)]);
                    stopsGraph.setPath(stopIndex, i, path);
                    stopsGraph.setCost(stopIndex, i, dist[stops.get(i)]);
                }

            }
            stopIndex++;

        }

    }

    /**
     * Sets the stopNumber and departure / arrival time attributes of each Stop
     * based on their order in the list, their duration, the departure time, and the bike's speed.
     */
    private void setStopsTimesAndNumbers() {

        LocalTime currentTime = departureTime;
        for (int i = 0; i < tourPaths.size(); i++) {
            Stop currentStop = tourPaths.get(i).getOrigin();
            currentStop.setStopNumber(i);
            currentStop.setArrivalTime(currentTime);
            currentTime = currentTime.plusSeconds(currentStop.getDuration());
            currentStop.setDepartureTime(currentTime);
            double d = tourPaths.get(i).getLength();
            int t = (int) (d / (15 / 3.6)) + 1;
            currentTime = currentTime.plusSeconds(t);
        }
        if (tourPaths.size() > 0) {
            Stop currentStop = tourPaths.get(tourPaths.size() - 1).getDestination();
            currentStop.setStopNumber(0);
            currentStop.setArrivalTime(currentTime);
        }

    }

    /**
     * Updates the stop times and numbers while notifying the view.
     */
    public void updateStopsTimesAndNumbers() {
        setStopsTimesAndNumbers();
        notifyObservers(UpdateType.TOUR);
    }

    /**
     * Executes the tsp algorithm on the graph of stops.
     */
    private void tsp() {

        /*
        Branch & Bound :
             H1 = 0
             H2 = (nbUnvisited+1)*dMin
             H3 = l + sum of li
             Improvement = Sort unvisited by shortest cost to last visited vertex
             Best Algo -> Limited Discrepancy Search (LDS)
         */

        TSP tsp = new TSP3(this);
        long startTime = System.currentTimeMillis();
        System.out.println("TSP START");
        tsp.searchSolution(120000, stopsGraph);
        Platform.runLater(() -> {
            if (tourComputingThread != null && !tourComputingThread.isInterrupted()) {
                System.out.println("TSP solution found in " + (System.currentTimeMillis() - startTime) + "ms");
                processTSPUpdate(tsp);
            }
        });

    }

    /**
     * Processes a TSP result (algorithm giving an intermediary or final result)
     * by populating tourPaths with the appropriate Paths from the stopsGraph
     * and setting the stop's times / order of passage
     * @param tsp The TSP instance holding the result
     */
    public void processTSPUpdate(TSP tsp) {
        System.out.println("Solution cost: " + tsp.getSolutionCost());
        tourPaths = new ArrayList<>();
        int n = stopsGraph.getNbVertices();
        for(int i = 0; i < n-1; i++) {
            tourPaths.add(stopsGraph.getPath(tsp.getSolution(i),tsp.getSolution(i+1)));
        }
        tourPaths.add(stopsGraph.getPath(tsp.getSolution(n-1), tsp.getSolution(0)));
        setStopsTimesAndNumbers();
    }

    /**
     * Called when the TSP instance notifies TourData that an intermediary result
     * has been found. If the computing thread is still going, processes that intermediary result and notifies the view
     * (otherwise it's no use, the view has already been notified of the final result in stopComputingTour())
     * @param o The observable object who notified the view.
     * @param updateType The type of update that has been made.
     */
    @Override
    public void update(Observable o, UpdateType updateType) {
        if (updateType == UpdateType.INTERMEDIARY_TSP && tourComputingThread != null && !tourComputingThread.isInterrupted()) {
            TSP tsp = (TemplateTSP) o;
            processTSPUpdate(tsp);
            notifyObservers(UpdateType.INTERMEDIARY_TOUR);
        }
    }

    /**
     * Generates a String which describes the object
     * @return type String
     */
    @Override
    public String toString() {
        return "TourData{"
                + "stopsList=" + stopsList
                + ", associatedMap=" + associatedMap
                + ", warehouse=" + warehouse
                + ", departureTime='" + departureTime + '\''
                + '}';
    }

}
