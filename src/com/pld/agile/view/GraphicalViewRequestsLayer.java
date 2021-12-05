package com.pld.agile.view;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.view.ViewUtilities;
import javafx.scene.Group;
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
     * GraphicalViewRequestsLayer constructor.
     * @param graphicalView The parent GraphicalView instance
     */
    public GraphicalViewRequestsLayer(GraphicalView graphicalView) {
        this.graphicalView = graphicalView;
        this.setPickOnBounds(false);
    }

    /**
     * Draws the requests, by populating the pane with graphical stops.
     */
    public void draw() {

        GraphicalViewMapLayer graphicalMap = graphicalView.getGraphicalViewMapLayer();
        MapData mapData = graphicalView.getWindow().getMapData();
        TourData tourData = graphicalView.getWindow().getTourData();

        double screenScale = ViewUtilities.mapValue(
                graphicalMap.getHeight(),
                0, 1080,
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

        double graphicSize = 32 * screenScale * mapScale;

        Stop warehouse = tourData.getWarehouse();
        GraphicalViewStop warehouseGraphic = new GraphicalViewStop(warehouse, graphicalView, graphicSize, 0, false);
        double[] warehousePos = graphicalView.projectLatLon(warehouse.getAddress());
        warehouseGraphic.place(warehousePos);
        this.getChildren().add(warehouseGraphic);

        for (Request request : requests) {

            Stop pickup = request.getPickup();
            if (pickup != null) {
                GraphicalViewStop pickupGraphic = new GraphicalViewStop(
                        pickup,
                        graphicalView,
                        graphicSize,
                        pickup.getStopNumber(),
                        pickup.getStopNumber() > 0
                );
                double[] pickupPos = graphicalView.projectLatLon(pickup.getAddress());
                pickupGraphic.place(pickupPos);
                this.getChildren().add(pickupGraphic);
            }

            Stop delivery = request.getDelivery();
            if (delivery != null) {
                GraphicalViewStop deliveryGraphic = new GraphicalViewStop(
                        delivery,
                        graphicalView,
                        graphicSize,
                        delivery.getStopNumber(),
                        pickup.getStopNumber() > 0
                );
                double[] deliveryPos = graphicalView.projectLatLon(delivery.getAddress());
                deliveryGraphic.place(deliveryPos);
                this.getChildren().add(deliveryGraphic);
            }
        }

    }
}
