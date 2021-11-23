package com.pld.agile.view;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

enum ItemType {
    PICKUP,
    DELIVERY,
    WAREHOUSE
}

public class TextualViewItem extends VBox {

    public TextualViewItem(Stop stop, ItemType type, Color colour) {

        double lat = stop.getAddress().getLatitude();
        double lon = stop.getAddress().getLongitude();

        VBox panel = constructItemSection(type, lat, lon, 0, colour);
        this.getChildren().add(panel);

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.getStyleClass().add("requestTextualPanel");

    }

    private VBox constructItemSection(ItemType type, double lat, double lon, double duration, Color colour) {

        VBox panel = new VBox(8);
        HBox labelPanel = new HBox(8);
        GridPane infoPane = new GridPane();
        infoPane.setVgap(4);

        // Label
        Shape labelGraphic = new Circle(8);
        String labelTextString = "Stop";
        switch (type) {
            case PICKUP:
                labelGraphic = new Circle(8);
                labelTextString = "Pickup Stop";
                break;
            case DELIVERY:
                labelGraphic = new Rectangle(16, 16);
                labelTextString = "Delivery Stop";
                break;
            case WAREHOUSE:
                labelGraphic = new Rectangle(14, 14);
                labelGraphic.setRotate(45);
                labelTextString = "Warehouse";
                break;
        }
        labelGraphic.setFill(colour);
        Text labelText = new Text(labelTextString);
        labelText.getStyleClass().add("stopTextualLabel");
        labelPanel.setAlignment(Pos.CENTER_LEFT);
        labelPanel.getChildren().addAll(labelGraphic, labelText);

        // Latitude
        Text latText = new Text("Latitude: ");
        TextField latInput = new TextField(lat + "");
        latInput.setEditable(false);
        latInput.setMouseTransparent(true);
        latInput.setFocusTraversable(false);
        infoPane.add(latText, 0, 0);
        infoPane.add(latInput, 1, 0);

        // Longitude
        Text lonText = new Text("Longitude: ");
        TextField lonInput = new TextField(lon + "");
        lonInput.setEditable(false);
        lonInput.setMouseTransparent(true);
        lonInput.setFocusTraversable(false);
        infoPane.add(lonText, 0, 1);
        infoPane.add(lonInput, 1, 1);

        // Duration
        if (type != ItemType.WAREHOUSE) {
            Text durationText = new Text("Duration: ");
            TextField durationInput = new TextField(duration + "");
            durationInput.setEditable(false);
            durationInput.setMouseTransparent(true);
            durationInput.setFocusTraversable(false);
            infoPane.add(durationText, 0, 2);
            infoPane.add(durationInput, 1, 2);
        }

        // Add it all together
        panel.getChildren().addAll(labelPanel, infoPane);
        return panel;

    }


}
