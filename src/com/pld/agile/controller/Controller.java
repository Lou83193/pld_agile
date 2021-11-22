package com.pld.agile.controller;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.parsing.RequestLoader;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;
import java.io.File;

public class Controller {

    private Window window;

    public Controller (Window window) {
        this.window = window;
    }

    public void loadMap() {

        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.setInitialDirectory(new File("./src/resources"));
        File mapFile = fileChooser.showOpenDialog(window.getStage());

        if (mapFile != null) {

            // Load map
            MapLoader mapLoader = new MapLoader(mapFile.getPath(), window.getMapData());
            boolean success = mapLoader.load();

            // Switch scenes
            if (success) {
                window.getTourData().setAssociatedMap(window.getMapData());
                window.switchSceneToMainScene();
            }

        }

    }

    public void loadTour() {

        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tour File");
        fileChooser.setInitialDirectory(new File("./src/resources"));
        File requestsFile = fileChooser.showOpenDialog(window.getStage());

        if (requestsFile != null) {

            // Load tour
            RequestLoader requestsLoader = new RequestLoader(requestsFile.getPath(), window.getTourData());
            boolean success = requestsLoader.load();

        }

    }

    public void computeTour() {



    }

}
