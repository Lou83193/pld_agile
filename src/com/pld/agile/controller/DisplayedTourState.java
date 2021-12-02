package com.pld.agile.controller;

import com.pld.agile.model.tour.Path;
import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.utils.tsp.Graph;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

/**
 * State when the map and a list of requests are loaded.
 * User can load another map, load another list of requests or ask the app to compute the tour.
 */
public class DisplayedTourState implements State {

    /**
     * Loads the requests to tourData if map is loaded (default doesn't load).
     * @param c the controller
     * @param window the application window
     * @return boolean success
     */
    @Override
    public boolean doLoadRequests(Controller c, Window window) {
        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tour File");
        fileChooser.setInitialDirectory(new File("./src/resources/xml/requests"));
        File requestsFile = fileChooser.showOpenDialog(window.getStage());

        if (requestsFile != null) {

            RequestLoader requestsLoader = new RequestLoader(requestsFile.getPath(), window.getTourData());
            boolean success = requestsLoader.load();

            if (success) {
                window.toggleFileMenuItem(2, true);
                window.setMainSceneButton(
                        "Compute tour",
                        new ButtonListener(c, ButtonEventType.COMPUTE_TOUR)
                );
                window.placeMainSceneButton(false);
                c.setCurrState(c.displayedRequestsState);
            }
            return success;

        }
        return false;
    }

    @Override
    public void doClickOnGraphicalStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.getWarehouse().setHighlighted(false);
        for (Request request : tourData.getRequestList()) {
            request.getPickup().setHighlighted(false);
            request.getDelivery().setHighlighted(false);
        }
        stop.setHighlighted(true);
    }

    @Override
    public void doClickOnTextualStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        stop.setHighlighted(true);
    }

    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
    }

    @Override
    public void doDeleteRequest(Controller c, Window window, Request request) {
        Stop pickup = request.getPickup();
        Stop delivery = request.getDelivery();
        Stop currentOrigin = null;
        Stop currentDestination = null;

        List<Path> tourPath = window.getTourData().getTourPaths();

        for (int i = 0; i<tourPath.size(); i++) {
            Path path = tourPath.get(i);

            /** If we found the request which we want to remove in the previous iteration,
             * we find the new path between the previous stop and the next stop,
             * and we add it to the tourPath.
             */
            if (currentOrigin != null) {

                //Store destination and remove path to it
                currentDestination = path.getDestination();
                tourPath.remove(path);

                //Find new path
                Graph stopsGraph = window.getTourData().getStopsGraph();
                List<Integer> stops = window.getTourData().getStops();
                int indexOrigin = -1, indexDestination = -1;
                for (int j = 0; j < stops.size(); j++) {
                    if(stops.get(j) == currentOrigin.getAddress().getId()){
                        indexOrigin = j;
                    } else if (stops.get(j) == currentDestination.getAddress().getId()){
                        indexDestination = j;
                    }
                    if(indexOrigin != -1 && indexDestination != -1) { break; }
                }

                Path newPath = stopsGraph.getPath(indexOrigin, indexDestination);

                //Insert in position i
                tourPath.add(i, newPath);
                currentOrigin = null;
            }

            /**
             * If the destination of the current path
             * is the stop which we want to remove
             * we store the origin of that stop and
             * remove the path to it
             */
            if (path.getDestination() == pickup || path.getDestination() == delivery) {
                currentOrigin = path.getOrigin();
                tourPath.remove(path);
                i--;
            }
        }

        window.getTourData().setStopTimeAndNumber();
    }

    @Override
    public boolean doShiftStopOrderUp(Controller c, Window window, Stop stop) {
        // here it might be easier to construct a list of stops from the tour (using tourPaths, the list of paths)
        // then shift the stop up or down
        // then reconstruct tourPaths by iterating through the modified order list and fetching the paths in the graph
        // (otherwise you could also directly manipulate the list of paths but it might be a tricky algorithm)
        // don't forget to change order attribute in stop
        return false;
    }

    @Override
    public boolean doShiftStopOrderDown(Controller c, Window window, Stop stop) {
        // here it might be easier to construct a list of stops from the tour (using tourPaths, the list of paths)
        // then shift the stop up or down
        // then reconstruct tourPaths by iterating through the modified order list and fetching the paths in the graph
        // (otherwise you could also directly manipulate the list of paths but it might be a tricky algorithm)
        // don't forget to change order attribute in stop
        return false;
    }

    @Override
    public void doStartAddRequest(Controller c, Window window) {
        System.out.println("DoSstartAddRequest");
        TourData tourData = window.getTourData();
        int nbRequests = tourData.getRequestList().size();
        tourData.unHighlightStops();
        c.setCurrState(c.addingRequestState1);
    }

    @Override
    public void doDragOnGraphicalStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        c.setCurrState(c.movingStopState);
    }

}
