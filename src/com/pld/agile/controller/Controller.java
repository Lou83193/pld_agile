package com.pld.agile.controller;

import com.pld.agile.model.map.MapData;
import com.pld.agile.model.tour.TourData;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.utils.parsing.RequestsLoader;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;
import java.io.File;

public class Controller {

    private static Controller singletonInstance;

    private Controller() {}

    public static Controller getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Controller();
        }
        return singletonInstance;
    }

    public void loadMap() {

        Window window = Window.getInstance();

        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.setInitialDirectory(new File("./src/resources"));
        File mapFile = fileChooser.showOpenDialog(window.getStage());

        if (mapFile != null) {

            // Load map
            MapData mapData = MapData.getInstance();
            MapLoader mapLoader = new MapLoader(mapFile.getPath(), mapData);
            boolean success = mapLoader.load();

            // Switch scenes
            if (success) {
                window.switchSceneToMainScene();
            }

        }

    }

    public void loadTour() {

        Window window = Window.getInstance();

        // Fetch file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Tour File");
        fileChooser.setInitialDirectory(new File("./src/resources"));
        File requestsFile = fileChooser.showOpenDialog(window.getStage());

        if (requestsFile != null) {

            // Load tour
            TourData tourData = TourData.getInstance();
            RequestsLoader requestsLoader = new RequestsLoader(requestsFile.getPath(), tourData);
            boolean success = requestsLoader.load();

        }

    }

}
