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
     * Singleton instance.
     */
    private static TourData singletonInstance;

    /**
     * TourData constructor, private for the singleton design pattern.
     */
    private TourData() {
        super();
        requestList = new ArrayList<>();
        associatedMap = null;
        departureTime = "";
        warehouse = null;
    }

    /**
     * Getter for the singleton instance
     * @return the singleton instance of MapData
     */
    public static TourData getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new TourData();
        }
        return singletonInstance;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public MapData getAssociatedMap() {
        return associatedMap;
    }

    public void setAssociatedMap(MapData associatedMap) {
        this.associatedMap = associatedMap;
    }

    public Stop getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Stop warehouse) {
        this.warehouse = warehouse;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
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
