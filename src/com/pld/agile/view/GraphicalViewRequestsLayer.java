package com.pld.agile.view;

import com.pld.agile.model.map.Intersection;
import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import java.util.List;

/**
 * Class handling the graphical display of the requests.
 */
public class GraphicalViewRequestsLayer extends Group {

    /**
     * The associated graphical map layer.
     */
    private Pane graphicalMap;
    /**
     * The application's MapData instance.
     */
    private MapData mapData;
    /**
     * The application's TourData instance.
     */
    private TourData tourData;
    /**
     * Boolean switch instructing whether to draw the requests as a tour or not.
     */
    private boolean drawTour = false;

    /**
     * GraphicalViewRequestsLayer constructor.
     * @param mapData The application's MapData instance
     * @param tourData The application's TourData instance
     * @param graphicalMap The associated graphical map layer
     */
    public GraphicalViewRequestsLayer(MapData mapData, TourData tourData, Pane graphicalMap) {
        this.mapData = mapData;
        this.tourData = tourData;
        this.graphicalMap = graphicalMap;
    }

    /**
     * Draws the requests, either as a tour or unordered.
     */
    public void draw() {
        if (drawTour) {
            drawTour();
        } else {
            drawInitial();
        }
    }

    /**
     * Draws the requests in an unordered fashion, by populating the pane with
     * graphical stops.
     */
    public void drawInitial() {

        double screenScale = ViewUtilities.mapValue(
                graphicalMap.getHeight(),
                0, 720,
                0, 1
        );
        double mapScale = ViewUtilities.mapValue(
                mapData.getMaxLon() - mapData.getMinLon(),
                0.02235, 0.07610,
                1.25, 0.75
        );

        List<Request> requests = tourData.getRequestList();
        this.getChildren().clear();

        if (requests.size() == 0) {
            return;
        }

        double graphicSize = 24 * screenScale * mapScale;

        Stop warehouse = tourData.getWarehouse();
        GraphicalViewStop warehouseGraphic = new GraphicalViewStop(warehouse, graphicSize, 0);
        double[] warehousePos = projectLatLon(warehouse.getAddress());
        warehouseGraphic.place(warehousePos);
        this.getChildren().add(warehouseGraphic);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            GraphicalViewStop pickupGraphic = new GraphicalViewStop(pickup, graphicSize, 0);
            double[] pickupPos = projectLatLon(pickup.getAddress());
            pickupGraphic.place(pickupPos);
            this.getChildren().add(pickupGraphic);

            Stop delivery = request.getDelivery();
            GraphicalViewStop deliveryGraphic = new GraphicalViewStop(delivery, graphicSize, 0);
            double[] deliveryPos = projectLatLon(delivery.getAddress());
            deliveryGraphic.place(deliveryPos);
            this.getChildren().add(deliveryGraphic);

        }

    }

    /**
     * Draws the requests in an ordered fashion, by populating the pane with
     * graphical stops.
     */
    public void drawTour() {

        double screenScale = ViewUtilities.mapValue(
                graphicalMap.getHeight(),
                0, 720,
                0, 1
        );
        double mapScale = ViewUtilities.mapValue(
                mapData.getMaxLon() - mapData.getMinLon(),
                0.02235, 0.07610,
                1.25, 0.75
        );

        List<Request> requests = tourData.getRequestList();
        List<Integer> tourOrder = tourData.getComputedPath();
        this.getChildren().clear();

        if (requests.size() == 0) {
            return;
        }

        double graphicSize = 24 * screenScale * mapScale;

        int count = 0;
        for (Integer i : tourOrder) {
            Integer stopId = tourData.getStops().get(i);
            Stop stop = tourData.getStopMap().get(stopId);
            GraphicalViewStop stopGraphic = new GraphicalViewStop(stop, graphicSize, count);
            double[] stopPos = projectLatLon(stop.getAddress());
            stopGraphic.place(stopPos);
            this.getChildren().add(stopGraphic);
            count++;
        }

    }

    /**
     * Projects an intersection's (lat; lon) address to a pixel coordinate
     * based on the container's size and the mapData's bounds.
     * @param intersection The intersection whose address to project.
     * @return A double[] containing the {x, y} projection.
     */
    private double[] projectLatLon(final Intersection intersection) {
        return ViewUtilities.projectMercatorLatLon(
                intersection.getLatitude(),
                intersection.getLongitude(),
                mapData.getMinLat(),
                mapData.getMinLon(),
                mapData.getMaxLat(),
                mapData.getMaxLon(),
                graphicalMap.getHeight()
        );
    }

    /**
     * Setter for attribute drawTour.
     * @param drawTour Whether to draw the tour's trace or not
     */
    public void setDrawTour(final boolean drawTour) {
        this.drawTour = drawTour;
    }
}
