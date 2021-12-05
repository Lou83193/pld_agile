/*
 * GraphicalViewStop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.MouseClickNotDragDetector;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;

/**
 * Graphical object representing a Stop in the graphical view.
 * A Stop is represented (in the graphical view) by an icon with a shape
 * pinpointing to the Stop's position on the map. It can also contain its order
 * number within the pointer.
 * <p>
 * Pickups Stops are represented by circle pointers.
 * Delivery Stops are represented by square pointers.
 * Warehouse Stops are represented by diamond pointers.
 */
public class GraphicalViewStop extends Pane {

    /**
     * The center of the shape's X coordinate.
     */
    private double pointerCenterX;
    /**
     * The center of the shape's Y coordinate.
     */
    private double pointerCenterY;
    /**
     * The pointer's height.
     */
    private double pointerH;

    private Color fillColour;
    private Color outlineColour;
    private Shape stopGraphic;
    private Shape highlightPointerGraphic;
    private Text numText;
    private int highlightLevel;

    /**
     * TextualViewStop constructor.
     * Populates the graphical object.
     * @param stop The corresponding Stop model object.
     * @param parent The GraphicalView instance containing the stop
     * @param graphicSize The size of the pointer.
     * @param num The order number of the stop.
     */
    public GraphicalViewStop(Stop stop, GraphicalView parent, double graphicSize, int num, boolean editable) {

        fillColour = Color.BLACK;
        outlineColour = Color.BLACK;

        Shape symbol = new Circle();

        pointerCenterX = graphicSize / 2;
        pointerCenterY = graphicSize / 2;
        pointerH = graphicSize;

        double pointerX = graphicSize / 2;
        double pointerY = graphicSize / 2;

        switch (stop.getType()) {

            case PICKUP:
                fillColour = ViewUtilities.stringToColour(
                    stop.getRequest().getPickup().getAddress().toString()
                );
                symbol = new Circle(graphicSize / 2);
                symbol.setTranslateX(graphicSize / 2);
                symbol.setTranslateY(graphicSize / 2);
                double radius = graphicSize / 2;
                double theta = Math.acos(radius / pointerH);
                pointerX = radius * Math.sin(theta);
                pointerY = radius * Math.cos(theta);
                break;

            case DELIVERY:
                fillColour = ViewUtilities.stringToColour(
                    stop.getRequest().getPickup().getAddress().toString()
                );
                symbol = new Rectangle(graphicSize, graphicSize);
                break;

            case WAREHOUSE:
                outlineColour = Color.WHITE;
                symbol = new Rectangle(graphicSize, graphicSize);
                symbol.setRotate(45);
                pointerX = graphicSize / Math.sqrt(2);
                pointerY = 0;

        }

        Shape pointer = new Polygon(
            pointerCenterX - pointerX, pointerCenterY + pointerY,
            pointerCenterX + pointerX, pointerCenterY + pointerY,
            pointerCenterX, pointerCenterY + pointerH
        );

        stopGraphic = Shape.union(symbol, pointer);
        stopGraphic.setFill(fillColour);
        stopGraphic.setStroke(outlineColour);
        stopGraphic.setStrokeWidth(graphicSize / 10);
        stopGraphic.setStrokeLineJoin(StrokeLineJoin.ROUND);

        double pointerSize = graphicSize / 4;
        highlightPointerGraphic = new Circle(pointerSize);
        highlightPointerGraphic.setFill(Color.TRANSPARENT);
        highlightPointerGraphic.relocate(
            pointerCenterX - pointerSize,
            pointerCenterY + pointerH - pointerSize
        );

        this.getChildren().addAll(highlightPointerGraphic, stopGraphic);

        if (num > 0) {
            int fontSize = (int) (graphicSize * 0.7);
            numText = new Text(num + "");
            numText.setFont(Font.font("Segoe UI", FontWeight.BOLD, fontSize));
            numText.setFill(Color.BLACK);
            numText.setTextAlignment(TextAlignment.CENTER);
            numText.relocate(
                pointerCenterX - numText.getBoundsInLocal().getWidth() / 2,
                pointerCenterY - numText.getBoundsInLocal().getHeight() / 2
            );
            this.getChildren().add(numText);
        }

        if (parent != null) {

            /*
            MouseClickNotDragDetector.clickNotDragDetectingOn(this)

                    .withPressedDurationThreshold(150)
                    .setOnMouseClickedNotDragged((t) -> {
                        parent.getWindow().getController().clickOnGraphicalStop(stop);
                    });
            */
            this.setOnMousePressed(
                (e) -> {
                    Window w = parent.getWindow();
                    w.unhighlightStops();
                    if (stop.getRequest() != null) {
                        Stop pickup = stop.getRequest().getPickup();
                        Stop delivery = stop.getRequest().getDelivery();
                        GraphicalViewStop pickupGraphicalView = (GraphicalViewStop) w.getGraphicalStopsMap().get(pickup)[0];
                        GraphicalViewStop deliveryGraphicalView = (GraphicalViewStop) w.getGraphicalStopsMap().get(delivery)[0];
                        TextualViewStop pickupTextualView = (TextualViewStop) w.getGraphicalStopsMap().get(pickup)[1];
                        TextualViewStop deliveryTextualView = (TextualViewStop) w.getGraphicalStopsMap().get(delivery)[1];
                        if (this.equals(pickupGraphicalView)) {
                            pickupGraphicalView.setHighlight(2);
                            pickupTextualView.setHighlight(2);
                            deliveryGraphicalView.setHighlight(1);
                            deliveryTextualView.setHighlight(1);
                        } else {
                            pickupGraphicalView.setHighlight(1);
                            pickupTextualView.setHighlight(1);
                            deliveryGraphicalView.setHighlight(2);
                            deliveryTextualView.setHighlight(2);
                        }
                    }
                    else {
                        GraphicalViewStop stopGraphicalView = (GraphicalViewStop) w.getGraphicalStopsMap().get(stop)[0];
                        TextualViewStop stopTextualView = (TextualViewStop) w.getGraphicalStopsMap().get(stop)[1];
                        stopGraphicalView.setHighlight(2);
                        stopTextualView.setHighlight(2);
                    }
                }
            );

            if (editable) {

                this.setCursor(Cursor.HAND);
                this.setOnMouseDragEntered((t) -> {
                    this.toFront();
                    parent.getWindow().getController().dragOnGraphicalStop(stop);
                    t.consume();
                });
                this.setOnMouseDragged((t) -> {
                    double offsetX = t.getX() - pointerCenterX;
                    double offsetY = t.getY() - pointerCenterY;
                    double viewPortSize = ((ScrollPane) parent.getComponent()).getHeight();
                    if (this.getBoundsInParent().getMinX() + offsetX >= -viewPortSize*0.05
                    &&  this.getBoundsInParent().getMaxX() + offsetX <= +viewPortSize*1.05) {
                        this.setTranslateX(this.getTranslateX() + offsetX);
                    }
                    if (this.getBoundsInParent().getMinY() + offsetY >= -viewPortSize*0.05
                    &&  this.getBoundsInParent().getMaxY() + offsetY <= +viewPortSize*1.05) {
                        this.setTranslateY(this.getTranslateY() + offsetY);
                    }
                    t.consume();
                });
                MapData mapData = parent.getWindow().getMapData();
                this.setOnMouseDragExited((t) -> {
                    double[] latLonPos = ViewUtilities.projectMercatorLatLonInv(
                            t.getX() + pointerCenterX,
                            t.getY() + pointerCenterY + pointerH,
                            mapData.getMinLat(),
                            mapData.getMaxLat(),
                            mapData.getMinLon(),
                            mapData.getMaxLon(),
                            ((ScrollPane) parent.getComponent()).getHeight()
                    );
                    parent.getWindow().getController().dragOffGraphicalStop(latLonPos);
                    t.consume();
                });

            }

            GraphicalViewStop oldGraphicalViewStop = null;
            HashMap<Stop, Node[]> graphicalStopsMap = parent.getWindow().getGraphicalStopsMap();
            if (graphicalStopsMap.containsKey(stop)) {
                oldGraphicalViewStop = (GraphicalViewStop) graphicalStopsMap.get(stop)[0];
                graphicalStopsMap.get(stop)[0] = this;
            } else {
                graphicalStopsMap.put(stop, new Node[] {this, null});
            }

            if (oldGraphicalViewStop != null) {
                setHighlight(oldGraphicalViewStop.getHighlightLevel());
            } else {
                setHighlight(0);
            }

        }

    }

