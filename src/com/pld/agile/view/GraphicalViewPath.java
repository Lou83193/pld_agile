/*
 * GraphicalViewPath
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.utils.view.ViewUtilities;
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
     * @param strokeWidth The path's line thickness.
     * @param finished Whether the path is final or an intermediary result.
     */
    public GraphicalViewPath(final GraphicalView graphicalView,
                             final Path path,
                             final double strokeWidth,
                             final boolean finished) {
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
        final Color traceColour1;
        final Color traceColour2;
        if (finished) {
            traceColour1 = ViewUtilities.DARK_ORANGE;
            traceColour2 = ViewUtilities.BLUE;
        }
        else {
            traceColour1 = ViewUtilities.PURPLE;
            traceColour2 = ViewUtilities.YELLOW;
        }
        this.setStrokeWidth(strokeWidth);
        this.setStroke(traceColour1);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setStrokeLineJoin(StrokeLineJoin.ROUND);
        this.addEventHandler(MouseEvent.MOUSE_ENTERED,
            e -> {
                this.setStroke(traceColour2);
                this.toFront();
            }
        );
        this.addEventHandler(MouseEvent.MOUSE_EXITED,
            e -> {
                this.setStroke(traceColour1);
            }
        );
    }

}
