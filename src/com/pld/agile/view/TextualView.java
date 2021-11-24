package com.pld.agile.view;

import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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

    public void populateTextualView() {

        VBox requestListContainer = new VBox(20);
        List<Request> requests = tourData.getRequestList();

        if (requests.size() == 0) return;

        VBox warehousePanel = new TextualViewItem(tourData.getWarehouse(), ItemType.WAREHOUSE, Color.BLACK);
        requestListContainer.getChildren().add(warehousePanel);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            Stop delivery = request.getDelivery();
            Color colour = ViewUtilities.stringToColour(pickup.getAddress().toString());

            VBox requestPanel1 = new TextualViewItem(pickup, ItemType.PICKUP, colour);
            VBox requestPanel2 = new TextualViewItem(delivery, ItemType.DELIVERY, colour);
            requestListContainer.getChildren().addAll(requestPanel1, requestPanel2);

        }

        requestListContainer.getStyleClass().add("white-background");
        component.setContent(requestListContainer);

    }

    @Override
    public void update(Observable o, UpdateType updateType) {
        switch (updateType) {
            case REQUESTS -> populateTextualView();
        }
    }

    public Node getComponent() {
        return component;
    }
}
