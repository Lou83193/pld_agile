package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.tsp.Graph;
import com.pld.agile.utils.view.ViewUtilities;
import com.pld.agile.utils.view.ZoomableScrollPane;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.util.List;

public class GraphicalViewMapLayer extends Pane {

    private MapData mapData;
    private TourData tourData;
    private Window window;

    public GraphicalViewMapLayer(MapData mapData, TourData tourData, Window window) {
        this.mapData = mapData;
        this.tourData = tourData;
        this.window = window;
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void draw() {
        drawMap();
        drawTour();
    }

    public void drawMap() {

        double screenScale = ViewUtilities.mapValue(getHeight(), 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        this.getChildren().clear();

        List<Segment> segments = mapData.getSegments();

        for (Segment s : segments) {

            double[] originPos = projectLatLon(s.getOrigin());
            double[] destinationPos = projectLatLon(s.getDestination());
            Line graphicalViewSegment = new GraphicalViewSegment(
                s,
                originPos[0],
                originPos[1],
                destinationPos[0],
                destinationPos[1],
                2 * screenScale * mapScale,
                Color.web("#545454"),
                window.getStreetNameLabel()
            );
            this.getChildren().add(graphicalViewSegment);

        }

    }

    public void drawTour() {

        double screenScale = ViewUtilities.mapValue(getHeight(), 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        List<Integer> stops = tourData.getStops();
        List<Integer> computedPath = tourData.getComputedPath();
        int[][] predecessors = tourData.getPredecessors();
        Graph graph = tourData.getStopsGraph();

        if (computedPath == null) {
            return;
        }

        int pathLength = computedPath.size();
        for (int i = 0; i < pathLength; i++) {

            // Get current and next stop
            Integer currStopId = computedPath.get(i);
            Integer nextStopId = computedPath.get((i + 1) % pathLength);

            Intersection nextStop = mapData.getIntersections().get(stops.get(nextStopId));
            double[] nextStopPos = projectLatLon(nextStop);
            Line line;

            // Trace first line
            int predecessor = predecessors[currStopId][stops.get(nextStopId)];
            Intersection currIntersection = mapData.getIntersections().get(predecessor);
            double[] currIntersectionPos = projectLatLon(currIntersection);
            line = new Line(nextStopPos[0], nextStopPos[1], currIntersectionPos[0], currIntersectionPos[1]);
            line.setStrokeWidth(4 * screenScale * mapScale); line.setStroke(Color.web("#ED6A08")); line.setStrokeLineCap(StrokeLineCap.ROUND);
            line.setMouseTransparent(true);
            this.getChildren().add(line);

            // Get intermediary intersections, trace lines
            while (predecessor != stops.get(currStopId)) {

                predecessor = predecessors[currStopId][predecessor];
                Intersection nextIntersection = mapData.getIntersections().get(predecessor);
                double[] nextIntersectionPos = projectLatLon(nextIntersection);
                line = new Line(currIntersectionPos[0], currIntersectionPos[1], nextIntersectionPos[0], nextIntersectionPos[1]);
                line.setStrokeWidth(4 * screenScale * mapScale); line.setStroke(Color.web("#ED6A08")); line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setMouseTransparent(true);
                this.getChildren().add(line);
                currIntersectionPos = nextIntersectionPos;

            }

        }

    }

    private double[] projectLatLon(Intersection intersection) {
        return ViewUtilities.projectMercatorLatLon(
            intersection.getLatitude(),
            intersection.getLongitude(),
            mapData.getMinLat(),
            mapData.getMinLon(),
            mapData.getMaxLat(),
            mapData.getMaxLon(),
            getHeight()
        );
    }

    @Override
    public boolean isResizable() {
        return true;
    }

}
