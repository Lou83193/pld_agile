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

        // Add observers
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

            VBox requestPanel = new VBox(10);

            // Pickup panel -------------------------------------------------------------------------------------------
            VBox pickupStopPanel = new VBox(8);
            HBox pickupStopLabelPanel = new HBox(8);
            GridPane pickupStopInfoPane = new GridPane();
            pickupStopInfoPane.setVgap(4);

            // Label
            Circle pickupGraphic = new Circle(8);
            pickupGraphic.setFill(colour);
            Text pickupStopLabelText = new Text("Pickup Stop");
            pickupStopLabelText.getStyleClass().add("stopTextualLabel");
            pickupStopLabelPanel.setAlignment(Pos.CENTER_LEFT);
            pickupStopLabelPanel.getChildren().addAll(pickupGraphic, pickupStopLabelText);

            // Latitude
            Text pickupStopLatText = new Text("Latitude: ");
            TextField pickupStopLatInput = new TextField(pickupLat + "");
            pickupStopLatInput.setEditable(false);
            pickupStopLatInput.setMouseTransparent(true);
            pickupStopLatInput.setFocusTraversable(false);
            pickupStopInfoPane.add(pickupStopLatText, 0, 0);
            pickupStopInfoPane.add(pickupStopLatInput, 1, 0);

            // Longitude
            Text pickupStopLonText = new Text("Longitude: ");
            TextField pickupStopLonInput = new TextField(pickupLon + "");
            pickupStopLonInput.setEditable(false);
            pickupStopLonInput.setMouseTransparent(true);
            pickupStopLonInput.setFocusTraversable(false);
            pickupStopInfoPane.add(pickupStopLonText, 0, 1);
            pickupStopInfoPane.add(pickupStopLonInput, 1, 1);

            // Duration
            Text pickupStopDurationText = new Text("Duration: ");
            TextField pickupStopDurationInput = new TextField(pickupDuration + "");
            pickupStopDurationInput.setEditable(false);
            pickupStopDurationInput.setMouseTransparent(true);
            pickupStopDurationInput.setFocusTraversable(false);
            pickupStopInfoPane.add(pickupStopDurationText, 0, 2);
            pickupStopInfoPane.add(pickupStopDurationInput, 1, 2);

            // Add it all together
            pickupStopPanel.getChildren().addAll(pickupStopLabelPanel, pickupStopInfoPane);

            // Delivery panel -----------------------------------------------------------------------------------------
            VBox deliveryStopPanel = new VBox(8);
            HBox deliveryStopLabelPanel = new HBox(8);
            GridPane deliveryStopInfoPane = new GridPane();
            deliveryStopInfoPane.setVgap(4);

            // Label
            Rectangle deliveryGraphic = new Rectangle(16, 16);
            deliveryGraphic.setFill(colour);
            Text deliveryStopLabelText = new Text("Delivery Stop");
            deliveryStopLabelText.getStyleClass().add("stopTextualLabel");
            deliveryStopLabelPanel.setAlignment(Pos.CENTER_LEFT);
            deliveryStopLabelPanel.getChildren().addAll(deliveryGraphic, deliveryStopLabelText);

            // Latitude
            Text deliveryStopLatText = new Text("Latitude: ");
            TextField deliveryStopLatInput = new TextField(deliveryLat + "");
            deliveryStopLatInput.setEditable(false);
            deliveryStopLatInput.setMouseTransparent(true);
            deliveryStopLatInput.setFocusTraversable(false);
            deliveryStopInfoPane.add(deliveryStopLatText, 0, 0);
            deliveryStopInfoPane.add(deliveryStopLatInput, 1, 0);

            // Longitude
            Text deliveryStopLonText = new Text("Longitude: ");
            TextField deliveryStopLonInput = new TextField(deliveryLon + "");
            deliveryStopLonInput.setEditable(false);
            deliveryStopLonInput.setMouseTransparent(true);
            deliveryStopLonInput.setFocusTraversable(false);
            deliveryStopInfoPane.add(deliveryStopLonText, 0, 1);
            deliveryStopInfoPane.add(deliveryStopLonInput, 1, 1);

            // Duration
            Text deliveryStopDurationText = new Text("Duration: ");
            TextField deliveryStopDurationInput = new TextField(deliveryDuration + "");
            deliveryStopDurationInput.setEditable(false);
            deliveryStopDurationInput.setMouseTransparent(true);
            deliveryStopDurationInput.setFocusTraversable(false);
            deliveryStopInfoPane.add(deliveryStopDurationText, 0, 2);
            deliveryStopInfoPane.add(deliveryStopDurationInput, 1, 2);

            // Add it all together
            deliveryStopPanel.getChildren().addAll(deliveryStopLabelPanel, deliveryStopInfoPane);

            requestPanel.setPadding(new Insets(10));
            requestPanel.getChildren().addAll(pickupStopPanel, deliveryStopPanel);
            requestPanel.getStyleClass().add("requestTextualPanel");

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
