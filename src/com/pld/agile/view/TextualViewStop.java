package com.pld.agile.view;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Graphical object representing a Stop in the textual view.
 * A Stop is represented (in the textual view) by a panel
 * containing text fields displaying its attributes.
 */
public class TextualViewStop extends VBox implements Observer {

    ScrollPane scrollPane;

    /**
     * TextualViewStop constructor.
     * Populates the graphical object.
     * @param stop The corresponding Stop model object.
     * @param editable Whether the component has edit buttons or not.
     */
    public TextualViewStop(Stop stop, TextualView parent, boolean editable) {

        stop.addObserver(this);

        this.scrollPane = (ScrollPane) parent.getComponent();

        double lat = stop.getAddress().getLatitude();
        double lon = stop.getAddress().getLongitude();
        double duration = stop.getDuration();
        StopType type = stop.getType();
        LocalTime departureTime = stop.getDepartureTime();
        String hour = "";
        if (departureTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            hour = formatter.format(departureTime);
        }
        int stopNumber = stop.getStopNumber();

        BorderPane panel = new BorderPane();
        BorderPane contentPane = new BorderPane();
        GridPane infoPane = new GridPane();
        infoPane.setHgap(4);
        infoPane.setVgap(4);
        infoPane.setPadding(new Insets(4));

        HBox labelPanel = new HBox(8);
        // Stop Icon
        GraphicalViewStop labelGraphic = new GraphicalViewStop(stop, 14, stopNumber);
        // Label
        String labelTextString = "";
        switch (type) {
            case PICKUP -> labelTextString = "Pickup Stop";
            case DELIVERY -> labelTextString = "Delivery Stop";
            case WAREHOUSE -> labelTextString = "Warehouse";
        }
        Text labelText = new Text(labelTextString);
        labelText.getStyleClass().add("textual-view-stop-panel-label");
        // Separator
        Text separatorText = new Text("-");
        separatorText.getStyleClass().add("textual-view-stop-panel-label");
        // Hour of passage
        Text hourText = new Text(hour);
        hourText.getStyleClass().add("textual-view-stop-panel-hour");
        labelPanel.setAlignment(Pos.CENTER_LEFT);
        labelPanel.getChildren().addAll(labelGraphic, labelText, separatorText, hourText);
        contentPane.setTop(labelPanel);

        // Position
        Text posText = new Text("Position:");
        TextField posInput = new TextField("(" + lat + "; " + lon + ")");
        posInput.setEditable(false);
        posInput.setMouseTransparent(true);
        posInput.setFocusTraversable(false);
        posInput.getStyleClass().add("uneditable-textfield");
        infoPane.add(posText, 0, 0);
        infoPane.add(posInput, 1, 0);

        // Duration
        if (type != StopType.WAREHOUSE) {
            Text durationText = new Text(" Duration:");
            TextField durationInput = new TextField((int)duration + "");
            durationInput.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // Replace with only numberical numbers
                    if (!newValue.matches("\\d*")) {
                        durationInput.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    // Update model
                    int duration1 = Integer.parseInt(durationInput.getText());
                    parent.getWindow().getController().changeStopDuration(stop, duration1);
                }
            );
            infoPane.add(durationText, 0, 1);
            infoPane.add(durationInput, 1, 1);
        }
        contentPane.setCenter(infoPane);
        panel.setLeft(contentPane);

        if (editable) {

            VBox controls = new VBox(6);
            // Delete button
            Image deleteIcon = new Image("deleteIcon.png", 20, 20, true, true);
            ImageView deleteIconView = new ImageView(deleteIcon);
            Button deleteButton = new Button();
            deleteButton.setGraphic(deleteIconView);
            deleteButton.getStyleClass().add("control-button");
            deleteButton.setOnMouseClicked(
                e -> parent.getWindow().getController().deleteRequest(stop.getRequest())
            );
            // Arrow up
            Image upIcon = new Image("arrowIcon.png", 20, 20, true, true);
            ImageView upIconView = new ImageView(upIcon);
            Button upButton = new Button();
            upButton.setGraphic(upIconView);
            upButton.getStyleClass().add("control-button");
            upButton.setOnMouseClicked(
                e -> parent.getWindow().getController().shiftStopOrderUp(stop)
            );
            // Arrow down
            Image downIcon = new Image("arrowIcon.png", 20, 20, true, true);
            ImageView downIconView = new ImageView(downIcon);
            downIconView.setRotate(180);
            Button downButton = new Button();
            downButton.setGraphic(downIconView);
            downButton.getStyleClass().add("control-button");
            downButton.setOnMouseClicked(
                    e -> parent.getWindow().getController().shiftStopOrderDown(stop)
            );
            controls.getChildren().addAll(deleteButton, upButton, downButton);
            panel.setRight(controls);

        }

        this.getChildren().add(panel);
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.getStyleClass().add("textual-view-stop-panel");
        this.setBorder(new Border(new BorderStroke(
                Color.TRANSPARENT,
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(2)
        )));

    }

    @Override
    public void update(Observable observed, UpdateType updateType) {

        switch (updateType) {
            case STOP_HIGHLIGHT -> {
                Stop stop = (Stop)observed;
                if (stop.isHighlighted()) {
                    this.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(10),
                            new BorderWidths(2)
                    )));
                    //ViewUtilities.ensureVisible(scrollPane, this);
                } else {
                    this.setBorder(new Border(new BorderStroke(
                            Color.TRANSPARENT,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(10),
                            new BorderWidths(2)
                    )));
                }
            }
        }

    }


}
