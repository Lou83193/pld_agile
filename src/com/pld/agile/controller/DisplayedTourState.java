package com.pld.agile.controller;

import com.pld.agile.model.tour.Request;
import com.pld.agile.model.tour.Stop;
import com.pld.agile.model.tour.StopType;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

import static com.pld.agile.model.tour.StopType.DELIVERY;
import static com.pld.agile.model.tour.StopType.PICKUP;

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
            try {
                requestsLoader.load();
                window.toggleFileMenuItem(2, true);
                window.setMainSceneButton(
                        "Compute tour",
                        new ButtonListener(c, ButtonEventType.COMPUTE_TOUR)
                );
                window.placeMainSceneButton(false);
                c.setCurrState(c.displayedRequestsState);

                return true;
            } catch (SyntaxException | IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Error"); // force english
                alert.setHeaderText("Requests loading error");
                alert.showAndWait();
                return false;
            }
        }
        return false;
    }

    @Override
    public void doClickOnGraphicalStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        stop.setHighlighted(2);
        if (stop.getType() != StopType.WAREHOUSE) {
            if (stop.getRequest().getPickup().equals(stop)) {
                stop.getRequest().getDelivery().setHighlighted(1);
            }
            else {
                stop.getRequest().getPickup().setHighlighted(1);
            }
        }
    }

    @Override
    public void doClickOnTextualStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        stop.setHighlighted(2);
        if (stop.getType() != StopType.WAREHOUSE) {
            if (stop.getRequest().getPickup().equals(stop)) {
                stop.getRequest().getDelivery().setHighlighted(1);
            }
            else {
                stop.getRequest().getPickup().setHighlighted(1);
            }
        }
    }

    @Override
    public void doClickOnGraphicalView(Controller c, Window window, double[] latLonPos) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
    }

    @Override
    public void doDeleteRequest(Controller c, Window window, Request request) {
        TourData tourData = window.getTourData();
        tourData.deleteRequest(request);
    }

    @Override
    public boolean doShiftStopOrderUp(Controller c, Window window, Stop stop) {
        ArrayList<Stop> listStops = new ArrayList<>();
        List<Path> tourPath = window.getTourData().getTourPaths();

        listStops.add(tourPath.get(0).getOrigin());
        int indexStop = 0;

        //Build a list of all the stops in the tour in order
        for(int i = 0; i < tourPath.size(); i++) {
            Stop currStop = tourPath.get(i).getDestination();
            listStops.add(currStop);
            if (currStop == stop) { indexStop = i; }
        }

        //Check if the stop is allowed to move up
        boolean canMove = true;
        Stop stopAbove = null;
        if (indexStop < 1) {
            canMove = false;
        } else {
            stopAbove = listStops.get(indexStop - 2);
            if (stop.getType() == DELIVERY) {
                System.out.println("delivery");
                //todo : check if delivery above pickup
                if (stop.getRequest().equals(stopAbove.getRequest())) { System.out.println("cannot move"); canMove = false; }
            }
        }
        System.out.println("check done :" + canMove);

        if (canMove) {
            //Shift the stop up one place
            listStops.add(indexStop, stopAbove);
            listStops.add(indexStop - 1, stop);
            System.out.println("stop moved");

            //Reconstruct tourData
            for (int i = 0; i < listStops.size() - 1; i++) {
                //Update stopNumber
                listStops.get(i).setStopNumber(i);

                //Get index of stops

                List<Integer> stops = window.getTourData().getStops();
                int indexOrigin = -1, indexDestination = -1;
                for (int j = 0; j < stops.size(); j++) {
                    if (stops.get(j) == listStops.get(i).getAddress().getId()) {
                        indexOrigin = j;
                    } else if (stops.get(j) == listStops.get(i + 1).getAddress().getId()) {
                        indexDestination = j;
                    }
                    if (indexOrigin != -1 && indexDestination != -1) {
                        break;
                    }
                }

                //Add path to tourPath
                Graph stopsGraph = window.getTourData().getStopsGraph();
                tourPath.clear();
                tourPath.add(stopsGraph.getPath(indexOrigin, indexDestination));
                System.out.println("path added");
            }

            System.out.println("DONE");
            return true;
        }

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
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        window.getScene().setCursor(Cursor.CROSSHAIR);
        window.toggleMainSceneButton(false);
        c.setCurrState(c.addingRequestState1);
    }

    @Override
    public void doDragOnGraphicalStop(Controller c, Window window, Stop stop) {
        TourData tourData = window.getTourData();
        tourData.unHighlightStops();
        c.setCurrState(c.movingStopState);
    }

}
