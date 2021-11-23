package com.pld.agile.view;

import com.pld.agile.Observable;
import com.pld.agile.Observer;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
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

        VBox warehousePanel = new TextualViewItem(tourData.getWarehouse());
        requestListContainer.getChildren().add(warehousePanel);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            Stop delivery = request.getDelivery();

            VBox requestPanel = new TextualViewItem(pickup, delivery);
            requestListContainer.getChildren().add(requestPanel);

        }

        requestListContainer.getStyleClass().add("white-background");
        component.setContent(requestListContainer);

    }

    @Override
    public void update(Observable o, Object arg) {
        populateTextualView();
    }

    public Node getComponent() {
        return component;
    }
}
