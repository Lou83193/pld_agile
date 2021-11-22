package com.pld.agile.model.tour;

import com.pld.agile.Observable;
import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
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

    private int [][] stopsGraph;

    private Intersection[][] predecessors;

    private String [] stops;

    private List<Stop> computedPath;

    /**
     * TourData constructor
     */
    public TourData() {
        super();
        requestList = new ArrayList<>();
        associatedMap = null;
        departureTime = "";
        warehouse = null;
    }


    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
        notifyObservers(this);
    }

    public MapData getAssociatedMap() {
        return associatedMap;
    }

    public void setAssociatedMap(MapData associatedMap) {
        this.associatedMap = associatedMap;
        notifyObservers(this);
    }

    public Stop getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Stop warehouse) {
        this.warehouse = warehouse;
        notifyObservers(this);
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
        notifyObservers(this);
    }


    private void setStops(){
        stops[0]=warehouse.getAddress().getId();
        for(int i=1; i<requestList.size(); i=i+2){
            stops[i]=requestList.get(i).getPickup().getAddress().getId();//add pickup
            stops[i+1]=requestList.get(i).getDelivery().getAddress().getId();//add delivery

        }
    }

    public void dijkstra(){

        for(int i=0; i< stops.length;i++){
            PriorityQueue<Pair<Intersection, Integer>> distances = new PriorityQueue<Pair<Intersection, Integer>>();

        }
    }

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
