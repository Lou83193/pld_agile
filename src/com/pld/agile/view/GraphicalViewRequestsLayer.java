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

        List<Stop> stops = tourData.getStopsList();
        this.getChildren().clear();

        double graphicSize = 32 * screenScale * mapScale;

        for (Stop stop : stops) {

            GraphicalViewStop stopGraphic = new GraphicalViewStop(
                stop,
                graphicalView,
                graphicSize,
                stop.getStopNumber() > 0
            );
            double[] stopPos = graphicalView.projectLatLon(stop.getAddress());
            stopGraphic.place(stopPos);
            this.getChildren().add(stopGraphic);

        }

    }
}
