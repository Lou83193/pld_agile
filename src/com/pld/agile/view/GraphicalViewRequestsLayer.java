package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Class handling the graphical display of the requests.
 */
public class GraphicalViewRequestsLayer extends Group {

    /**
     * The parent GraphicalView instance.
     */
    private GraphicalView graphicalView;
    /**
     * Boolean switch instructing whether to draw the requests as a tour or not.
     */
    private boolean drawTour = false;

    /**
     * GraphicalViewRequestsLayer constructor.
     * @param graphicalView The parent GraphicalView instance
     */
    public GraphicalViewRequestsLayer(GraphicalView graphicalView) {
        this.graphicalView = graphicalView;
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

        GraphicalViewMapLayer graphicalMap = graphicalView.getGraphicalViewMapLayer();
        MapData mapData = graphicalView.getMapData();
        TourData tourData = graphicalView.getTourData();

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
        double[] warehousePos = graphicalView.projectLatLon(warehouse.getAddress());
        warehouseGraphic.place(warehousePos);
        warehouseGraphic.setOnMouseClicked(e -> graphicalView.getWindow().getController().clickOnGraphicalStop(warehouse));
        this.getChildren().add(warehouseGraphic);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            GraphicalViewStop pickupGraphic = new GraphicalViewStop(pickup, graphicSize, 0);
            double[] pickupPos = graphicalView.projectLatLon(pickup.getAddress());
            pickupGraphic.place(pickupPos);
            pickupGraphic.setOnMouseClicked(e -> graphicalView.getWindow().getController().clickOnGraphicalStop(pickup));
            this.getChildren().add(pickupGraphic);

            Stop delivery = request.getDelivery();
            GraphicalViewStop deliveryGraphic = new GraphicalViewStop(delivery, graphicSize, 0);
            double[] deliveryPos = graphicalView.projectLatLon(delivery.getAddress());
            deliveryGraphic.place(deliveryPos);
            deliveryGraphic.setOnMouseClicked(e -> graphicalView.getWindow().getController().clickOnGraphicalStop(delivery));
            this.getChildren().add(deliveryGraphic);

        }

    }

    /**
     * Draws the requests in an ordered fashion, by populating the pane with
     * graphical stops.
     */
    public void drawTour() {

        GraphicalViewMapLayer graphicalMap = graphicalView.getGraphicalViewMapLayer();
        MapData mapData = graphicalView.getMapData();
        TourData tourData = graphicalView.getTourData();

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
            double[] stopPos = graphicalView.projectLatLon(stop.getAddress());
            stopGraphic.place(stopPos);
            stopGraphic.setOnMouseClicked(e -> graphicalView.getWindow().getController().clickOnGraphicalStop(stop));
            this.getChildren().add(stopGraphic);
            count++;
        }

    }

    /**
     * Setter for attribute drawTour.
     * @param drawTour Whether to draw the tour's trace or not
     */
    public void setDrawTour(final boolean drawTour) {
        this.drawTour = drawTour;
    }
}
