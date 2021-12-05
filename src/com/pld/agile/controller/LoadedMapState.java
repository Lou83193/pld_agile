package com.pld.agile.controller;

import com.pld.agile.utils.exception.SyntaxException;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.ButtonEventType;
import com.pld.agile.view.ButtonListener;
import com.pld.agile.view.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * State when the map is loaded.
 * User can load another map, or load a request xml file.
 */
public class LoadedMapState implements State {

    /**
     * Loads the requests to tourData if map is loaded (default doesn't load).
     * @param c the controller
     * @param w the application window
     * @return boolean success
     */
    @Override
    public boolean doLoadRequests(final Controller c, final Window w) {
        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tour File");
        fileChooser.setInitialDirectory(new File("./src/resources/xml/requests"));
        File requestsFile = fileChooser.showOpenDialog(w.getStage());

        if (requestsFile != null) {
            RequestLoader requestsLoader = new RequestLoader(requestsFile.getPath(), w.getTourData());
            try {
                requestsLoader.load();
                w.toggleFileMenuItem(2, true);
                w.setMainSceneButton(
                        "Compute tour",
                        new ButtonListener(c, ButtonEventType.COMPUTE_TOUR)
                );
                w.placeMainSceneButton(false);
                c.setCurrState(c.loadedRequestsState);
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

}
