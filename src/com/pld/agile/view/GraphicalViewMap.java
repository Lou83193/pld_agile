package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class GraphicalViewMap extends Canvas {

    private MapData mapData;
    private TourData tourData;

    public GraphicalViewMap(MapData mapData, TourData tourData, Scene parent) {
        this.mapData = mapData;
        this.tourData = tourData;
        widthProperty().bind(parent.heightProperty());
        heightProperty().bind(parent.heightProperty());
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void draw() {

        double width = getWidth();
        double height = getHeight();
        double screenScale = ViewUtilities.mapValue(height, 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.web("#DEDEDE"));
        gc.fillRect(0, 0, width, height);

        gc.setLineWidth(2*screenScale*mapScale);
        gc.setStroke(Color.web("#454545"));

        List<Segment> segments = mapData.getSegments();

        for (Segment s : segments) {

            Intersection origin = s.getOrigin();
            double[] originPos = projectLatLon(origin);

            Intersection destination = s.getDestination();
            double[] destinationPos = projectLatLon(destination);

            gc.strokeLine(originPos[0], originPos[1], destinationPos[0], destinationPos[1]);

        }

        drawTour();

    }

    public void drawTour() {

        double screenScale = ViewUtilities.mapValue(getHeight(), 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        GraphicsContext gc = getGraphicsContext2D();
        gc.setLineWidth(4*screenScale*mapScale);
        gc.setStroke(Color.web("#ED6A08"));

        List<Integer> stops = tourData.getStops();
        List<Integer> computedPath = tourData.getComputedPath();
        int[][] predecessors = tourData.getPredecessors();

        if (computedPath == null) return;

        int pathLength = computedPath.size();
        for (int i = 0; i < pathLength; i++) {

            // Get current and next stop
            Integer currStopId = computedPath.get(i);
            Integer nextStopId = computedPath.get((i+1)%pathLength);

            // Trace first line
            int predecessor = predecessors[currStopId][stops.get(nextStopId)];
            Intersection lastIntersection = mapData.getIntersections().get(stops.get(nextStopId));
            double[] lastIntersectionPos = projectLatLon(lastIntersection);
            Intersection currIntersection = mapData.getIntersections().get(predecessor);
            double[] currIntersectionPos = projectLatLon(currIntersection);
            gc.strokeLine(lastIntersectionPos[0], lastIntersectionPos[1], currIntersectionPos[0], currIntersectionPos[1]);

            // Get intermediary intersections, trace lines
            while (predecessor != stops.get(currStopId)) {
                predecessor = predecessors[currStopId][predecessor];
                Intersection nextIntersection = mapData.getIntersections().get(predecessor);
                double[] nextIntersectionPos = projectLatLon(nextIntersection);
                gc.strokeLine(currIntersectionPos[0], currIntersectionPos[1], nextIntersectionPos[0], nextIntersectionPos[1]);
                currIntersectionPos = nextIntersectionPos;
            }

        }


    }

    private double[] projectLatLon(Intersection intersection) {
        return ViewUtilities.projectLatLon(
            intersection.getLatitude(),
            intersection.getLongitude(),
            mapData.getMinLat(),
            mapData.getMinLon(),
            mapData.getMaxLat(),
            mapData.getMaxLon(),
            getWidth(),
            getHeight()
        );
    }

    @Override
    public boolean isResizable() {
        return true;
    }

}
