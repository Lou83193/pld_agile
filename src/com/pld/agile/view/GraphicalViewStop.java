/*
 * GraphicalViewStop
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GraphicalViewStop extends Pane {

    private double pointerCenterX;
    private double pointerCenterY;
    private double pointerH;
    private Stop stop;

    public GraphicalViewStop(final Stop stop, final double size, final int num) {

        this.stop = stop;
        StopType type = stop.getType();
        Color fillColour = Color.BLACK;
        Color outlineColour = Color.BLACK;

        Shape symbol = new Circle();

        double graphicSize = size;
        pointerCenterX = size / 2;
        pointerCenterY = size / 2;
        pointerH = size;

        double pointerX = graphicSize / 2;
        double pointerY = graphicSize / 2;

        switch (type) {

            case PICKUP:
                fillColour = ViewUtilities.stringToColour(stop.getRequest().getPickup().getAddress().toString());
                symbol = new Circle(graphicSize / 2);
                symbol.setTranslateX(graphicSize / 2);
                symbol.setTranslateY(graphicSize / 2);
                double radius = graphicSize / 2;
                double theta = Math.acos(radius / pointerH);
                pointerX = radius * Math.sin(theta);
                pointerY = radius * Math.cos(theta);
                break;

            case DELIVERY:
                fillColour = ViewUtilities.stringToColour(stop.getRequest().getPickup().getAddress().toString());
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

       Shape stopGraphic = Shape.union(symbol, pointer);
        stopGraphic.setFill(fillColour);
        stopGraphic.setStroke(outlineColour);
        stopGraphic.setStrokeWidth(graphicSize / 10);
        stopGraphic.setStrokeLineJoin(StrokeLineJoin.ROUND);

        this.getChildren().add(stopGraphic);

        if (num > 0) {
            int fontSize = (int) (graphicSize * 0.7);
            Text numText = new Text(num + "");
            numText.setFont(Font.font("Segoe UI", FontWeight.BOLD, fontSize));
            numText.setFill(Color.BLACK);
            numText.setTextAlignment(TextAlignment.CENTER);
            numText.relocate(
                pointerCenterX - numText.getBoundsInLocal().getWidth() / 2,
                pointerCenterY - numText.getBoundsInLocal().getHeight() / 2
            );
            this.getChildren().add(numText);
        }

    }

    public void place(double[] pos) {
        relocate(
            pos[0] - pointerCenterX,
            pos[1] - pointerCenterY - pointerH
        );
    }

}
