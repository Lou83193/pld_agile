package com.pld.agile.controller;

import com.pld.agile.view.Window;

/**
 * State when the map and a list of requests are loaded, the corresponding
 * tour is computed, and the user has asked to add another request.
 * User can only click on the graphical view (to add a pickup stop).
 */
public class AddingRequestState1 implements State {

    @Override
    public void doClickOnGraphicalView(Controller c, Window window) {
        // i will need to pass some sort of cooordinates as parameter. or an intersection. hmm...
    }

}
