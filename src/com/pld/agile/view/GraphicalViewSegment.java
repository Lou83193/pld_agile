/*
 * GraphicalViewSegment
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * Graphical object representing a Segment in the graphical view.
 * A Segment is represented (in the graphical view) by a line.
 */
public class GraphicalViewSegment extends Line {

    /**
     * GraphicalViewSegment constructor.
     * @param graphicalView The parent GraphicalView instance.
     * @param segment The associated Segment model object.
     * @param strokeWidth The segment's line thickness.
     * @param associatedPath The path the segmen's associated to (if any).
     *                       A non-null path will make the segment invisible
     * @param displayer TextField in which to display the segment's street name
     *                  (when hovered).
     */
    public GraphicalViewSegment(final GraphicalView graphicalView,
                                final Segment segment,
                                final double strokeWidth,
                                final GraphicalViewPath associatedPath,
                                final TextField displayer) {
        super();
        boolean isVisible = (associatedPath == null);
        Color color = (isVisible) ? ViewUtilities.COLOURS.get("GREY") : Color.TRANSPARENT;
        double[] originPos = graphicalView.projectLatLon(segment.getOrigin());
        double[] destinationPos = graphicalView.projectLatLon(segment.getDestination());
        this.setStartX(originPos[0]);
        this.setStartY(originPos[1]);
        this.setEndX(destinationPos[0]);
        this.setEndY(destinationPos[1]);
        this.setStrokeWidth(strokeWidth);
        this.setStroke(color);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        if (displayer != null) {
            this.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    displayer.setText(segment.getName());
                    this.setStroke(color.brighter());
                    if (associatedPath != null) {
                        associatedPath.highlight();
                    }
                    e.consume();
                }
            );
            this.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    displayer.setText("");
                    this.setStroke(color);
                    if (associatedPath != null) {
                        associatedPath.unhighlight();
                    }
                    e.consume();
                }
            );
        }
    }

}