    public int getHighlightLevel() {
        return highlightLevel;
    }

    /**
     * Relocates the graphical object to the given position.
     * @param pos The position to relocate the graphical object to.
     */
    public void place(final double[] pos) {
        relocate(
            pos[0] - pointerCenterX,
            pos[1] - pointerCenterY - pointerH
        );
    }

    /**
     * Highlights or un-highlights the graphical object.
     * @param highlightLevel The level of highlight (2 = primary; 1 = secondary; 0 = none).
     */
    public void setHighlight(int highlightLevel) {
        this.highlightLevel = highlightLevel;
        if (highlightLevel > 0) {
            DropShadow shadow = new DropShadow();
            shadow.setColor(ViewUtilities.mixColours(ViewUtilities.ORANGE, Color.WHITE, 0.6));
            shadow.setRadius(5);
            this.setEffect(shadow);
            stopGraphic.setFill(Color.WHITE);
            stopGraphic.setStroke(ViewUtilities.ORANGE);
            if (highlightLevel > 1) {
                highlightPointerGraphic.setFill(ViewUtilities.ORANGE);
                if (numText != null) {
                    numText.setFill(ViewUtilities.DARK_ORANGE);
                }
            }
        } else {
            this.setEffect(null);
            stopGraphic.setFill(fillColour);
            stopGraphic.setStroke(outlineColour);
            highlightPointerGraphic.setFill(Color.TRANSPARENT);
            if (numText != null) {
                numText.setFill(Color.BLACK);
            }
        }
    }

}
