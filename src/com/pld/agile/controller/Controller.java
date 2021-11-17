package com.pld.agile.controller;

import com.pld.agile.model.MapData;
import com.pld.agile.utils.parsing.MapLoader;
import com.pld.agile.view.Window;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    private Window window;

    public Controller(Window window) {
        this.window = window;
    }

    public void loadMap() {

        // Load map
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        File mapFile = fileChooser.showOpenDialog(window.getStage());

        if (mapFile != null) {

            MapData mapData = new MapData();
            MapLoader mapLoader = new MapLoader(mapFile.getPath(), mapData);
            boolean success = mapLoader.load();
            System.out.println("SUCCESS : " + success);
            System.out.println(mapData);

            // Switch scenes
            if (success) {
                window.setMainScene(window.constructMainScene());
                window.switchScene(window.getMainScene());
            }

        }

    }

}
