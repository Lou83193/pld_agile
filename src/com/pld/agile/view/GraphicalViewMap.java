package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.map.Segment;
import com.pld.agile.utils.view.ProjectionUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class GraphicalViewMap extends Canvas {

    private MapData mapData;

    public GraphicalViewMap(MapData mapData, Scene parent) {
        this.mapData = mapData;
        widthProperty().bind(parent.heightProperty());
        heightProperty().bind(parent.heightProperty());
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void draw() {

        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(0, 0, width, height);

        gc.setLineWidth(height/250);
        gc.setStroke(Color.BLACK);

        List<Segment> segments = mapData.getSegments();

        for (Segment s : segments) {

            Intersection origin = s.getOrigin();
            double[] originPos = ProjectionUtils.projectLatLon(
                    origin.getLatitude(),
                    origin.getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            Intersection destination = s.getDestination();
            double[] destinationPos = ProjectionUtils.projectLatLon(
                    destination.getLatitude(),
                    destination.getLongitude(),
                    mapData.getMinLat(),
                    mapData.getMinLon(),
                    mapData.getMaxLat(),
                    mapData.getMaxLon(),
                    width,
                    height
            );

            gc.strokeLine(originPos[0], originPos[1], destinationPos[0], destinationPos[1]);

        }
    }

    @Override
    public boolean isResizable() {
        return true;
    }

}
