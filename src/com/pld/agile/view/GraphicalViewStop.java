/*
 * GraphicalViewStop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
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
    /**
     * The fill colour of the graphical stop.
     */
    private Color fillColour;
    /**
     * The outline colour of the graphical stop.
     */
    private Color outlineColour;
    /**
     * The shape of the graphical stop.
     */
    private Shape stopGraphic;
    /**
     * The shape of the highlight pointer on the graphical stop.
     */
    private Shape highlightPointerGraphic;
    /**
     * The text displaying the order number of the graphical stop.
     */
    private Text numText;

    private boolean isDragged;

    /**
     * The highlight level of the graphical stop.
     * 2 = highlighted by selection.
     * 1 = highlighted by association.
     * 0 = not highlighted.
     */
    private int highlightLevel;

    /**
     * TextualViewStop constructor.
     * Populates the graphical object.
     * @param stop The corresponding Stop model object.
     * @param parent The GraphicalView instance containing the stop.
     * @param graphicSize The size of the pointer.
     * @param editable Whether the stop is draggable or not.
     */
    public GraphicalViewStop(Stop stop, GraphicalView parent, double graphicSize, boolean editable) {

        isDragged = false;
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
                fillColour = ViewUtilities.getRandomColour(stop.getRequest().getId());
                symbol = new Circle(graphicSize / 2);
                symbol.setTranslateX(graphicSize / 2);
                symbol.setTranslateY(graphicSize / 2);
                double radius = graphicSize / 2;
                double theta = Math.acos(radius / pointerH);
                pointerX = radius * Math.sin(theta);
                pointerY = radius * Math.cos(theta);
                break;

            case DELIVERY:
                fillColour = ViewUtilities.getRandomColour(stop.getRequest().getId());
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

        int num = stop.getStopNumber();
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

            this.setOnMousePressed(
                (e) -> {
                    Window w = parent.getWindow();
                    w.unhighlightStops();
                    if (stop.getRequest() != null) {
                        Stop pickup = stop.getRequest().getPickup();
                        Stop delivery = stop.getRequest().getDelivery();
                        if (pickup == null || delivery == null) {
                            return;
                        }
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
                this.setOnDragDetected((t) -> {
                    isDragged = true;
                    this.toFront();
                    parent.getWindow().getController().dragOnGraphicalStop();
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
                this.setOnMouseReleased((t) -> {
                    if (isDragged) {
                        int offset = 7;
                        double[] latLonPos = ViewUtilities.projectMercatorLatLonInv(
                                this.getBoundsInParent().getMinX() + offset + pointerCenterX,
                                this.getBoundsInParent().getMinY() + offset + pointerCenterY + pointerH,
                                mapData.getMinLat(),
                                mapData.getMinLon(),
                                mapData.getMaxLat(),
                                mapData.getMaxLon(),
                                ((ScrollPane) parent.getComponent()).getHeight()
                        );
                        parent.getWindow().getController().dragOffGraphicalStop(stop, latLonPos);
                        isDragged = false;
                        t.consume();
                    }
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

    /**
     * Getter for attribute highlightLevel.
     * @return highlightLevel
     */
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
     * @param highlightLevel The level of highlight.
     */
    public void setHighlight(int highlightLevel) {
        this.highlightLevel = highlightLevel;
        if (highlightLevel > 0) {
            DropShadow shadow = new DropShadow();
            shadow.setColor(ViewUtilities.mixColours(ViewUtilities.COLOURS.get("ORANGE"), Color.WHITE, 0.6));
            shadow.setRadius(5);
            this.setEffect(shadow);
            stopGraphic.setFill(Color.WHITE);
            stopGraphic.setStroke(ViewUtilities.COLOURS.get("ORANGE"));
            if (highlightLevel > 1) {
                highlightPointerGraphic.setFill(ViewUtilities.COLOURS.get("ORANGE"));
                if (numText != null) {
                    numText.setFill(ViewUtilities.COLOURS.get("DARK_ORANGE"));
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
