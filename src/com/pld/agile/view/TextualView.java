package com.pld.agile.view;

import com.pld.agile.Observable;
import com.pld.agile.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class TextualView implements Observer {

    private VBox component;
    private TourData tourData;

    public TextualView(TourData tourData, Scene parent) {

        this.tourData = tourData;

        // Add observers
        tourData.addObserver(this);

        // Create VBox
        component = new VBox();

    }

    public void populateTextualView() {

        List<Request> requests = tourData.getRequestList();

        if (requests.size() == 0) return;

        component.getChildren().clear();

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            Stop delivery = request.getDelivery();

            double pickupLat = pickup.getAddress().getLatitude();
            double pickupLon = pickup.getAddress().getLongitude();
            double pickupDuration = pickup.getDuration();

            double deliveryLat = delivery.getAddress().getLatitude();
            double deliveryLon = delivery.getAddress().getLongitude();
            double deliveryDuration = delivery.getDuration();

            Color colour = ViewUtilities.stringToColour(pickup.getAddress().toString());

            Group requestPanel = new Group();
            //TODO: Populate panel

            component.getChildren().add(requestPanel);

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        populateTextualView();
    }

    public Node getComponent() {
        return component;
    }
}
