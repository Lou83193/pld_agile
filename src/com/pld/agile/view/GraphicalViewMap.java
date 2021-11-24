package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.tsp.CompleteGraph;
import com.pld.agile.utils.tsp.Graph;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.List;

public class GraphicalViewMap extends Canvas {

    private MapData mapData;
    private TourData tourData;

    public GraphicalViewMap(MapData mapData, TourData tourData, Scene parent) {
        this.mapData = mapData;
        this.tourData = tourData;
        //BorderPane root = (BorderPane)parent.getRoot(); MenuBar menuBar = (MenuBar)root.getTop();
        widthProperty().bind(parent.heightProperty().subtract(25));
        heightProperty().bind(parent.heightProperty().subtract(25));
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void draw() {
        drawMap();
        drawTour();
    }

    public void drawMap() {

        double width = getWidth();
        double height = getHeight();
        double screenScale = ViewUtilities.mapValue(height, 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.web("#DEDEDE"));
        gc.fillRect(0, 0, width, height);

        gc.setLineWidth(2 * screenScale * mapScale);
        gc.setStroke(Color.web("#454545"));

        List<Segment> segments = mapData.getSegments();

        for (Segment s : segments) {

            Intersection origin = s.getOrigin();
            double[] originPos = projectLatLon(origin);

            Intersection destination = s.getDestination();
            double[] destinationPos = projectLatLon(destination);

            gc.strokeLine(originPos[0], originPos[1], destinationPos[0], destinationPos[1]);

        }

    }

    public void drawTour() {

        double screenScale = ViewUtilities.mapValue(getHeight(), 0, 720, 0, 1);
        double mapScale = ViewUtilities.mapValue(mapData.getMaxLon() - mapData.getMinLon(), 0.02235, 0.07610, 1.25, 0.75);

        GraphicsContext gc = getGraphicsContext2D();
        gc.setLineWidth(4 * screenScale * mapScale);
        gc.setStroke(Color.web("#ED6A08"));

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

            // Get length of path
            double length = graph.getCost(currStopId, nextStopId);
            double cumulatedLength = 0;
            boolean hasDrawnArrow = false;

            // Trace first line
            int predecessor = predecessors[currStopId][stops.get(nextStopId)];
            Intersection currIntersection = mapData.getIntersections().get(predecessor);
            double[] currIntersectionPos = projectLatLon(currIntersection);
            gc.strokeLine(nextStopPos[0], nextStopPos[1], currIntersectionPos[0], currIntersectionPos[1]);
            cumulatedLength += ViewUtilities.distance(nextStopPos, currIntersectionPos);

            // Get intermediary intersections, trace lines
            while (predecessor != stops.get(currStopId)) {

                predecessor = predecessors[currStopId][predecessor];
                Intersection nextIntersection = mapData.getIntersections().get(predecessor);
                double[] nextIntersectionPos = projectLatLon(nextIntersection);
                gc.strokeLine(currIntersectionPos[0], currIntersectionPos[1], nextIntersectionPos[0], nextIntersectionPos[1]);
                double currLength = ViewUtilities.distance(currIntersectionPos, nextIntersectionPos);
                cumulatedLength += currLength;

                // Trace arrow in middle
                /*
                if (cumulatedLength >= length/12 && currLength > 10 && !hasDrawnArrow) {
                    hasDrawnArrow = true;
                    double dir = ViewUtilities.direction(currIntersectionPos, nextIntersectionPos)*180/Math.PI;
                    System.out.println(currLength);
                    double[] arrowPointsX = new double[3];
                    double[] arrowPointsY = new double[3];
                    arrowPointsX[1] = (currIntersectionPos[0] + nextIntersectionPos[0])/2;
                    arrowPointsY[1] = (currIntersectionPos[1] + nextIntersectionPos[1])/2;
                    arrowPointsX[0] = arrowPointsX[1] + Math.cos(dir-15)*10;
                    arrowPointsY[0] = arrowPointsY[1] - Math.sin(dir-15)*10;
                    arrowPointsX[2] = arrowPointsX[1] + Math.cos(dir+15)*10;
                    arrowPointsY[2] = arrowPointsY[1] - Math.sin(dir+15)*10;
                    gc.strokePolyline(arrowPointsX, arrowPointsY, 3);
                }
                */

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
