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

public class GraphicalViewSegment extends Line {

    public GraphicalViewSegment(Segment segment, double startX, double startY, double endX, double endY, double strokeWidth, Color strokeColour, TextField displayer) {
        super(startX, startY, endX, endY);
        this.setStrokeWidth(strokeWidth);
        this.setStroke(strokeColour);
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
    }

}
