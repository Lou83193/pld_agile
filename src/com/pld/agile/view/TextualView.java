package com.pld.agile.view;

import com.pld.agile.model.tour.StopType;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.List;

public class TextualView implements Observer {

    private ScrollPane component;
    private TourData tourData;

    public TextualView(TourData tourData) {

        this.tourData = tourData;
        tourData.addObserver(this);

        // Create ScrollPane
        component = new ScrollPane();
        component.setPadding(new Insets(20));
        component.setFitToWidth(true);
        component.setMaxWidth(Double.MAX_VALUE);
        component.getStyleClass().add("white-background");

    }

    public void populateInitialTextualView() {

        VBox requestListContainer = new VBox(20);
        List<Request> requests = tourData.getRequestList();

        if (requests.size() == 0) return;

        VBox warehousePanel = new TextualViewItem(tourData.getWarehouse());
        requestListContainer.getChildren().add(warehousePanel);

        for (Request request : requests) {
            VBox requestPanel1 = new TextualViewItem(request.getPickup());
            VBox requestPanel2 = new TextualViewItem(request.getDelivery());
            requestListContainer.getChildren().addAll(requestPanel1, requestPanel2);
        }

        requestListContainer.getStyleClass().add("white-background");
        component.setContent(requestListContainer);

    }

    public void populateTourTextualView() {

        VBox requestListContainer = new VBox(20);
        List<Request> requests = tourData.getRequestList();
        List<Integer> tourOrder = tourData.getComputedPath();

        if (requests.size() == 0) return;

        for (Integer i : tourOrder) {
            Integer stopId = tourData.getStops().get(i);
            Stop stop = tourData.getStopMap().get(stopId);
            VBox requestPanel = new TextualViewItem(stop);
            requestListContainer.getChildren().add(requestPanel);
        }

        requestListContainer.getStyleClass().add("white-background");
        component.setContent(requestListContainer);

    }

    @Override
    public void update(Observable o, UpdateType updateType) {
        switch (updateType) {
            case REQUESTS -> populateInitialTextualView();
            case TOUR -> populateTourTextualView();
        }
    }

    public Node getComponent() {
        return component;
    }
}
