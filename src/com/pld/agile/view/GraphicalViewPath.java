/*
 * GraphicalViewPath
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.view;

import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.Path;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Graphical object representing a Path in the graphical view.
 * A Path is represented (in the graphical view) by a thick polyline.
 */
public class GraphicalViewPath extends Polyline {

    /**
     * The parent GraphicalView instance
     */
    private GraphicalView graphicalView;
    /**
     * The primary colour of the path.
     */
    private Color colour1;
    /**
     * The secondary colour of the path (used for highlights)
     */
    private Color colour2;

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
        this.graphicalView = graphicalView;
        int count = 0;
        List<Segment> pathSegments = path.getSegments();
        for (Segment segment : pathSegments) {
            if (segment == null) {
                continue;
            }
            if (count == 0) {
                double[] originPos = graphicalView.projectLatLon(segment.getOrigin());
                this.getPoints().addAll(originPos[0], originPos[1]);
            }
            double[] destinationPos = graphicalView.projectLatLon(segment.getDestination());
            this.getPoints().addAll(destinationPos[0], destinationPos[1]);
            count++;
        }
        if (finished) {
            colour1 = ViewUtilities.COLOURS.get("DARK_ORANGE");
            colour2 = ViewUtilities.COLOURS.get("BLUE");
        } else {
            colour1 = ViewUtilities.COLOURS.get("PURPLE");
            colour2 = ViewUtilities.COLOURS.get("YELLOW");
        }
        this.setStrokeWidth(strokeWidth);
        this.setStroke(colour1);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setStrokeLineJoin(StrokeLineJoin.ROUND);
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> highlight());
        this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> unhighlight());

    }

    /**
     * Highlights the path graphically
     */
    public void highlight() {
        this.setStroke(colour2);
        GraphicalViewTourLayer graphicalViewTourLayer = graphicalView.getGraphicalViewTourLayer();
        List<GraphicalViewPath> otherGraphicalViewPaths = new ArrayList<>();
        for (Node node : graphicalViewTourLayer.getChildren()) {
            if (node instanceof GraphicalViewPath) {
                GraphicalViewPath graphicalViewPath = (GraphicalViewPath) node;
                if (!graphicalViewPath.equals(this)) {
                    otherGraphicalViewPaths.add(graphicalViewPath);
                }
            }
        }
        for (GraphicalViewPath graphicalViewPath : otherGraphicalViewPaths) {
            graphicalViewPath.toBack();
        }
    }

    /**
     * Unhighlights the path graphically
     */
    public void unhighlight() {
        this.setStroke(colour1);
    }

}
