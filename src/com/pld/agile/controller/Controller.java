package com.pld.agile.controller;

import com.pld.agile.model.map.MapData;
import com.pld.agile.utils.parsing.MapLoader;
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

        // Load map
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.setInitialDirectory(new File("./src/resources"));
        File mapFile = fileChooser.showOpenDialog(window.getStage());

        if (mapFile != null) {

            MapData mapData = MapData.getInstance();
            MapLoader mapLoader = new MapLoader(mapFile.getPath(), mapData);
            boolean success = mapLoader.load();

            // Switch scenes
            if (success) {
                window.switchSceneToMainScene();
            }

        }

    }

}
