/*
 * GraphicalViewPath
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.List;

public class GraphicalViewPath extends Polyline {

    /**
     * GraphicalViewPath constructor.
     * @param graphicalView The parent GraphicalView instance.
     * @param path The associated Path model object.
     * @param strokeWidth The segment's line thickness.
     * @param strokeColour The segment's line colour.
     */
    public GraphicalViewPath(final GraphicalView graphicalView,
                             final Path path,
                             final double strokeWidth,
                             final Color strokeColour) {
        super();
        int count = 0;
        List<Segment> pathSegments = path.getSegments();
        for (Segment segment : pathSegments) {
            if (count == 0) {
                double[] originPos = graphicalView.projectLatLon(segment.getOrigin());
                this.getPoints().addAll(originPos[0], originPos[1]);
            }
            double[] destinationPos = graphicalView.projectLatLon(segment.getDestination());
            this.getPoints().addAll(destinationPos[0], destinationPos[1]);
            count++;
        }
        this.setStrokeWidth(strokeWidth);
        this.setStroke(strokeColour);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setStrokeLineJoin(StrokeLineJoin.ROUND);
        this.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    this.setStroke(strokeColour.deriveColor(180, 1, 1, 1));
                }
        );
        this.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    this.setStroke(strokeColour);
                }
        );
    }

}
