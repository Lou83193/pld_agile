package com.pld.agile.controller;

import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has already started to add a new request.
 * (ie a pickup has been placed). User can only click on the graphical view
 * (to add a delivery stop).
 */
public class AddingRequestState2 implements State {

    @Override
    public void doClickOnGraphicalView(Controller c, Window window) {
        // i will need to pass some sort of cooordinates as parameter. or an intersection. hmm...
    }

}
