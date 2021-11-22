package com.pld.agile.model.tour;

import com.pld.agile.Observable;
import com.pld.agile.model.map.MapData;

import java.util.ArrayList;
import java.util.List;

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
