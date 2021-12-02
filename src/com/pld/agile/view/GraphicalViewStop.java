/*
 * GraphicalViewStop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.utils.observer.Observable;
import com.pld.agile.utils.observer.Observer;
import com.pld.agile.utils.observer.UpdateType;
import com.pld.agile.utils.view.MouseClickNotDragDetector;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Cursor;
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
public class GraphicalViewStop extends Pane implements Observer {

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

    /**
     * TextualViewStop constructor.
     * Populates the graphical object.
     * @param stop The corresponding Stop model object.
     * @param parent The GraphicalView instance containing the stop
     * @param graphicSize The size of the pointer.
     * @param num The order number of the stop.
     */
    public GraphicalViewStop(Stop stop, GraphicalView parent, double graphicSize, int num, boolean editable) {

        stop.addObserver(this);

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
            this.setOnMousePressed((t) -> {
                parent.getWindow().getController().clickOnGraphicalStop(stop);
            });

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
                this.setOnMouseDragExited((t) -> {
                    double[] latLonPos = ViewUtilities.projectMercatorLatLonInv(
                            t.getX() + pointerCenterX,
                            t.getY() + pointerCenterY + pointerH,
                            parent.getMapData().getMinLat(),
                            parent.getMapData().getMaxLat(),
                            parent.getMapData().getMinLon(),
                            parent.getMapData().getMaxLon(),
                            ((ScrollPane) parent.getComponent()).getHeight()
                    );
                    parent.getWindow().getController().dragOffGraphicalStop(latLonPos);
                    t.consume();
                });

            }

        }

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

    @Override
    public void update(Observable observed, UpdateType updateType) {

        switch (updateType) {
            case STOP_HIGHLIGHT -> {
                Stop stop = (Stop)observed;
                if (stop.getHighlighted() > 0) {
                    DropShadow shadow = new DropShadow();
                    shadow.setColor(ViewUtilities.mixColours(ViewUtilities.ORANGE, Color.WHITE, 0.6));
                    shadow.setRadius(5);
                    this.setEffect(shadow);
                    stopGraphic.setFill(Color.WHITE);
                    stopGraphic.setStroke(ViewUtilities.ORANGE);
                    if (stop.getHighlighted() > 1) {
                        highlightPointerGraphic.setFill(ViewUtilities.ORANGE);
                        if (numText != null) {
                            numText.setFill(ViewUtilities.ORANGE);
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

    }
}
