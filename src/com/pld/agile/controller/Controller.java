package com.pld.agile.controller;

import com.pld.agile.view.Window;

public class Controller {

    private Window window;

    public Controller(Window window) {
        this.window = window;
    }

    public void loadMap(String path) {

        // Load map

        // Switch scenes
        window.setMainScene(window.constructMainScene());
        window.setScene(window.getMainScene());

    }

}
