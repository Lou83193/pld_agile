package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class handling the graphical display of the requests.
 */
public class GraphicalViewRequestsLayer extends Group {

    /**
     * The parent GraphicalView instance.
     */
    private GraphicalView graphicalView;

    /**
     * GraphicalViewRequestsLayer constructor.
     * @param graphicalView The parent GraphicalView instance
     */
    public GraphicalViewRequestsLayer(GraphicalView graphicalView) {
        this.graphicalView = graphicalView;
    }

    /**
     * Draws the requests, by populating the pane with graphical stops.
     */
    public void draw() {

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

        final EventHandler<MouseEvent> dropOff = (event) -> {
            double[] latLonPos = ViewUtilities.projectMercatorLatLonInv(
                    event.getX(),
                    event.getY(),
                    graphicalView.getMapData().getMinLat(),
                    graphicalView.getMapData().getMaxLat(),
                    graphicalView.getMapData().getMinLon(),
                    graphicalView.getMapData().getMaxLon(),
                    ((ScrollPane) graphicalView.getComponent()).getHeight()
            );
            graphicalView.getWindow().getController().dragOffGraphicalStop(latLonPos);
        };

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            GraphicalViewStop pickupGraphic = new GraphicalViewStop(pickup, graphicSize, pickup.getStopNumber());
            double[] pickupPos = graphicalView.projectLatLon(pickup.getAddress());
            pickupGraphic.place(pickupPos);
            pickupGraphic.setOnMouseClicked(e -> graphicalView.getWindow().getController().clickOnGraphicalStop(pickup));
            pickupGraphic.setOnMouseDragEntered(e -> graphicalView.getWindow().getController().dragOnGraphicalStop(pickup));
            pickupGraphic.setOnMouseDragExited(dropOff);
            this.getChildren().add(pickupGraphic);

            Stop delivery = request.getDelivery();
            GraphicalViewStop deliveryGraphic = new GraphicalViewStop(delivery, graphicSize, delivery.getStopNumber());
            double[] deliveryPos = graphicalView.projectLatLon(delivery.getAddress());
            deliveryGraphic.place(deliveryPos);
            deliveryGraphic.setOnMouseClicked(e -> graphicalView.getWindow().getController().clickOnGraphicalStop(delivery));
            deliveryGraphic.setOnMouseDragExited(dropOff);
            this.getChildren().add(deliveryGraphic);

        }

    }
}
