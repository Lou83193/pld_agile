/*
 * GraphicalViewStop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GraphicalViewStop extends VBox {

    public GraphicalViewStop(final Stop stop, double graphicSize) {

        StopType type = stop.getType();
        Shape stopGraphic = new Circle(graphicSize / 2);
        Color fillColour = Color.BLACK;
        Color outlineColour = Color.BLACK;

        switch (type) {

            case PICKUP:
                fillColour = ViewUtilities.stringToColour(stop.getRequest().getPickup().getAddress().toString());
                stopGraphic = new Circle(graphicSize / 2);
                break;

            case DELIVERY:
                fillColour = ViewUtilities.stringToColour(stop.getRequest().getPickup().getAddress().toString());
                stopGraphic = new Rectangle(graphicSize, graphicSize);
                break;

            case WAREHOUSE:
                outlineColour = Color.WHITE;
                stopGraphic = new Rectangle(graphicSize * 0.8, graphicSize * 0.8);
                stopGraphic.setRotate(45);

        }

        stopGraphic.setFill(fillColour);
        stopGraphic.setStroke(outlineColour);
        stopGraphic.setStrokeWidth(graphicSize / 10);

        this.getChildren().add(stopGraphic);

    }

}
