/*
 * GraphicalViewSegment
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.Segment;
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
     * @param segment The associated Segment model object.
     * @param startX The segment's starting position's X.
     * @param startY The segment's starting position's Y.
     * @param endX The segment's ending position's X.
     * @param endY The segment's ending position's Y.
     * @param strokeWidth The segment's line thickness.
     * @param strokeColour The segment's line colour.
     * @param displayer TextField in which to display the segment's street name
     *                  (when hovered).
     */
    public GraphicalViewSegment(final Segment segment,
                                final double startX,
                                final double startY,
                                final double endX,
                                final double endY,
                                final double strokeWidth,
                                final Color strokeColour,
                                final TextField displayer) {

        super(startX, startY, endX, endY);
        this.setStrokeWidth(strokeWidth);
        this.setStroke(strokeColour);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        if (displayer != null) {
            this.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> {
                        displayer.setText(segment.getName());
                        this.setStroke(strokeColour.brighter());
                    }
            );
            this.addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> {
                        displayer.setText("");
                        this.setStroke(strokeColour);
                    }
            );
        } else {
            this.setMouseTransparent(true);
        }
    }

}
