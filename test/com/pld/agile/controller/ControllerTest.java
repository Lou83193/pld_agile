/*
 * ControllerTest
 *
 * Copyright (c) 2021. Hexanomnom
 */

package com.pld.agile.controller;

import com.pld.agile.view.Window;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    public void testController(){
        Window window = new Window();
        Controller controller = new Controller(window);
        assertEquals(window.toString(),controller.getWindow().toString());
        assertEquals(controller.getCurrState().toString(),controller.getAwaitMapState().toString());
    }
    @Test
    void loadMap() {

    }

    @Test
    void loadTour() {
    }

    @Test
    void computeTour() {
    }

    @Test
    void clickOnGraphicalStop() {
    }

    @Test
    void clickOnTextualStop() {
    }

    @Test
    void clickOnGraphicalView() {
    }

    @Test
    void deleteRequest() {
    }

    @Test
    void shiftStopOrderUp() {
    }

    @Test
    void shiftStopOrderDown() {
    }

    @Test
    void changeStopDuration() {
    }

    @Test
    void startAddRequest() {
    }

    @Test
    void dragOnGraphicalStop() {
    }

    @Test
    void dragOffGraphicalStop() {
    }

    @Test
    void changeWarehouseDepartureTime() {
    }
}