package com.pld.agile.model.tour;

import com.pld.agile.Observable;
import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;

import java.util.*;

import com.pld.agile.model.map.Segment;
import javafx.util.Pair;

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

    private double [][] stopsGraph; // to do

    //private HashMap<Long, HashMap<Long, Long>> predecessors;
    private Long [][] predecessors;

    private List<Long> stops;

    private List<Stop> computedPath;

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
        notifyObservers(this);
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
        notifyObservers(this);
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
        notifyObservers(this);
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
        notifyObservers(this);
    }


    private void setStops(){
        stops.add(warehouse.getAddress().getId());
        for(int i=1; i<requestList.size(); i++) {
            stops.add(requestList.get(i).getPickup().getAddress().getId());//add pickup
            System.out.println("id="+requestList.get(i).getPickup().getAddress().getId());
            stops.add(requestList.get(i).getDelivery().getAddress().getId());//add delivery
        }
    }

    public void dijkstra() {
        int nbIntersections = associatedMap.getIntersections().size();
        predecessors = new Long [nbIntersections-1][nbIntersections-1];

        for(Long currStop : stops) {
            int stopIndex =0; // need index of currStop to fill predecessors
            // Current Stop Variables
            //HashMap<Long, Double> dist = new HashMap<Long, Double>();
            //HashMap<Long, Long> pi = new HashMap<>();

            double [] dist = new double[nbIntersections-1]; //index = intersection id in map data
            Long [] pi = new Long [nbIntersections-1]; //index = intersection id in map data

            Set<Long> settled = new HashSet<Long>();

            PriorityQueue<Pair<Long, Double>> pq = new PriorityQueue<Pair<Long, Double>>(Comparator.comparing(Pair::getValue));

            // Distance to the source is 0
            for(int i=0;i<nbIntersections;i++){
                dist[i]=Double.MAX_VALUE;
            }
            dist [Math.toIntExact(currStop)]=0;
            pi[Math.toIntExact(currStop)]=(long)-1;//null, starting stop won't have predecessors
            //dist.put(currStop, 0.);

            pq.add(new Pair<>(currStop, 0.));

            while(settled.size() != nbIntersections) {

                if(pq.isEmpty())
                    return;

                // Sommet gris avec distance minimale
                Long node = pq.remove().getKey();

                for(Segment road : associatedMap.getIntersections().get(Math.toIntExact(node)).getOriginOf()) {
                    Long nextNode = road.getDestination().getId();

                    if(!settled.contains(nextNode)) {
                        // Relachement
                        //double distance = Double.MAX_VALUE;
                        /*
                        //if(dist.containsKey(nextNode))
                        {
                            //distance = dist.get(nextNode);

                        }
                        if(distance > dist.get(node) + road.getLength()) {
                            distance = dist.get(node) + road.getLength();
                            pq.put(nextNode, node);
                        }
                        dist.put(nextNode, distance);*/
                        double distance=dist[Math.toIntExact(nextNode)];
                        if(distance > dist[Math.toIntExact(node)] + road.getLength()) {
                            distance = dist[Math.toIntExact(node)] + road.getLength();
                            pi[Math.toIntExact(nextNode)]=node;
                        }

                        pq.add(new Pair<>(nextNode, distance));
                    }
                }

                settled.add(node);
            }

            //Check
            for(int i=0; i<nbIntersections-1; i++){
                predecessors [stopIndex][i]=pi[i];
            }
            for(int i=0; i< stops.size(); i++){
                stopsGraph [stopIndex][i]=dist[i];
            }

            stopIndex++;
            //predecessors.put(currStop, pi);

        }
    }

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
