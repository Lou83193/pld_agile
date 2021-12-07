package com.pld.agile.view;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.utils.view.TimeTextField;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Graphical object representing a Stop in the textual view.
 * A Stop is represented (in the textual view) by a panel
 * containing text fields displaying its attributes.
 */
public class TextualViewStop extends VBox {

    /**
     * The parent ScrollPane containing all the textual stops.
     */
    private final ScrollPane scrollPane;
    /**
     * Tracker used to keep track of the modifications made
     * to the textual stop's text fields.
     */
    private String inputValueTracker;
    /**
     * The graphical stop icon contained within the textual view stop.
     */
    private final GraphicalViewStop labelGraphic;
    /**
     * The highlight level of the textual stop.
     * 2 = highlighted by selection.
     * 1 = highlighted by association.
     * 0 = not highlighted.
     */
    private int highlightLevel;

    /**
     * TextualViewStop constructor.
     * Populates the graphical object.
     * @param stop The corresponding Stop model object.
     * @param parent The parent TextualView instance.
     * @param editable Whether the component has edit buttons or not.
     */
    public TextualViewStop(Stop stop, TextualView parent, boolean editable) {

        this.scrollPane = (ScrollPane) parent.getComponent();

        double lat = stop.getAddress().getLatitude();
        double lon = stop.getAddress().getLongitude();
        double duration = stop.getDuration();
        StopType type = stop.getType();
        LocalTime arrivalTime = stop.getArrivalTime();
        String arrivalTimeString = "00:00";
        if (arrivalTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            arrivalTimeString = formatter.format(arrivalTime);
        }
        LocalTime departureTime = stop.getDepartureTime();
        String departureTimeString = "00:00";
        if (departureTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            departureTimeString = formatter.format(departureTime);
        }

        BorderPane panel = new BorderPane();
        BorderPane contentPane = new BorderPane();
        GridPane infoPane = new GridPane();
        infoPane.setHgap(4);
        infoPane.setVgap(4);
        infoPane.setPadding(new Insets(4));

        HBox labelPanel = new HBox(8);
        // Stop Icon
        labelGraphic = new GraphicalViewStop(stop, null, 14, false);
        // Label
        String labelTextString;
        switch (type) {
            case PICKUP -> labelTextString = "Pickup Stop";
            case DELIVERY -> labelTextString = "Delivery Stop";
            case WAREHOUSE -> labelTextString = "Warehouse";
            default -> labelTextString = "";
        }
        Text labelText = new Text(labelTextString);
        labelText.getStyleClass().add("textual-view-stop-panel-label");
        labelPanel.getChildren().addAll(labelGraphic, labelText);

        // Separators
        Text separatorText = new Text("•");
        separatorText.getStyleClass().add("textual-view-stop-panel-label");
        Text hourSeparatorText = new Text("→");
        hourSeparatorText.getStyleClass().add("textual-view-stop-panel-label");

        // Hours of departure / arrival
        if (type == StopType.WAREHOUSE) {
            labelPanel.getChildren().addAll(separatorText);
            if (editable) {
                // Departure hour of warehouse
                TimeTextField departureHourInput = new TimeTextField(departureTimeString);
                departureHourInput.setFocusTraversable(false);
                departureHourInput.setOnKeyPressed(
                        (event) -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                if (!departureHourInput.getText()
                                        .equals(inputValueTracker)) {
                                    parent.getComponent().requestFocus();
                                }
                            }
                        }
                );
                departureHourInput.focusedProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            if (!newValue) {
                                if (!departureHourInput.getText()
                                        .equals(inputValueTracker)) {
                                    int hour = departureHourInput
                                            .hoursProperty().getValue();
                                    int minute = departureHourInput
                                            .minutesProperty().getValue();
                                    LocalTime newDepartureTime =
                                            LocalTime.of(hour, minute);
                                    parent.getWindow().getController()
                                        .changeWarehouseDepartureTime(newDepartureTime);
                                }
                            } else {
                                inputValueTracker = departureHourInput.getText();
                            }
                        }
                );
                departureHourInput.setPrefWidth(60);
                departureHourInput.getStyleClass().add("textual-view-stop-panel-hour");
                // Arrival hour back at warehouse
                TimeTextField arrivalHourInput = new TimeTextField(arrivalTimeString);
                arrivalHourInput.setFocusTraversable(false);
                arrivalHourInput.setEditable(false);
                arrivalHourInput.setMouseTransparent(true);
                arrivalHourInput.setPrefWidth(60);
                arrivalHourInput.getStyleClass().add("textual-view-stop-panel-hour");
                arrivalHourInput.getStyleClass().add("uneditable-textfield");
                // Adding them together
                labelPanel.getChildren().addAll(
                        departureHourInput,
                        hourSeparatorText,
                        arrivalHourInput
                );
            } else {
                LocalTime warehouseDepartureTime =
                        parent.getWindow().getTourData().getDepartureTime();
                String warehouseDepartureTimeString = "00:00";
                if (warehouseDepartureTime != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    warehouseDepartureTimeString =
                            formatter.format(warehouseDepartureTime);
                }
                TimeTextField departureHourInput =
                        new TimeTextField(warehouseDepartureTimeString);
                departureHourInput.setFocusTraversable(false);
                departureHourInput.setEditable(false);
                departureHourInput.setMouseTransparent(true);
                departureHourInput.setPrefWidth(60);
                departureHourInput.getStyleClass().add("textual-view-stop-panel-hour");
                // Adding them together
                labelPanel.getChildren().addAll(departureHourInput);
            }
        } else {
            // Arrival hour at stop
            if (editable) {
                labelPanel.getChildren().addAll(separatorText);
                Text arrivalHourText = new Text(arrivalTimeString);
                arrivalHourText.getStyleClass().add("textual-view-stop-panel-hour");
                labelPanel.getChildren().addAll(arrivalHourText);
            }
        }
        labelPanel.setAlignment(Pos.CENTER_LEFT);
        contentPane.setTop(labelPanel);

        // Position
        Text posText = new Text("Position (lat/lon):");
        TextField posInput = new TextField("(" + lat + "; " + lon + ")");
        posInput.setEditable(false);
        posInput.setMouseTransparent(true);
        posInput.setFocusTraversable(false);
        posInput.getStyleClass().add("uneditable-textfield");
        infoPane.add(posText, 0, 0);
        infoPane.add(posInput, 1, 0);

        // Duration
        if (type != StopType.WAREHOUSE) {
            Text durationText = new Text("Duration (sec):");
            TextField durationInput = new TextField((int) duration + "");
            durationInput.setFocusTraversable(false);
            if (editable) {
                durationInput.textProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            // Replace with only numberical numbers
                            if (!newValue.matches("\\d*")) {
                                durationInput.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        }
                );
                durationInput.setOnKeyPressed(
                        (event) -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                if (!durationInput.getText().equals(inputValueTracker)) {
                                    parent.getComponent().requestFocus();
                                }
                            }
                        }
                );
                durationInput.focusedProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            if (!newValue) {
                                if (!durationInput.getText().equals(inputValueTracker)) {
                                    int durationValue =
                                            Integer.parseInt(durationInput.getText());
                                    parent.getWindow().getController()
                                            .changeStopDuration(stop, durationValue);
                                }
                            } else {
                                inputValueTracker = durationInput.getText();
                            }
                        }
                );
            } else {
                durationInput.setEditable(false);
                durationInput.setMouseTransparent(true);
            }
            infoPane.add(durationText, 0, 1);
            infoPane.add(durationInput, 1, 1);
        }
        contentPane.setCenter(infoPane);
        panel.setLeft(contentPane);

        if (editable && type != StopType.WAREHOUSE) {

            VBox controls = new VBox(6);
            // Delete button
            Button deleteButton = new Button();
            deleteButton.setGraphic(new ImageView(ViewUtilities.DELETE_ICON));
            deleteButton.getStyleClass().add("control-button");
            deleteButton.setOnMouseClicked(
                e -> parent.getWindow().getController().deleteRequest(stop.getRequest())
            );
            // Arrow up
            Button upButton = new Button();
            upButton.setGraphic(new ImageView(ViewUtilities.UP_ARROW_ICON));
            upButton.getStyleClass().add("control-button");
            upButton.setOnMouseClicked(
                e -> parent.getWindow().getController().shiftStopOrderUp(stop)
            );
            boolean upEnabled = !parent.getWindow().getTourData()
                    .stopIsShiftable(stop, -1);
            upButton.setDisable(upEnabled);
            // Arrow down
            Button downButton = new Button();
            downButton.setGraphic(new ImageView(ViewUtilities.DOWN_ARROW_ICON));
            downButton.getStyleClass().add("control-button");
            downButton.setOnMouseClicked(
                e -> parent.getWindow().getController().shiftStopOrderDown(stop)
            );
            boolean dbEnabled = !parent.getWindow().getTourData()
                    .stopIsShiftable(stop, +1);
            downButton.setDisable(dbEnabled);
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
            new BorderWidths(3)
        )));
        this.setOnMouseClicked(
            e -> parent.getWindow().highlightStop(stop, this)
        );

        TextualViewStop oldTextualViewStop = null;
        HashMap<Stop, Node[]> graphicalStopsMap =
                parent.getWindow().getGraphicalStopsMap();
        if (graphicalStopsMap.containsKey(stop)) {
            oldTextualViewStop = (TextualViewStop) graphicalStopsMap.get(stop)[1];
            graphicalStopsMap.get(stop)[1] = this;
        } else {
            graphicalStopsMap.put(stop, new Node[] {null, this});
        }

        if (oldTextualViewStop != null) {
            setHighlight(oldTextualViewStop.getHighlightLevel());
        } else {
            setHighlight(0);
        }

    }

    /**
     * Getter for attribute highlightLevel.
     * @return highlightLevel
     */
    public int getHighlightLevel() {
        return highlightLevel;
    }

    /**
     * Highlights or un-highlights the graphical object.
     * @param highlightLevel The level of highlight
     *                       (2 = primary; 1 = secondary; 0 = none).
     */
    public void setHighlight(int highlightLevel) {
        this.highlightLevel = highlightLevel;
        labelGraphic.setHighlight(highlightLevel);
        if (highlightLevel > 0) {
            this.setBorder(new Border(new BorderStroke(
                    ViewUtilities.COLOURS.get("ORANGE"),
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(10),
                    new BorderWidths(3)
            )));
            if (highlightLevel > 1) {
                ViewUtilities.ensureVisible(scrollPane, this);
            }
        } else {
            this.setBorder(new Border(new BorderStroke(
                    Color.TRANSPARENT,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(10),
                    new BorderWidths(3)
            )));
        }
    }

}
