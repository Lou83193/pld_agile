package com.pld.agile.view;

/**
 * Enum describing the types of button press events.
 */
public enum ButtonEventType {
    /**
     * Event invoked to load a map.
     */
    LOAD_MAP,
    /**
     * Event invoked to load a set of requests.
     */
    LOAD_REQUESTS,
    /**
     * Event invoked to compute a tour with the loaded requests.
     */
    COMPUTE_TOUR,
    /**
     * Event invoked to stop the tour computation.
     */
    STOP_COMPUTING_TOUR,
    /**
     * Event invoked to add a request to the tour.
     */
    ADD_REQUEST
}
